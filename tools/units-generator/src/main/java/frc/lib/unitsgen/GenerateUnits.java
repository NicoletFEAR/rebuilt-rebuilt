package frc.lib.unitsgen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Generates the classes in the units package from units.json.
 *
 * <p>Usage: {@code GenerateUnits <spec-path> <output-dir>}
 *
 * <p>The specification lists one entry per quantity. Each entry provides:
 *
 * <ul>
 *   <li>{@code name} — class name, e.g. {@code "Distance"} (the matching unit class is {@code
 *       DistanceUnit}).
 *   <li>{@code dimension} — 8 integer exponents describing the physical dimension, in the order
 *       <em>length, mass, time, current, temperature, amount, luminous intensity, angle</em>. The
 *       angle exponent is treated as a base dimension, so torques and action-per-angle work out
 *       correctly. Entries whose dimension vectors collide are rejected; products and quotients
 *       that land on another entry's dimension vector generate {@code times}/{@code div} methods on
 *       both operands automatically (e.g. {@code Distance.div(Time) -> Velocity}).
 *   <li>{@code baseUnitSymbol} — symbol used when logging, e.g. {@code "m"}.
 *   <li>{@code units} — ordered map of constant name to conversion factor (in base units). The
 *       constant for factor 1.0 is the base unit, e.g. {@code "Meters": 1}.
 * </ul>
 *
 * <p>Optional per-entry fields:
 *
 * <ul>
 *   <li>{@code wpiUnits} — {@code {"measureType": "...", "unitName": "...", "constant": "..."}};
 *       emits a static {@code ofWpi(org.wpilib.units.measure.<measureType>)} factory that converts
 *       via {@code <constant>.of(quantity.in(org.wpilib.units.Units.<unitName>))}. {@code constant}
 *       is the name of the unit constant in {@code units} used to wrap the value; it defaults to
 *       {@code unitName} and must exist in {@code units}.
 *   <li>{@code imports} — extra imports spliced into both the quantity and unit classes (for
 *       spec-embedded {@code quantityCode}/{@code unitCode} snippets).
 *   <li>{@code quantityCode} — raw method source spliced into the quantity class right after the
 *       constructor (escape hatch for behavior that can't be expressed in the spec, e.g. rotation
 *       conversions).
 *   <li>{@code unitCode} — raw method source spliced into the unit class right after the {@code
 *       of}/{@code ofWpi} methods and before the unit constants.
 * </ul>
 */
public final class GenerateUnits {
    private static final int DIMENSION_COUNT = 8;
    private static final Pattern PACKAGE_NAME =
            Pattern.compile("[a-zA-Z_$][a-zA-Z0-9_$]*(\\.[a-zA-Z_$][a-zA-Z0-9_$]*)*");
    private static final Pattern TYPE_NAME = Pattern.compile("[A-Za-z_$][A-Za-z0-9_$]*");

