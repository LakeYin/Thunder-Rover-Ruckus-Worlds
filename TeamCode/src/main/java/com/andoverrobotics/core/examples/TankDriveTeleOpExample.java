package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

@TeleOp(name = "TankDrive TeleOp Example", group = "ARC")
public class TankDriveTeleOpExample extends OpMode {

  private static final int TICKS_PER_INCH = 1000, TICKS_PER_360 = 1440 * 4;
  private TankDrive tankDrive;

  @Override
  public void init() {
    DcMotor motorL = hardwareMap.dcMotor.get("motorL");
    DcMotor motorR = hardwareMap.dcMotor.get("motorR");
    motorL.setDirection(Direction.REVERSE);

    tankDrive = TankDrive.fromMotors(
        motorL, motorR, this, TICKS_PER_INCH, TICKS_PER_360);
  }

  @Override
  public void loop() {
    // The Y axis for gamepad sticks is reversed, negate it in use
    tankDrive.setMovementAndRotation(-gamepad1.left_stick_y, gamepad1.left_stick_x);

    telemetry.addData("Left stick X", gamepad1.left_stick_x);
    telemetry.addData("Left stick Y", -gamepad1.left_stick_y);
    telemetry.update();
  }
}
