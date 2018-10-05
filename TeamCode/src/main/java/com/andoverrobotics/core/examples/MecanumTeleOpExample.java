package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

@TeleOp(name = "Mecanum TeleOp Example", group = "ARC")
public class MecanumTeleOpExample extends OpMode {

  private static final int TICKS_PER_INCH = 20, TICKS_PER_360 = 200;

  private MecanumDrive mecanumDrive;

  @Override
  public void init() {
    DcMotor motorFL = hardwareMap.dcMotor.get("motorFL");
    DcMotor motorFR = hardwareMap.dcMotor.get("motorFR");
    DcMotor motorBL = hardwareMap.dcMotor.get("motorBL");
    DcMotor motorBR = hardwareMap.dcMotor.get("motorBR");

    motorFL.setDirection(Direction.REVERSE);
    motorBL.setDirection(Direction.REVERSE);

    mecanumDrive = MecanumDrive.fromOctagonalMotors(
        motorFL, motorFR, motorBL, motorBR, this, TICKS_PER_INCH, TICKS_PER_360);
  }

  @Override
  public void loop() {

    if (Math.abs(gamepad1.right_stick_x) > 0.1) {
      mecanumDrive.setRotationPower(gamepad1.right_stick_x);
    } else {
      mecanumDrive.setStrafe(gamepad1.left_stick_x, -gamepad1.left_stick_y, 1);
    }

    telemetry.addData("Left stick X", gamepad1.left_stick_x);
    telemetry.addData("Left stick Y", -gamepad1.left_stick_y);
    telemetry.addData("Right stick X", gamepad1.right_stick_x);
    telemetry.update();
  }
}