    private GenerateUnits() {}

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("usage: GenerateUnits <spec-path> <output-dir>");
            System.exit(2);
        }

        Path specPath = Path.of(args[0]);
        Path outputDir = Path.of(args[1]);

        Spec spec = parse(specPath);
        validate(spec);

        Path packageDir = outputDir.resolve(spec.packageName().replace('.', '/'));
        deleteRecursively(packageDir);
        Files.createDirectories(packageDir);

        for (QuantitySpec quantity : spec.quantities()) {
            write(packageDir, quantity.name() + ".java", quantityFile(spec, quantity));
            write(packageDir, quantity.name() + "Unit.java", unitFile(spec, quantity));
        }
    }

    private static Spec parse(Path specPath) throws IOException {
        JsonObject root = JsonParser.parseString(Files.readString(specPath, StandardCharsets.UTF_8))
                .getAsJsonObject();

        String packageName = requiredString(root, "package", "<root>");

        List<QuantitySpec> quantities = new ArrayList<>();
        for (JsonElement element : root.getAsJsonArray("quantities")) {
            JsonObject object = element.getAsJsonObject();
            String name = requiredString(object, "name", "quantity");
            String where = "quantity " + name;

            int[] dimension = new int[DIMENSION_COUNT];
            JsonElement dimensionElement = object.get("dimension");
            if (dimensionElement == null) {
                throw new IllegalArgumentException(where + ": missing \"dimension\"");
            }
            JsonElement[] components =
                    dimensionElement.getAsJsonArray().asList().toArray(new JsonElement[0]);
            if (components.length != DIMENSION_COUNT) {
                throw new IllegalArgumentException(
                        where + ": \"dimension\" must have exactly " + DIMENSION_COUNT + " entries");
            }
            for (int i = 0; i < DIMENSION_COUNT; i++) {
                dimension[i] = components[i].getAsInt();
            }

            String baseUnitSymbol = requiredString(object, "baseUnitSymbol", where);

            LinkedHashMap<String, Double> units = new LinkedHashMap<>();
            JsonObject unitsObject = object.getAsJsonObject("units");
            if (unitsObject != null) {
                for (Map.Entry<String, JsonElement> unit : unitsObject.entrySet()) {
                    units.put(unit.getKey(), unit.getValue().getAsDouble());
                }
            }

            WpiUnits wpiUnits = null;
            JsonObject wpiUnitsObject = object.getAsJsonObject("wpiUnits");
            if (wpiUnitsObject != null) {
                String unitName = requiredString(wpiUnitsObject, "unitName", where + " wpiUnits");
                JsonElement constantElement = wpiUnitsObject.get("constant");
                String constant = constantElement == null ? unitName : constantElement.getAsString();
                wpiUnits = new WpiUnits(
                        requiredString(wpiUnitsObject, "measureType", where + " wpiUnits"), unitName, constant);
            }

            quantities.add(new QuantitySpec(
                    name,
                    dimension,
                    baseUnitSymbol,
                    units,
                    wpiUnits,
                    stringList(object, "imports", where),
                    stringList(object, "quantityCode", where),
                    stringList(object, "unitCode", where)));
        }

        return new Spec(packageName, quantities);
    }

    private static void validate(Spec spec) {
        if (!PACKAGE_NAME.matcher(spec.packageName()).matches()) {
            throw new IllegalArgumentException("invalid package name: " + spec.packageName());
        }

        Map<String, QuantitySpec> byDimension = new LinkedHashMap<>();
        Map<String, QuantitySpec> byName = new LinkedHashMap<>();
        for (QuantitySpec quantity : spec.quantities()) {
            if (!TYPE_NAME.matcher(quantity.name()).matches()) {
                throw new IllegalArgumentException("invalid quantity name: " + quantity.name());
            }
            QuantitySpec named = byName.putIfAbsent(quantity.name(), quantity);
            if (named != null) {
                throw new IllegalArgumentException("duplicate quantity name: " + quantity.name());
            }
            for (int exponent : quantity.dimension()) {
                if (Math.abs(exponent) > 8) {
                    throw new IllegalArgumentException(
                            quantity.name() + ": dimension exponent out of range: " + exponent);
                }
            }
            String dimensionKey = dimensionKey(quantity.dimension());
            QuantitySpec clash = byDimension.putIfAbsent(dimensionKey, quantity);
            if (clash != null) {
                throw new IllegalArgumentException("quantities "
                        + clash.name()
                        + " and "
                        + quantity.name()
                        + " share the same dimension "
                        + dimensionKey);
            }

            if (quantity.units().isEmpty()) {
                // Not an error: some quantities (e.g. spec placeholders) may not need unit constants
                // yet, but it is almost certainly a spec mistake, so be loud.
                System.err.println("warning: quantity " + quantity.name() + " defines no units");
            }
            for (Map.Entry<String, Double> unit : quantity.units().entrySet()) {
                if (!TYPE_NAME.matcher(unit.getKey()).matches()) {
                    throw new IllegalArgumentException(quantity.name() + ": invalid unit name: " + unit.getKey());
                }
                double factor = unit.getValue();
                if (!Double.isFinite(factor) || factor <= 0.0) {
                    throw new IllegalArgumentException(quantity.name()
                            + ": unit "
                            + unit.getKey()
                            + " must have a finite, positive factor, got "
                            + factor);
                }
            }

            if (quantity.wpiUnits() != null) {
                WpiUnits wpi = quantity.wpiUnits();
                if (!TYPE_NAME.matcher(wpi.measureType()).matches()
                        || !TYPE_NAME.matcher(wpi.unitName()).matches()
                        || !TYPE_NAME.matcher(wpi.constant()).matches()) {
                    throw new IllegalArgumentException(quantity.name() + ": invalid wpiUnits field in " + wpi);
                }
                if (!quantity.units().containsKey(wpi.constant())) {
                    throw new IllegalArgumentException(quantity.name()
                            + ": wpiUnits.constant \""
                            + wpi.constant()
                            + "\" is not a unit defined in \"units\"");
                }
            }
        }
    }

    private static String quantityFile(Spec spec, QuantitySpec quantity) {
        StringBuilder out = new StringBuilder();
        out.append(header());
        out.append("package ").append(spec.packageName()).append(";\n");
        out.append(imports(quantity));
        out.append("\n");
        out.append("public class ")
                .append(quantity.name())
                .append(" extends Quantity<")
                .append(quantity.name())
                .append(", ")
                .append(quantity.name())
                .append("Unit> {\n");
        out.append("    ")
                .append(quantity.name())
                .append("(double value) {\n        super(value, ")
                .append(javaString(quantity.baseUnitSymbol()))
                .append(");\n    }\n");

        for (String extra : quantity.quantityCode()) {
            out.append("\n    ").append(extra.replace("\n", "\n    ")).append("\n");
        }

        out.append("\n");
        out.append(boilerplateArithmetic(quantity));
        out.append(crossQuantityMethods(spec, quantity));
        out.append("}\n");
        return out.toString();
    }

    private static String boilerplateArithmetic(QuantitySpec quantity) {
        String name = quantity.name();
        StringBuilder out = new StringBuilder();
        out.append("    public ")
                .append(name)
                .append(" max(")
                .append(name)
                .append(" other) {\n")
                .append("        if (value < other.value) {\n")
                .append("            return other;\n")
                .append("        } else {\n")
                .append("            return this;\n")
                .append("        }\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" min(")
                .append(name)
                .append(" other) {\n")
                .append("        if (value > other.value) {\n")
                .append("            return other;\n")
                .append("        } else {\n")
                .append("            return this;\n")
                .append("        }\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" clamp(")
                .append(name)
                .append(" low, ")
                .append(name)
                .append(" high) {\n")
                .append("        return max(low).min(high);\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" plus(")
                .append(name)
                .append(" other) {\n")
                .append("        return new ")
                .append(name)
                .append("(value + other.value);\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" minus(")
                .append(name)
                .append(" other) {\n")
                .append("        return new ")
                .append(name)
                .append("(value - other.value);\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" times(double other) {\n")
                .append("        return new ")
                .append(name)
                .append("(value * other);\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" div(double other) {\n")
                .append("        return new ")
                .append(name)
                .append("(value / other);\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" negative() {\n")
                .append("        return new ")
                .append(name)
                .append("(-value);\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" ceil() {\n")
                .append("        return new ")
                .append(name)
                .append("(Math.ceil(value));\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(name)
                .append(" floor() {\n")
                .append("        return new ")
                .append(name)
                .append("(Math.floor(value));\n")
                .append("    }\n");
        return out.toString();
    }

    private static String crossQuantityMethods(Spec spec, QuantitySpec quantity) {
        Map<String, QuantitySpec> byDimension = new LinkedHashMap<>();
        for (QuantitySpec other : spec.quantities()) {
            byDimension.put(dimensionKey(other.dimension()), other);
        }

        StringBuilder out = new StringBuilder();
        for (QuantitySpec other : spec.quantities()) {
            QuantitySpec product = byDimension.get(dimensionKey(add(quantity.dimension(), other.dimension())));
            if (product != null) {
                out.append("\n    public ")
                        .append(product.name())
                        .append(" times(")
                        .append(other.name())
                        .append(" other) {\n        return new ")
                        .append(product.name())
                        .append("(value * other.value);\n    }\n");
            }
            QuantitySpec quotient = byDimension.get(dimensionKey(subtract(quantity.dimension(), other.dimension())));
            if (quotient != null) {
                out.append("\n    public ")
                        .append(quotient.name())
                        .append(" div(")
                        .append(other.name())
                        .append(" other) {\n        return new ")
                        .append(quotient.name())
                        .append("(value / other.value);\n    }\n");
            }
        }

        QuantitySpec reciprocal = byDimension.get(dimensionKey(negate(quantity.dimension())));
        if (reciprocal != null) {
            out.append("\n    public ")
                    .append(reciprocal.name())
                    .append(" reciprocal() {\n")
                    .append("        return new ")
                    .append(reciprocal.name())
                    .append("(1.0 / value);\n")
                    .append("    }\n");
        }
        return out.toString();
    }

    private static String unitFile(Spec spec, QuantitySpec quantity) {
        String unitClassName = quantity.name() + "Unit";
        StringBuilder out = new StringBuilder();
        out.append(header());
        out.append("package ").append(spec.packageName()).append(";\n");
        out.append(imports(quantity));
        out.append("\n");
        out.append("public class ")
                .append(unitClassName)
                .append(" extends Unit<")
                .append(unitClassName)
                .append("> {\n");
        out.append("    public ")
                .append(unitClassName)
                .append("(double inBaseUnits) {\n")
                .append("        super(inBaseUnits);\n")
                .append("    }\n\n");
        out.append("    public ")
                .append(quantity.name())
                .append(" of(double quantity) {\n")
                .append("        return new ")
                .append(quantity.name())
                .append("(quantity * inBaseUnits);\n")
                .append("    }\n");

        if (quantity.wpiUnits() != null) {
            WpiUnits wpi = quantity.wpiUnits();
            out.append("\n");
            out.append("    public static ")
                    .append(quantity.name())
                    .append(" ofWpi(org.wpilib.units.measure.")
                    .append(wpi.measureType())
                    .append(" quantity) {\n")
                    .append("        return ")
                    .append(wpi.constant())
                    .append(".of(quantity.in(org.wpilib.units.Units.")
                    .append(wpi.unitName())
                    .append("));\n")
                    .append("    }\n");
        }

        for (String extra : quantity.unitCode()) {
            out.append("\n    ").append(extra.replace("\n", "\n    ")).append("\n");
        }

        if (!quantity.units().isEmpty()) {
            out.append("\n");
            for (Map.Entry<String, Double> unit : quantity.units().entrySet()) {
                out.append(unitConstant(unitClassName, unit.getKey(), unit.getValue()));
            }
        }
        out.append("}\n");
        return out.toString();
    }

    private static String unitConstant(String unitClassName, String name, double factor) {
        String prefix = "    public static final " + unitClassName + " " + name + " = new " + unitClassName + "(";
        String literal = Double.toString(factor);
        String singleLine = prefix + literal + ");\n";
        if (singleLine.stripTrailing().length() <= 100) {
            return singleLine;
        }
        return "    public static final "
                + unitClassName
                + " "
                + name
                + " =\n"
                + "            new "
                + unitClassName
                + "("
                + literal
                + ");\n";
    }

    private static String imports(QuantitySpec quantity) {
        if (quantity.imports().isEmpty()) {
            return "";
        }
        StringBuilder out = new StringBuilder("\n");
        for (String importName : quantity.imports()) {
            out.append("import ").append(importName).append(";\n");
        }
        return out.toString();
    }

    private static String header() {
        return "// Generated by frc.lib.unitsgen.GenerateUnits from units.json. Do not edit.\n"
                + "// Make changes to src/main/resources/units.json instead.\n";
    }

    private static String javaString(String value) {
        StringBuilder out = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        return out.append("\"").toString();
    }

    private static int[] add(int[] a, int[] b) {
        int[] out = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = a[i] + b[i];
        }
        return out;
    }

    private static int[] subtract(int[] a, int[] b) {
        int[] out = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = a[i] - b[i];
        }
        return out;
    }

    private static int[] negate(int[] a) {
        int[] out = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = -a[i];
        }
        return out;
    }

    private static String dimensionKey(int[] dimension) {
        StringBuilder out = new StringBuilder();
        for (int exponent : dimension) {
            if (out.length() > 0) {
                out.append(',');
            }
            out.append(exponent);
        }
        return out.toString();
    }

    private static String requiredString(JsonObject object, String key, String where) {
        JsonElement element = object.get(key);
        if (element == null || !element.isJsonPrimitive()) {
            throw new IllegalArgumentException(where + ": missing string field \"" + key + "\"");
        }
        return element.getAsString();
    }

    private static List<String> stringList(JsonObject object, String key, String where) {
        JsonElement element = object.get(key);
        if (element == null) {
            return List.of();
        }
        List<String> out = new ArrayList<>();
        for (JsonElement item : element.getAsJsonArray()) {
            out.add(item.getAsString());
        }
        return out;
    }

    private static void deleteRecursively(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(path)) {
            for (Path entry : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(entry);
            }
        }
    }

    private static void write(Path directory, String fileName, String contents) throws IOException {
        try {
            Files.writeString(directory.resolve(fileName), contents, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * {@code "wpiUnits": {"measureType": "...", "unitName": "..."}} emits {@code ofWpi(measureType)}
     * doing {@code <constant>.of(quantity.in(Units.<unitName>))}. {@code constant} defaults to {@code
     * unitName} and must exist in {@code units}.
     */
    private record WpiUnits(String measureType, String unitName, String constant) {}

    private record QuantitySpec(
            String name,
            int[] dimension,
            String baseUnitSymbol,
            LinkedHashMap<String, Double> units,
            WpiUnits wpiUnits,
            List<String> imports,
            List<String> quantityCode,
            List<String> unitCode) {}

    private record Spec(String packageName, List<QuantitySpec> quantities) {}
}
