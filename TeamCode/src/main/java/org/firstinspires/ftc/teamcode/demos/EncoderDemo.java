package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.util.stream.Stream;

@Autonomous(name = "EncoderDemo", group = "ARC Lightning")
public class EncoderDemo extends LinearOpMode {

  // Change these values to your liking
  private static final double kPower = 0.5;
  private static final int kRuntime = 5;
  private static final String[] kMotorsToTest = {
      "motorFL", "motorFR", "motorBL", "motorBR"
  };

  @Override
  public void runOpMode() {

    DcMotor[] motors = Stream.of(kMotorsToTest).map(motorName ->
        hardwareMap.dcMotor.get(motorName)).toArray(DcMotor[]::new);

    waitForStart();

    for (DcMotor motor : motors) {

      motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
      motor.setMode(RunMode.RUN_USING_ENCODER);

      motor.setPower(kPower);

      double runtime = getRuntime();
      while (opModeIsActive() && getRuntime() - runtime < kRuntime) {
        telemetry.addData("Position", motor.getCurrentPosition());
        telemetry.update();
      }
      motor.setPower(0);
    }
  }
}
