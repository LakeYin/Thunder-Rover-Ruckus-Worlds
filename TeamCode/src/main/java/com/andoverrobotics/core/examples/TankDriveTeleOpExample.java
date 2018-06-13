package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TankDrive TeleOp Example")
public class TankDriveTeleOpExample extends OpMode {

  private DcMotor motorL, motorR;
  private int ticksPerInch, ticksPer360;
  private TankDrive tankDrive;

  @Override
  public void init() {
    motorL = hardwareMap.dcMotor.get("motorL");
    motorR = hardwareMap.dcMotor.get("motorR");
    tankDrive = TankDrive.fromMotors(motorL, motorR, this, ticksPerInch, ticksPer360);
  }

  @Override
  public void loop() {
    tankDrive.setMovementAndRotation(gamepad1.left_stick_y, gamepad1.left_stick_x);
  }
}
