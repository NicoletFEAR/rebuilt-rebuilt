package com.nicoletfear;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.nicoletfear", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {
    @ArchTest
    static final ArchRule libShouldNotDependOnRobot = noClasses()
            .that()
            .resideInAPackage("com.nicoletfear.lib..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("com.nicoletfear.robot..");
}
