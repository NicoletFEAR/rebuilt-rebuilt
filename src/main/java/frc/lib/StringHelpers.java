package frc.lib;

import java.util.Arrays;
import java.util.stream.Collectors;

// I don't like having a Helpers class but I don't
// know where else to put this method
public final class StringHelpers {
    public static String pascalCaseToWords(String name) {
        if (name.isEmpty()) {
            return name;
        }

        return name.replaceAll("(?<=[A-Z])(?=[A-Z][a-z])|(?=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])", " ");
    }

    public static String screamingSnakeCaseToWords(String name) {
        return Arrays.stream(name.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).collect(Collectors.joining(" "));
    }
    private StringHelpers() {}
}
