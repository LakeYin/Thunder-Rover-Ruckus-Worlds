package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;

@TeleOp(name = "Main TeleOp", group = "ARC Lightning")
public class TeleOpMode extends OpMode {

  private TeleOpBot bot;
  private ControlMapper controlMapper;

  @Override
  public void init() {
    try {

      bot = TeleOpBot.fromOpMode(this);
      controlMapper = new ControlMapper();

    } catch (IOException e) {
      stop();
      e.printStackTrace();
    }
  }

  @Override
  public void loop() {
    controlMapper.applyGamepadInputs(gamepad1, gamepad2);
    telemetry.addData("Connection Keep-Alive", getRuntime());
  }
}
