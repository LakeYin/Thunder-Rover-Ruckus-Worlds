package org.firstinspires.ftc.teamcode.teleop.chassis;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.HookLift;

@TeleOp(name = "HookTest", group = "ARC")
public class HookTest extends TankDriveTester {

  private HookLift hookLift;

  @Override
  protected void runLoop() throws InterruptedException {
    while (opModeIsActive()) {
      controlHookLift(gamepad1);
    }
  }

  private void controlHookLift(Gamepad gamepad) throws InterruptedException {
    if (Math.abs(gamepad.left_stick_y) > 0.1) {
      hookLift.adjust(-gamepad.left_stick_y);
    } else if (gamepad.y) {
      hookLift.liftToHook();
    } else if (gamepad.a) {
      hookLift.lowerToBottom();
    }
  }

  @Override
  protected void setup() {
    hookLift = new HookLift(hardwareMap.dcMotor.get("hookLift"), this);
  }
}
