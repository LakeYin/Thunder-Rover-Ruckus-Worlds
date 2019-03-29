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
  private static final String[] kMotorsToTest = {
      "hookLift"
  };

  @Override
  public void runOpMode() {

    DcMotor[] motors = Stream.of(kMotorsToTest).map(motorName ->
        hardwareMap.dcMotor.get(motorName)).toArray(DcMotor[]::new);

    telemetry.addData("Testing motors", Arrays.asList(kMotorsToTest));
    telemetry.update();

    waitForStart();

    while (opModeIsActive()) {
      for (DcMotor motor : motors) {
        motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(-gamepad1.left_stick_y);
      }

      telemetry.addData("Time passed", getRuntime());
      for (DcMotor motor : motors)
        telemetry.addData("motor positions", motor.getCurrentPosition());
      telemetry.update();
    }

    while (!isStopRequested() && opModeIsActive());
  }
}
