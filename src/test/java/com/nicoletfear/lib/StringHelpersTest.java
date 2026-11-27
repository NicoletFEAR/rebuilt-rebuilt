package com.nicoletfear.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringHelpersTest {
    private Locale originalLocale;

    @BeforeEach
    void useStableLocale() {
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterEach
    void restoreLocale() {
        Locale.setDefault(originalLocale);
    }

    @Test
    void camelCaseToWordsCapitalizesAndSplits() {
        assertEquals("Drive Motor", StringHelpers.camelCaseToWords("driveMotor"));
        assertEquals("Already Pascal", StringHelpers.camelCaseToWords("alreadyPascal"));
        assertEquals("Url 2 Path", StringHelpers.camelCaseToWords("url2Path"));
        assertEquals("X", StringHelpers.camelCaseToWords("x"));
    }

    @Test
    void camelCaseToWordsReturnsEmptyStringForEmptyInput() {
        assertEquals("", StringHelpers.camelCaseToWords(""));
    }

    @Test
    void pascalCaseToWordsSplitsAcronymsAndWords() {
        assertEquals("HTTP Server", StringHelpers.pascalCaseToWords("HTTPServer"));
        assertEquals("CAN Range 2", StringHelpers.pascalCaseToWords("CANRange2"));
        assertEquals("Already Pascal", StringHelpers.pascalCaseToWords("AlreadyPascal"));
        assertEquals("X 1 Y 2", StringHelpers.pascalCaseToWords("X1Y2"));
        assertEquals("ABC", StringHelpers.pascalCaseToWords("ABC"));
        assertEquals("A", StringHelpers.pascalCaseToWords("A"));
    }

    @Test
    void pascalCaseToWordsReturnsEmptyStringForEmptyInput() {
        assertEquals("", StringHelpers.pascalCaseToWords(""));
    }

    @Test
    void screamingSnakeCaseToWordsTitleCasesEachWord() {
        assertEquals("Front Left", StringHelpers.screamingSnakeCaseToWords("FRONT_LEFT"));
        assertEquals("Back Right", StringHelpers.screamingSnakeCaseToWords("BACK_RIGHT"));
        assertEquals("Multi Word Name", StringHelpers.screamingSnakeCaseToWords("MULTI_WORD_NAME"));
        assertEquals("Hello World", StringHelpers.screamingSnakeCaseToWords("hello_world"));
        assertEquals("Front", StringHelpers.screamingSnakeCaseToWords("FRONT"));
    }

    @Test
    void screamingSnakeCaseToWordsReturnsEmptyStringForEmptyInput() {
        assertEquals("", StringHelpers.screamingSnakeCaseToWords(""));
    }
}
