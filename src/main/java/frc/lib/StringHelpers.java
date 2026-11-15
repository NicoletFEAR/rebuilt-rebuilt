package frc.lib;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Utility methods for formatting strings. */
public final class StringHelpers {
    /**
     * Inserts spaces into a PascalCase-like string at letter/non-letter boundaries and before a
     * capitalized word that follows an uppercase letter. It is used to turn record class names into
     * human-readable words for logging.
     *
     * @param name the PascalCase-like string to split
     * @return the string with spaces inserted at the boundaries described above, or the input
     *     unchanged if it is empty
     */
    public static String pascalCaseToWords(String name) {
        if (name.isEmpty()) {
            return name;
        }

        // Separates acronyms from captialized words.
        String result = name.replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2");
        // Separates lowercase letters or digits from uppercase letters.
        result = result.replaceAll("([a-z0-9])([A-Z])", "$1 $2");
        // Separates letters from numbers.
        result = result.replaceAll("([a-zA-Z])([0-9])", "$1 $2");
        // Separates numbers from letters.
        result = result.replaceAll("([0-9])([a-zA-Z])", "$1 $2");
        return result;
    }

    /**
     * Splits a SCREAMING_SNAKE_CASE string into words separated by spaces.
     *
     * <p>Each underscore-separated word is converted to title case (first letter uppercase, remaining
     * letters lowercase).
     *
     * @param name the SCREAMING_SNAKE_CASE string to split
     * @return the input string with underscores replaced by spaces and each word converted to title
     *     case
     */
    public static String screamingSnakeCaseToWords(String name) {
        // Splits name by underscores.
        return Arrays.stream(name.split("_"))
                // Capitalizes first letter of each word and uncapitalizes the rest.
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                // Puts word back together with spaces between.
                .collect(Collectors.joining(" "));
    }

    private StringHelpers() {
        // Keep this constructor empty.
    }
}
