package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

@Autonomous(name = "Mecanum Autonomous Example", group = "ARC")
public class MecanumAutonomousExample extends LinearOpMode {

  // Change these values if necessary
  private static final int TICKS_PER_INCH = (int) (1120 / (4 * Math.PI)),
      ticksPer360 = 4000;

  @Override
  public void runOpMode() {

    DcMotor motorFL = hardwareMap.dcMotor.get("motorFL");
    DcMotor motorFR = hardwareMap.dcMotor.get("motorFR");
    DcMotor motorBL = hardwareMap.dcMotor.get("motorBL");
    DcMotor motorBR = hardwareMap.dcMotor.get("motorBR");

    motorFL.setDirection(Direction.REVERSE);
    motorBL.setDirection(Direction.REVERSE);

    MecanumDrive drivetrain = MecanumDrive.fromOctagonalMotors(
        motorFL, motorFR, motorBL, motorBR, this, TICKS_PER_INCH, ticksPer360);

    waitForStart();

    drivetrain.strafeInches(-5, 10, 1);
    drivetrain.strafeInches(5, 10, 1);
    drivetrain.strafeInches(5, -10, 1);
    drivetrain.strafeInches(-5, -10, 1);
  }
}
