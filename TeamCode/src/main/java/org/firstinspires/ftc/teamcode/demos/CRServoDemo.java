package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "CRServo Demo", group = "ARC Lightning")
public class CRServoDemo extends OpMode {

  private CRServo servo;

  @Override
  public void init() {
    servo = hardwareMap.crservo.get("intakeSweeper");
  }

  @Override
  public void loop() {
    servo.setPower(-0.85);
    telemetry.addData("Runtime", getRuntime());
    telemetry.addData("getPower", servo.getPower());
  }
}
