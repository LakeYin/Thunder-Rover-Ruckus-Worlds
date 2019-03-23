package org.firstinspires.ftc.teamcode.teleop.chassis;

import com.andoverrobotics.core.drivetrain.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class TankDriveTester extends LinearOpMode {

  protected TankDrive drivetrain;

  @Override
  public void runOpMode() throws InterruptedException {
    drivetrain = TankDrive
        .fromMotors(hardwareMap.dcMotor.get("motorL"), hardwareMap.dcMotor.get("motorR"),
            this, 100, 500);

    setup();
    waitForStart();
    runLoop();
  }

  protected abstract void runLoop() throws InterruptedException;
  protected abstract void setup();
}
