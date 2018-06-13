package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

@Autonomous(name = "TankDrive Autonomous Example", group = "ARC")
public class TankDriveAutonomousExample extends LinearOpMode {

  private static final int ticksPerInch = 20, ticksPer360 = 200;

  private DcMotor motorL, motorR;
  private TankDrive tankDrive;

  @Override
  public void runOpMode() {
    motorL = hardwareMap.dcMotor.get("motorL");
    motorR = hardwareMap.dcMotor.get("motorR");
    motorL.setDirection(Direction.REVERSE);

    tankDrive = TankDrive.fromMotors(motorL, motorR, this, ticksPerInch, ticksPer360);

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
