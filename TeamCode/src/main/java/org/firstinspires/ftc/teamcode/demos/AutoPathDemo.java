package org.firstinspires.ftc.teamcode.demos;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import java.util.Arrays;
import org.firstinspires.ftc.teamcode.HookLift;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral;
import org.firstinspires.ftc.teamcode.autonomous.VuforiaManager;

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

    for (DcMotor motor : Arrays.asList(motorFR, motorBR, motorBL, motorFL)) {
      motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }

    StrafingDriveTrain dt = MecanumDrive.fromOctagonalMotors(
        motorFL,
        motorFR,
        motorBL,
        motorBR,
        this,
        63,
        5230
    );
    dt.setDefaultDrivePower(0.9);
    VuforiaManager.initVuforia(hardwareMap);

    DcMotor hookLift = hardwareMap.dcMotor.get("hookLift");
    hookLift.setMode(RunMode.STOP_AND_RESET_ENCODER);
    while (hookLift.getCurrentPosition() != 0 && opModeIsActive()) {
      ;
    }
    HookLift lift = new HookLift(hookLift);

    waitForStart();

    new Thread(() -> {
      while (opModeIsActive()) {
        telemetry.addData("nanoTime", System.nanoTime());
        telemetry.update();
      }
    }).start();

//    move 4 1
//    move 0 80
//    rotate -40
//    move -2 20
//    drop_team_marker
    MineralDetector detector = new MineralDetector(hardwareMap);
    detector.activate();

    lift.liftToHook();
    dt.setMovementPower(1);
    sleep(200);
    dt.setStrafe(-1, 0);
    sleep(650);
    dt.setStrafe(0, -1);
    sleep(270);
    dt.stop();

    lift.lowerToBottom().begin();

    runCraterPath(dt);

    dt.strafeInches(5, 40);


    sleep(700);
    if (isGoldDetected(detector)) {
      knock(dt);
      return;
    }

    dt.driveForwards(19);
    sleep(700);
    if (isGoldDetected(detector)) {
      knock(dt);
      return;
    }

    dt.driveForwards(19);
    knock(dt);
  }

  private boolean isGoldDetected(MineralDetector detector) {
    return detector.currentRecognition().orElse(Mineral.SILVER) == Mineral.GOLD;
  }

  private void knock(StrafingDriveTrain dt) {
    dt.rotateCounterClockwise(90);
    dt.driveForwards(20);
    dt.driveBackwards(20);
  }

  private void runCraterPath(StrafingDriveTrain dt) {
    dt.strafeInches(-7, 1);
    dt.strafeInches(0, -63);
    dt.rotateClockwise(135);
    dt.strafeRight(9);
    dt.strafeInches(-0.3, 4);
    sleep(2000);
    dt.rotateCounterClockwise(135);
  }
}
