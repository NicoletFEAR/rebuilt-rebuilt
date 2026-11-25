package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.lib.CanId;
import frc.robot.subsystems.drivebase.SwerveModuleName;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeviceIdsTest {
    private static final List<SwerveModuleName> MODULES = List.of(
            SwerveModuleName.FRONT_LEFT,
            SwerveModuleName.FRONT_RIGHT,
            SwerveModuleName.BACK_LEFT,
            SwerveModuleName.BACK_RIGHT);

    @Test
    void everySwerveModuleHasDriveTurnAndEncoderIds() {
        for (SwerveModuleName module : MODULES) {
            assertTrue(DeviceIds.DRIVE.containsKey(module));
            assertTrue(DeviceIds.TURN.containsKey(module));
            assertTrue(DeviceIds.SWERVE_ABS_ENCODER.containsKey(module));
        }

        assertEquals(MODULES.size(), DeviceIds.DRIVE.size());
        assertEquals(MODULES.size(), DeviceIds.TURN.size());
        assertEquals(MODULES.size(), DeviceIds.SWERVE_ABS_ENCODER.size());
    }

    @Test
    void swerveIdsMatchTheExpectedValues() {
        assertEquals(1, DeviceIds.TURN.get(SwerveModuleName.FRONT_LEFT).id());
        assertEquals(4, DeviceIds.TURN.get(SwerveModuleName.FRONT_RIGHT).id());
        assertEquals(10, DeviceIds.TURN.get(SwerveModuleName.BACK_LEFT).id());
        assertEquals(7, DeviceIds.TURN.get(SwerveModuleName.BACK_RIGHT).id());

        assertEquals(2, DeviceIds.DRIVE.get(SwerveModuleName.FRONT_LEFT).id());
        assertEquals(5, DeviceIds.DRIVE.get(SwerveModuleName.FRONT_RIGHT).id());
        assertEquals(11, DeviceIds.DRIVE.get(SwerveModuleName.BACK_LEFT).id());
        assertEquals(8, DeviceIds.DRIVE.get(SwerveModuleName.BACK_RIGHT).id());

        assertEquals(3, DeviceIds.SWERVE_ABS_ENCODER.get(SwerveModuleName.FRONT_LEFT).id());
        assertEquals(6, DeviceIds.SWERVE_ABS_ENCODER.get(SwerveModuleName.FRONT_RIGHT).id());
        assertEquals(12, DeviceIds.SWERVE_ABS_ENCODER.get(SwerveModuleName.BACK_LEFT).id());
        assertEquals(9, DeviceIds.SWERVE_ABS_ENCODER.get(SwerveModuleName.BACK_RIGHT).id());
    }

    @Test
    void everyCanIdIsUnique() {
        Set<Integer> ids = new HashSet<>();
        List<CanId> allIds = new java.util.ArrayList<>();

        allIds.addAll(DeviceIds.DRIVE.values());
        allIds.addAll(DeviceIds.TURN.values());
        allIds.addAll(DeviceIds.SWERVE_ABS_ENCODER.values());
        allIds.add(DeviceIds.LEFT_LAUNCHER);
        allIds.add(DeviceIds.RIGHT_LAUNCHER);
        allIds.add(DeviceIds.INDEXER);
        allIds.add(DeviceIds.HOOD);
        allIds.add(DeviceIds.HOOD_ENCODER);
        allIds.add(DeviceIds.INTAKE_WHEELS);
        allIds.add(DeviceIds.INTAKE_ARM);

        for (CanId id : allIds) {
            assertTrue(ids.add(id.id()), "Duplicate CAN ID: " + id.id());
        }
    }

    @Test
    void swerveDevicesUseTheDrivebaseBus() {
        for (SwerveModuleName module : MODULES) {
            assertSame(DeviceIds.DRIVEBASE_BUS, DeviceIds.DRIVE.get(module).bus());
            assertSame(DeviceIds.DRIVEBASE_BUS, DeviceIds.TURN.get(module).bus());
            assertSame(DeviceIds.DRIVEBASE_BUS, DeviceIds.SWERVE_ABS_ENCODER.get(module).bus());
        }
    }

    @Test
    void launcherAndIntakeDevicesUseTheirOwnBuses() {
        assertSame(DeviceIds.LAUNCHER_BUS, DeviceIds.LEFT_LAUNCHER.bus());
        assertSame(DeviceIds.LAUNCHER_BUS, DeviceIds.RIGHT_LAUNCHER.bus());
        assertSame(DeviceIds.LAUNCHER_BUS, DeviceIds.INDEXER.bus());
        assertSame(DeviceIds.LAUNCHER_BUS, DeviceIds.HOOD.bus());
        assertSame(DeviceIds.LAUNCHER_BUS, DeviceIds.HOOD_ENCODER.bus());

        assertSame(DeviceIds.INTAKE_BUS, DeviceIds.INTAKE_WHEELS.bus());
        assertSame(DeviceIds.INTAKE_BUS, DeviceIds.INTAKE_ARM.bus());
    }

    @Test
    void launcherAndIntakeIdsMatchTheExpectedValues() {
        assertEquals(14, DeviceIds.LEFT_LAUNCHER.id());
        assertEquals(15, DeviceIds.RIGHT_LAUNCHER.id());
        assertEquals(16, DeviceIds.INDEXER.id());
        assertEquals(23, DeviceIds.HOOD.id());
        assertEquals(22, DeviceIds.HOOD_ENCODER.id());
        assertEquals(18, DeviceIds.INTAKE_WHEELS.id());
        assertEquals(19, DeviceIds.INTAKE_ARM.id());
    }
}
