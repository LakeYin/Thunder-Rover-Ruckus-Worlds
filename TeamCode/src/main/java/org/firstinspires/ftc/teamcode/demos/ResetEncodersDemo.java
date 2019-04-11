package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

@Autonomous(name = "Reset Encoders", group = "ARC")
public class ResetEncodersDemo extends LinearOpMode {

  @Override
  public void runOpMode() {
    waitForStart();
    for (DcMotor motor : hardwareMap.getAll(DcMotor.class)) {
      motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
      while (motor.getCurrentPosition() != 0 && opModeIsActive()) {
      }
      motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
      telemetry.addData(motor.getDeviceName(), motor.getCurrentPosition());
    }
    telemetry.update();
    while (opModeIsActive());
  }
}
