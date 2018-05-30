package com.andoverrobotics.core.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TankDrive extends DriveTrain {

  private DcMotor motorL;
  private DcMotor motorR;
  private final int ticksPerInch;
  private final int ticksPer360;

  public TankDrive(DcMotor motorL, DcMotor motorR, OpMode opMode,
                   int ticksPerInch, int ticksPer360) {
    super(opMode);

    this.motorL = motorL;
    this.motorR = motorR;
    this.ticksPerInch = ticksPerInch;
    this.ticksPer360 = ticksPer360;
  }

  @Override
  public void driveForwards(double distanceInInches, double power) {
    power = Range.clip(power, -1, 1);
    power = Math.abs(power);

    motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    // Setting variables
    double robotTurn = distanceInInches * ticksPerInch ;

    motorL.setTargetPosition((int)(robotTurn));
    motorR.setTargetPosition((int)(robotTurn));

    // Sets the motors' positions
    motorL.setPower(power);
    motorR.setPower(power);

    // While loop for updating telemetry
    while(motorL.isBusy() && motorR.isBusy() && opModeIsActive())
    {
      // Updates the position of the motors
      double LPos = motorL.getCurrentPosition();
      double RPos = motorR.getCurrentPosition();

      // Adds telemetry of the drive motors
      opMode.telemetry.addData("motorL Pos:", LPos);
      opMode.telemetry.addData("motorR Pos:", RPos);

      // Updates the telemetry
      opMode.telemetry.update();

    }

    motorL.setPower(0);
    motorR.setPower(0);

    motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
  }

  @Override
  public void driveBackwards(double distanceInInches, double power) {
    driveForwards(-distanceInInches, power);
  }

  @Override
  public void rotateClockwise(int degrees, double power) {
    power = Range.clip(power, -1, 1);
    power = Math.abs(power);

    motorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    // Setting variables
    double robotTurn = degrees * ticksPer360 / 360.0;

    motorL.setTargetPosition((int)(-robotTurn));
    motorR.setTargetPosition((int)(robotTurn));

    // Sets the motors' positions
    motorL.setPower(power);
    motorR.setPower(power);

    // While loop for updating telemetry
    while(motorL.isBusy() && motorR.isBusy() && opModeIsActive())
    {
      // Updates the position of the motors
      double LPos = motorL.getCurrentPosition();
      double RPos = motorR.getCurrentPosition();

      // Adds telemetry of the drive motors
      opMode.telemetry.addData("motorL Pos:", LPos);
      opMode.telemetry.addData("motorR Pos:", RPos);

      // Updates the telemetry
      opMode.telemetry.update();

    }

    motorL.setPower(0);
    motorR.setPower(0);

    motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
  }

  @Override
  public void rotateCounterClockwise(int degrees, double power) {
    rotateClockwise(-degrees, power);
  }

  // TeleOp methods

  @Override
  public void setMovementPower(double power) {
    motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    motorL.setPower(power);
    motorR.setPower(power);

    motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
  }

  @Override
  public void setRotationPower(double power) { //clockwise if power is positive
    motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    motorL.setPower(power);
    motorR.setPower(-power);

    motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
  }

}
