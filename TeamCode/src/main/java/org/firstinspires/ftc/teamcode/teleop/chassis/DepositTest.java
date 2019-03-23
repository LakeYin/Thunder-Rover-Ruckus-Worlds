package org.firstinspires.ftc.teamcode.teleop.chassis;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.DepositSystem;

public class DepositTest extends TankDriveTester {

  private DepositSystem deposit;

  @Override
  protected void runLoop() {
    while (opModeIsActive()) {
      controlDeposit(gamepad1);
    }
  }

  private void controlDeposit(Gamepad gamepad) {

    drivetrain.setMovementAndRotation(-gamepad.left_stick_y, gamepad.left_stick_x);

    if (gamepad.b) {
      deposit.retract();
    }
    if (gamepad.x || gamepad.a) {
      deposit.prepareToDeposit();
    }
    if (gamepad.y) {
      deposit.deposit();
    }
  }

  protected void setup() {
    deposit = new DepositSystem(hardwareMap.dcMotor.get("depositSlide"),
        hardwareMap.servo.get("depositOrientator"), this);
  }
}
