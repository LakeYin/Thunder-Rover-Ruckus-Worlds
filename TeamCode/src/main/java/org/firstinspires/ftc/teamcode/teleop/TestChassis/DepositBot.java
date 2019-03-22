package org.firstinspires.ftc.teamcode.teleop.TestChassis;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.teleop.TeleOpMode;

public class DepositBot extends TeleOpMode {

  @Override
  public void runOpMode() {
    initialize();
    waitForStart();

    while (opModeIsActive()) {

      controlDeposit(gamepad1);

      telemetry.addData("Connection Keep-Alive", getRuntime());
      addPowerDrawDebug();
      telemetry.update();
    }

  }

  private void controlDeposit(Gamepad gamepad) {
    if(gamepad.a) {
      Bot.getInstance().deposit.deposit();
    }
    else if(gamepad.b) {
      Bot.getInstance().deposit.retract();
    }
    else if(gamepad.x) {
      Bot.getInstance().deposit.prepareToDeposit();
    }
  }
}
