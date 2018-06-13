package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Mecanum Autonomous Example", group = "ARC")
public class MecanumAutonomousExample extends LinearOpMode {

  private static final int ticksPerInch = 20, ticksPer360 = 200;

  private DcMotor motorFL, motorFR, motorBL, motorBR;
  private MecanumDrive drivetrain;

  @Override
  public void runOpMode() {

    motorFL = hardwareMap.dcMotor.get("motorFL");
    motorFR = hardwareMap.dcMotor.get("motorFR");
    motorBL = hardwareMap.dcMotor.get("motorBL");
    motorBR = hardwareMap.dcMotor.get("motorBR");

    drivetrain = MecanumDrive.fromOctagonalMotors(
        motorFL, motorFR, motorBL, motorBR, this, ticksPerInch, ticksPer360);

    /* Make the following sideways V shape:

      ⬈
      ⬉

     */

    for (int i = 1; i <= 2; i++) {
      drivetrain.strafeInches(Math.pow(-1, i) * 36 / Math.sqrt(2), 36 / Math.sqrt(2), 1);
    }

    /* Make the following sideways V shape:

      ⬊
      ⬋

     */

    for (int i = 1; i <= 2; i++) {
      drivetrain.strafeInches(Math.pow(-1, i - 1) * 36 / Math.sqrt(2), -36 / Math.sqrt(2), 1);
    }
  }
}
