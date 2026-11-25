package frc.lib.unitsgen;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GenerateUnitsTest {
    @TempDir
    Path tempDir;

    private Path writeSpec(String json) throws IOException {
        Path spec = tempDir.resolve("units.json");
        Files.writeString(spec, json);
        return spec;
    }

    private Path outputDir() {
        return tempDir.resolve("out");
    }

    private void generate(Path spec) throws IOException {
        GenerateUnits.main(new String[] {spec.toString(), outputDir().toString()});
    }

    private String read(String relativePath) throws IOException {
        return Files.readString(outputDir().resolve(relativePath));
    }

    private static String validSpec() {
        return """
                {
                  "package": "test.units",
                  "quantities": [
                    {
                      "name": "Length",
                      "dimension": [1, 0, 0, 0, 0, 0, 0, 0],
                      "baseUnitSymbol": "m",
                      "units": {
                        "Meters": 1,
                        "Centimeters": 0.01
                      },
                      "wpiUnits": {
                        "measureType": "Distance",
                        "unitName": "Meters"
                      },
                      "imports": ["java.util.Optional"],
                      "quantityCode": [
                        "public double doubled() { return value * 2.0; }",
                        "public Optional<String> optional() { return Optional.empty(); }"
                      ],
                      "unitCode": [
                        "public static Length zero() { return Meters.of(0.0); }"
                      ]
                    },
                    {
                      "name": "Duration",
                      "dimension": [0, 0, 1, 0, 0, 0, 0, 0],
                      "baseUnitSymbol": "s",
                      "units": {
                        "Seconds": 1
                      }
                    },
                    {
                      "name": "Speed",
                      "dimension": [1, 0, -1, 0, 0, 0, 0, 0],
                      "baseUnitSymbol": "m/s",
                      "units": {
                        "MetersPerSecond": 1
                      }
                    }
                  ]
                }
                """;
    }

    @Test
    void generatesQuantityAndUnitFiles() throws IOException {
        generate(writeSpec(validSpec()));

        for (String name : new String[] {"Length", "Duration", "Speed"}) {
            assertTrue(Files.exists(outputDir().resolve("test/units/" + name + ".java")));
            assertTrue(Files.exists(outputDir().resolve("test/units/" + name + "Unit.java")));
        }
    }

    @Test
    void generatedQuantityContainsArithmeticAndCustomCode() throws IOException {
        generate(writeSpec(validSpec()));

        String length = read("test/units/Length.java");

        assertTrue(length.contains("package test.units;"));
        assertTrue(length.contains("public class Length extends Quantity<Length, LengthUnit>"));
        assertTrue(length.contains("public double doubled()"));
        assertTrue(length.contains("public Optional<String> optional()"));
        assertTrue(length.contains("public Length plus(Length other)"));
        assertTrue(length.contains("public Length times(double other)"));
        assertTrue(length.contains("public Speed div(Duration other)"));
    }

    @Test
    void generatedUnitContainsConstantsAndFactories() throws IOException {
        generate(writeSpec(validSpec()));

        String lengthUnit = read("test/units/LengthUnit.java");

        assertTrue(lengthUnit.contains("public class LengthUnit extends Unit<LengthUnit>"));
        assertTrue(lengthUnit.contains("public static final LengthUnit Meters = new LengthUnit(1.0);"));
        assertTrue(lengthUnit.contains("public static final LengthUnit Centimeters = new LengthUnit(0.01);"));
        assertTrue(lengthUnit.contains("public static Length zero()"));
        assertTrue(lengthUnit.contains("public static Length ofWpi(org.wpilib.units.measure.Distance quantity)"));
        assertTrue(lengthUnit.contains("org.wpilib.units.Units.Meters"));
        assertTrue(lengthUnit.contains("import java.util.Optional;"));
    }

    @Test
    void generatesCrossQuantityMethodsOnBothOperands() throws IOException {
        generate(writeSpec(validSpec()));

        assertTrue(read("test/units/Length.java").contains("public Speed div(Duration other)"));
        assertTrue(read("test/units/Duration.java").contains("public Length times(Speed other)"));
    }

    @Test
    void deletesStaleFilesInTheGeneratedPackage() throws IOException {
        Path stale = outputDir().resolve("test/units/Stale.java");
        Path staleText = outputDir().resolve("test/units/stale.txt");
        Files.createDirectories(stale.getParent());
        Files.writeString(stale, "stale");
        Files.writeString(staleText, "stale");

        generate(writeSpec(validSpec()));

        assertFalse(Files.exists(stale));
        assertFalse(Files.exists(staleText));
    }

    @Test
    void createsTheOutputDirectoryWhenItDoesNotExist() throws IOException {
        generate(writeSpec(validSpec()));

        assertTrue(Files.isDirectory(outputDir().resolve("test/units")));
    }

    @Test
    void rejectsDuplicateDimensions() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "First", "dimension": [1,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 1}},
                    {"name": "Second", "dimension": [1,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("share the same dimension"));
    }

    @Test
    void rejectsDuplicateNames() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Same", "dimension": [1,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 1}},
                    {"name": "Same", "dimension": [0,0,1,0,0,0,0,0], "baseUnitSymbol": "s", "units": {"Seconds": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("duplicate quantity name"));
    }

    @Test
    void rejectsNonPositiveUnitFactors() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Length", "dimension": [1,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 0}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("must have a finite, positive factor"));
    }

    @Test
    void rejectsMissingWpiConstant() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {
                      "name": "Length",
                      "dimension": [1,0,0,0,0,0,0,0],
                      "baseUnitSymbol": "m",
                      "units": {"Meters": 1},
                      "wpiUnits": {"measureType": "Distance", "unitName": "Meters", "constant": "Feet"}
                    }
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("is not a unit defined"));
    }

    @Test
    void rejectsWrongDimensionLength() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Length", "dimension": [1,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("must have exactly 8 entries"));
    }

    @Test
    void rejectsMissingDimension() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Length", "baseUnitSymbol": "m", "units": {"Meters": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("missing \"dimension\""));
    }

    @Test
    void rejectsMissingBaseUnitSymbol() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Length", "dimension": [1,0,0,0,0,0,0,0], "units": {"Meters": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("missing string field \"baseUnitSymbol\""));
    }

    @Test
    void rejectsInvalidPackageName() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "bad-package",
                  "quantities": [
                    {"name": "Length", "dimension": [1,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("invalid package name"));
    }

    @Test
    void rejectsInvalidQuantityName() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Bad-Name", "dimension": [1,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("invalid quantity name"));
    }

    @Test
    void rejectsDimensionExponentOutOfRange() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Length", "dimension": [9,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("dimension exponent out of range"));
    }

    @Test
    void rejectsInvalidUnitName() throws IOException {
        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Length", "dimension": [1,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Bad-Unit": 1}}
                  ]
                }
                """);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> generate(spec));

        assertTrue(exception.getMessage().contains("invalid unit name"));
    }

    @Test
    void doesNotLeaveOldFilesWhenGenerationFailsValidation() throws IOException {
        Path stale = outputDir().resolve("test/units/Stale.java");
        Files.createDirectories(stale.getParent());
        Files.writeString(stale, "stale");

        Path spec = writeSpec("""
                {
                  "package": "test.units",
                  "quantities": [
                    {"name": "Length", "dimension": [1,0,0,0,0,0,0,0], "baseUnitSymbol": "m", "units": {"Meters": 0}}
                  ]
                }
                """);

        assertThrows(IllegalArgumentException.class, () -> generate(spec));
        assertTrue(Files.exists(stale), "Validation happens before the output directory is deleted");
    }

    @Test
    void generatedFilesHaveTheExpectedHeader() throws IOException {
        generate(writeSpec(validSpec()));

        String expectedHeader = "// Generated by frc.lib.unitsgen.GenerateUnits from units.json. Do not edit.\n"
                + "// Make changes to src/main/resources/units.json instead.\n";

        assertTrue(read("test/units/Length.java").startsWith(expectedHeader));
        assertTrue(read("test/units/LengthUnit.java").startsWith(expectedHeader));
    }
}
