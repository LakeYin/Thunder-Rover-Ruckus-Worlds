package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumAutonomousExample extends LinearOpMode {

  private DcMotor motorFL = hardwareMap.dcMotor.get("motorFL"),
      motorFR = hardwareMap.dcMotor.get("motorFR"),
      motorBL = hardwareMap.dcMotor.get("motorBL"),
      motorBR = hardwareMap.dcMotor.get("motorBR");
  private MecanumDrive mecanumDrive = MecanumDrive
      .fromOctagonalMotors(motorFL, motorFR, motorBL, motorBR, this, ticksPerInch, ticksPer360);
  private int ticksPerInch, ticksPer360;

  @Override
  public void runOpMode() throws InterruptedException {
    /* Make the following sideways V shape:

      ⬈
      ⬉

     */

    for (int i = 1; i <= 2; i++) {
      mecanumDrive.strafeInches(Math.pow(-1, i) * 36 / Math.sqrt(2), 36 / Math.sqrt(2), 1);
    }

    /* Make the following sideways V shape:

      ⬊
      ⬋

     */

    for (int i = 1; i <= 2; i++) {
      mecanumDrive.strafeInches(Math.pow(-1, i - 1) * 36 / Math.sqrt(2), -36 / Math.sqrt(2), 1);
    }
  }
}
