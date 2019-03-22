package org.firstinspires.ftc.teamcode.teleop.TestChassis;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.teleop.TeleOpMode;

public class LiftBot extends TeleOpMode {

  @Override
  public void runOpMode() {
    initialize();
    waitForStart();

    while (opModeIsActive()) {

      controlLift(gamepad1);

      telemetry.addData("Connection Keep-Alive", getRuntime());
      addPowerDrawDebug();
      telemetry.update();
    }

  }

  private void controlLift(Gamepad gamepad) {
    if(gamepad.a) {
      Bot.getInstance().hookLift.liftToHook();
    }
    else if(gamepad.b) {
      Bot.getInstance().hookLift.lowerToBottom();
    }
    else {
      Bot.getInstance().hookLift.adjust(gamepad.left_trigger - gamepad.right_trigger);
    }
  }
}
