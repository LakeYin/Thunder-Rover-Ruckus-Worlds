package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name = "Run To Position Demo", group = "ARC Lightning")
public class RunToPositionDemo extends LinearOpMode {

  @Override
  public void runOpMode() throws InterruptedException {
    DcMotorEx testMotor = hardwareMap.get(DcMotorEx.class, "leftLift");

    waitForStart();

    testMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    testMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    while (testMotor.getCurrentPosition() != 0 && opModeIsActive());
    testMotor.setMode(RunMode.RUN_TO_POSITION);

    testMotor.setTargetPosition(-500);
    testMotor.setPower(0.4);

    while (opModeIsActive()) {
      telemetry.addData("statement", "run to position -500 at speed 0.4");
      telemetry.addData("current pos", testMotor.getCurrentPosition());
      telemetry.addData("target pos", testMotor.getTargetPosition());
      telemetry.addData("power", testMotor.getPower());
      telemetry.addData("velocity deg/sec", testMotor.getVelocity(AngleUnit.DEGREES));
      telemetry.addData("pidf", testMotor.getPIDFCoefficients(RunMode.RUN_TO_POSITION));
      telemetry.addData("mode", testMotor.getMode());
      telemetry.addData("busy?", testMotor.isBusy() ? "very busy" : "taking a break (brake)");
      telemetry.update();
    }
  }
}
