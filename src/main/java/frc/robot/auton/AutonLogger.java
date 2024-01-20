package frc.robot.auton;

import com.pathplanner.lib.util.PathPlannerLogging;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonLogger {
    // TODO: Get Logging working on AdvantageScope

    public class RobotContainer {
        private final Field2d field;

        public RobotContainer() {
            field = new Field2d();
            SmartDashboard.putData("Field", field);

            // Logging callback for current robot pose
            PathPlannerLogging.setLogCurrentPoseCallback(
                    (pose) -> {
                        // Do whatever you want with the pose here
                        field.setRobotPose(pose);
                    });

            // Logging callback for target robot pose
            PathPlannerLogging.setLogTargetPoseCallback(
                    (pose) -> {
                        // Do whatever you want with the pose here
                        field.getObject("target pose").setPose(pose);
                    });

            // Logging callback for the active path, this is sent as a list of poses
            PathPlannerLogging.setLogActivePathCallback(
                    (poses) -> {
                        // Do whatever you want with the poses here
                        field.getObject("path").setPoses(poses);
                    });
        }
    }
}
