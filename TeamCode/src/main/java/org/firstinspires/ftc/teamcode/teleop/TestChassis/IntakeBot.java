package org.firstinspires.ftc.teamcode.teleop.TestChassis;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.teleop.TeleOpMode;

public class IntakeBot extends TeleOpMode {

  private boolean intakeIsExtended = false;

  @Override
  public void runOpMode() {
    initialize();
    waitForStart();

    while (opModeIsActive()) {

      controlIntake(gamepad1);

      telemetry.addData("Connection Keep-Alive", getRuntime());
      addPowerDrawDebug();
      telemetry.update();
    }

  }

  private void controlIntake(Gamepad gamepad) {
    Bot.getInstance().intake.controlSlidesManually((gamepad.dpad_left?1:0) - (gamepad.dpad_right?1:0));
    if(gamepad.a) {
      if(intakeIsExtended) {
        Bot.getInstance().intake.orientToTransit();
        Bot.getInstance().intake.retractFully();
      }
      else {
        Bot.getInstance().intake.extendFully();
      }
      intakeIsExtended = !intakeIsExtended;
    }
    if(gamepad.b) {
      Bot.getInstance().intake.orientToCollect();
    }
    else if(gamepad.x) {
      Bot.getInstance().intake.orientToTransfer();
    }
    else if(gamepad.y) {
      Bot.getInstance().intake.orientToTransit();
    }
  }
}
