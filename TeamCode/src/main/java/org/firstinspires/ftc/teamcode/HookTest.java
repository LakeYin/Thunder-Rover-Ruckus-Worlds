package org.firstinspires.ftc.teamcode;

public class HookTest extends TankDriveTester {

  private HookLift hookLift;

  @Override
  protected void runLoop() {
    while (opModeIsActive()) {
      if (Math.abs(gamepad1.left_stick_y) > 0.1) {
        hookLift.adjust(-gamepad1.left_stick_y);
      } else if (gamepad1.y) {
        hookLift.liftToHook();
      } else if (gamepad1.a) {
        hookLift.lowerToBottom();
      }
    }
  }

  @Override
  protected void setup() {
    hookLift = new HookLift(hardwareMap.dcMotor.get("hookLift"), this);
  }
}
