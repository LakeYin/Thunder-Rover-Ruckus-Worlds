package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.util.stream.Stream;

@Autonomous(name = "EncoderDemo", group = "ARC Lightning")
public class EncoderDemo extends LinearOpMode {
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

      motor.setTargetPosition(1440 * 2);
      motor.setMode(RunMode.RUN_TO_POSITION);
      motor.setPower(1);

      while (motor.isBusy()) {
        telemetry.addData("Position: ", motor.getCurrentPosition());
        telemetry.update();
      }
      motor.setPower(0);
    }
  }
}
