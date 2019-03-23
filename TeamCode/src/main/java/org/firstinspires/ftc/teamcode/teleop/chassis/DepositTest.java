package org.firstinspires.ftc.teamcode.teleop.chassis;

import org.firstinspires.ftc.teamcode.DepositSystem;

public class DepositTest extends TankDriveTester {

  private DepositSystem deposit;

  @Override
  protected void runLoop() {
    while (opModeIsActive()) {
      drivetrain.setMovementAndRotation(-gamepad1.left_stick_y, gamepad1.left_stick_x);

      if (gamepad1.b) {
        deposit.retract();
      }
      if (gamepad1.x || gamepad1.a) {
        deposit.prepareToDeposit();
      }
      if (gamepad1.y) {
        deposit.deposit();
      }
    }
  }

  protected void setup() {
    deposit = new DepositSystem(hardwareMap.dcMotor.get("depositSlide"),
        hardwareMap.servo.get("depositOrientator"), this);
  }
}
