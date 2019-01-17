package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.ArrayList;


@TeleOp(name = "ServoDemo", group = "ARC")
public class ServoDemo extends OpMode {

  private ArrayList<Servo> servos;

  @Override
  public void init() {

    servos = new ArrayList<>();

    for (int i = 0; hardwareMap.servo.contains("servo" + i); i++)
      servos.add(hardwareMap.servo.get("servo" + i));

    telemetry.addData("Loaded servo count", servos.size());
    telemetry.update();
  }

  @Override
  public void loop() {
    double position = servos.get(0).getPosition() + (gamepad1.left_trigger * 0.005) - (gamepad1.right_trigger * 0.005);
    for (Servo servo : servos) {
      servo.setPosition(position);
      telemetry.addData("Position", position);
    }
  }

}