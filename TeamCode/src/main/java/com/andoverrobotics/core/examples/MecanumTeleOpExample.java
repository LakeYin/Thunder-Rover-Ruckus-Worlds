package com.andoverrobotics.core.examples;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.HookLift;

@TeleOp(name = "Mecanum TeleOp Example", group = "ARC")
public class MecanumTeleOpExample extends LinearOpMode {

  private static final int TICKS_PER_INCH = 20, TICKS_PER_360 = 200;

  private MecanumDrive mecanumDrive;
  private BNO055IMU imu;
  private HookLift lift;

  @Override
  public void runOpMode() {
    DcMotor motorFL = hardwareMap.dcMotor.get("motorFL");
    DcMotor motorFR = hardwareMap.dcMotor.get("motorFR");
    DcMotor motorBL = hardwareMap.dcMotor.get("motorBL");
    DcMotor motorBR = hardwareMap.dcMotor.get("motorBR");

    motorFL.setDirection(Direction.REVERSE);
    motorBL.setDirection(Direction.REVERSE);

    mecanumDrive = MecanumDrive.fromOctagonalMotors(
        motorFL, motorFR, motorBL, motorBR, this, TICKS_PER_INCH, TICKS_PER_360);
    lift = new HookLift(hardwareMap.dcMotor.get("hookLift"));

    setupIMU();
    waitForStart();
    while (opModeIsActive()) {
      mecanumDrive.setStrafeAndRotation(
          Coordinate.fromXY(gamepad1.left_stick_x, -gamepad1.left_stick_y),
          gamepad1.right_stick_x, 1);
      if (gamepad1.y) {
        lift.liftToHook().begin();
      }
      else if (gamepad1.a) {
        lift.lowerToBottom().begin();
      }
      if (gamepad1.x) {
        lift.adjust(0);
      }


      telemetry.addData("Left stick X", gamepad1.left_stick_x);
      telemetry.addData("Left stick Y", -gamepad1.left_stick_y);
      telemetry.addData("Right stick X", gamepad1.right_stick_x);
      telemetry.addData("Heading", imu.getAngularOrientation().firstAngle);
      telemetry.update();
    }
  }

  private void setupIMU() {
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
    parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

    // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
    // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
    // and named "imu".
    imu = hardwareMap.get(BNO055IMU.class, "imu");
    imu.initialize(parameters);
  }
}
