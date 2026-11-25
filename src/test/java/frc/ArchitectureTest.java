import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "frc", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {
    @ArchTest
    static final ArchRule libShouldNotDependOnRobot = ArchRuleDefinition.noClasses()
            .that()
            .resideInAPackage("frc.lib..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("frc.robot..");
}
