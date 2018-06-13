package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Example Mecanum TeleOp")
public class MecanumTeleOpExample extends OpMode {

  private DcMotor motorFL, motorFR, motorBL, motorBR;
  private MecanumDrive mecanumDrive;
  private int ticksPerInch, ticksPer360;

  @Override
  public void init() {
    motorFL = hardwareMap.dcMotor.get("motorFL");
    motorFR = hardwareMap.dcMotor.get("motorFR");
    motorBL = hardwareMap.dcMotor.get("motorBL");
    motorBR = hardwareMap.dcMotor.get("motorBR");

    mecanumDrive = MecanumDrive.fromOctagonalMotors(motorFL, motorFR, motorBL, motorBR, this, ticksPerInch, ticksPer360);
  }

  @Override
  public void loop() {
    mecanumDrive.setStrafe(gamepad1.left_stick_x, -gamepad1.left_stick_y);
  }
}
