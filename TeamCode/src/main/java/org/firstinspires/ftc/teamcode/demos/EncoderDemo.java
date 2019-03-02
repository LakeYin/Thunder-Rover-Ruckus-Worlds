package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import java.util.Arrays;
import java.util.stream.Stream;

@Autonomous(name = "EncoderDemo", group = "ARC Lightning")
public class EncoderDemo extends LinearOpMode {

  // Change these values to your liking
  private static final double kPower = 0;
  private static final String[] kMotorsToTest = {
      "leftLift"
  };

  @Override
  public void runOpMode() {

    DcMotor[] motors = Stream.of(kMotorsToTest).map(motorName ->
        hardwareMap.dcMotor.get(motorName)).toArray(DcMotor[]::new);

    telemetry.addData("Testing motors", Arrays.asList(kMotorsToTest));
    telemetry.addData("Power/speed", kPower);
    telemetry.update();

    waitForStart();

    for (DcMotor motor : motors) {
      while (opModeIsActive() && gamepad1.y);
      motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
      motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
      while (opModeIsActive() && motor.getCurrentPosition() != 0);
      motor.setMode(RunMode.RUN_USING_ENCODER);

      motor.setPower(kPower);

      while (opModeIsActive() && !isStopRequested() && !gamepad1.y) {
        telemetry.addData("Position", motor.getCurrentPosition());
        telemetry.addData("Press Y", "to stop");
        telemetry.update();
      }
      motor.setPower(0);
    }

    telemetry.addData("Time passed", getRuntime());
    for (DcMotor motor : motors)
      telemetry.addData("motor positions", motor.getCurrentPosition());
    telemetry.update();

    while (!isStopRequested() && opModeIsActive());
  }
}
