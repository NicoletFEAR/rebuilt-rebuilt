package frc.lib;

import java.util.Arrays;
import java.util.stream.Collectors;

// I don't like having a Helpers class but I don't
// know where else to put this method

/** Utility methods for formatting strings. */
public final class StringHelpers {
    private StringHelpers() {}

    /**
     * Inserts spaces into a PascalCase-like string at letter/non-letter boundaries and before a
     * capitalized word that follows an uppercase letter.
     *
     * <p>Note that this does not insert a space at every lower-to-uppercase boundary; for example,
     * {@code "FrontLeft"} is returned unchanged. It does handle boundaries like {@code "URLPath"}
     * (becoming {@code "URL Path"}) and letter/digit boundaries like {@code "Turbo2000X"} (becoming
     * {@code "Turbo 2000X"}). It is used to turn record class names into human-readable words for
     * logging.
     *
     * @param name the PascalCase-like string to split
     * @return the string with spaces inserted at the boundaries described above, or the input
     *     unchanged if it is empty
     */
    public static String pascalCaseToWords(String name) {
        if (name.isEmpty()) {
            return name;
        }

        return name.replaceAll(
                "(?<=[A-Z])(?=[A-Z][a-z])|(?=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])", " ");
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
        return Arrays.stream(name.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
