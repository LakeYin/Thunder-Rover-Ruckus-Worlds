package org.firstinspires.ftc.teamcode.demos;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

@Autonomous(name = "AutoPath Demo", group = "ARC")
public class AutoPathDemo extends LinearOpMode {

  @Override
  public void runOpMode() throws InterruptedException {
    DcMotor motorFR = hardwareMap.dcMotor.get("motorFR");
    DcMotor motorBR = hardwareMap.dcMotor.get("motorBR");
    DcMotor motorBL = hardwareMap.dcMotor.get("motorBL");
    DcMotor motorFL = hardwareMap.dcMotor.get("motorFL");

    motorFL.setDirection(Direction.REVERSE);
    motorBL.setDirection(Direction.REVERSE);

    StrafingDriveTrain dt = MecanumDrive.fromOctagonalMotors(
        motorFL,
        motorFR,
        motorBL,
        motorBR,
        this,
        63,
        5400
    );
    dt.setDefaultDrivePower(0.5);

    waitForStart();

//    move 4 1
//    move 0 80
//    rotate -40
//    move -2 20
//    drop_team_marker
    dt.strafeInches(4, 1);
    dt.strafeInches(0, -50);
    dt.rotateClockwise(125);
    dt.strafeInches(2, 5);
    sleep(4000);
    dt.rotateCounterClockwise(180);
    dt.strafeInches(0, 10);

  }
}
