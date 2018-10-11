package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoTest", group = "ARC")
public class ServoTest extends OpMode {

  private Servo servo;
  private double servoPosition = 0;
  private double increment = 0.01;

  @Override
  public void init() {
    servo = hardwareMap.servo.get("servo0");
  }

  @Override
  public void loop() {

    servoPosition = (servoPosition + increment);
    if (Math.abs(servoPosition) > 1) {
      increment *= -1;
    }

    servo.setPosition(servoPosition);

    telemetry.addData("servoPosition", servoPosition);
    telemetry.update();
  }
}
