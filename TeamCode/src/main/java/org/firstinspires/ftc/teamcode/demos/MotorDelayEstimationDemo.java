package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name = "Motor Delay Estimation Demo", group = "ARC")
public class MotorDelayEstimationDemo extends LinearOpMode {
    private static final String kTargetMotorName = "hookLift";

    private DcMotor motor;
    private double targetSpeed = 0.4;

    @Override
    public void runOpMode() {
        motor = hardwareMap.dcMotor.get(kTargetMotorName);

        waitForStart();

        while (!gamepad1.start) {
            adjustSpeedUsingTriggers();
        }

        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(targetSpeed);
        double startTime = System.currentTimeMillis();

        while (!gamepad1.x) {
            telemetry.addData("Time Elapsed", System.currentTimeMillis() - startTime);
            telemetry.update();
        }

        motor.setPower(0);
        telemetry.addData("Total time", System.currentTimeMillis() - startTime);
        telemetry.update();

        while (!isStopRequested() && opModeIsActive());
    }

    private void adjustSpeedUsingTriggers() {
        targetSpeed = Range.clip(targetSpeed + (gamepad1.left_trigger - gamepad1.right_trigger) * 0.01, -1, 1);
        telemetry.addData("Speed", targetSpeed);
        telemetry.update();
    }
}
