package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "TankDrive Autonomous Example")
public class TankDriveAutonomousExample extends LinearOpMode {

  private DcMotor motorL = hardwareMap.dcMotor.get("motorL"),
      motorR = hardwareMap.dcMotor.get("motorR");
  private int ticksPerInch, ticksPer360;
  private TankDrive tankDrive = TankDrive.fromMotors(motorL, motorR, this, ticksPerInch, ticksPer360);

  @Override
  public void runOpMode() throws InterruptedException {
    for (int i = 0; i < 4; i++) {
      tankDrive.driveForwards(36, 1);
      telemetry.addLine("Traveled 36 inches");
      telemetry.update();
      tankDrive.rotateClockwise(90, 1);
      telemetry.addLine("Turned 90 degrees clockwise");
      telemetry.update();
    }
  }
}
