package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU.Parameters;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Autonomous(name = "Drivetrain Coefficient IMU Demo", group = "ARC")
public class DrivetrainCoefficientIMUDemo extends LinearOpMode {

  private BNO055IMU imu;
  private DcMotor motorFL, motorFR, motorBL, motorBR;

  @Override
  public void runOpMode() {
    resolveMotors();
    initIMU();

    waitForStart();

    imu.startAccelerationIntegration(new Position(), new Velocity(), 100);

    strafe(0, 0.1, 0);
    logWhileWaiting((x, y) -> y >= 12);
    addCurrentPositionsToReport("x=0 y=1");

    strafe(0.1, 0, 0);
    logWhileWaiting((x, y) -> x >= 12);
    addCurrentPositionsToReport("x=1 y=0");

    strafe(0, -0.1, 0);
    logWhileWaiting((x, y) -> y <= 0);
    addCurrentPositionsToReport("x=0 y=-1");

    strafe(-0.1, 0, 0);
    logWhileWaiting((x, y) -> x <= 0);
    addCurrentPositionsToReport("x=-1 y=0");

    strafe(0, 0, 0.2);
    while (opModeIsActive()) {
      Orientation orientation = imu.getAngularOrientation();
      float heading = orientation.angleUnit.toDegrees(orientation.firstAngle);
      telemetry.addData("hdg", heading);
      telemetry.update();

      if (heading >= 180)
        break;
    }
    addCurrentPositionsToReport("180deg");

    showReport();
    while (opModeIsActive());
  }

  private void logWhileWaiting(BiPredicate<Double, Double> shouldStop) {
    while (opModeIsActive()) {
      double y = imu.getPosition().unit.toInches(imu.getPosition().y),
          x = imu.getPosition().unit.toInches(imu.getPosition().x);
      telemetry.addData("y", y);
      telemetry.addData("x", x);
      telemetry.update();

      if (shouldStop.test(x, y))
        break;
    }
    strafe(0, 0, 0);
  }

  private void strafe(double x, double y, double z) {
    resetEncoders();
    motorFL.setPower(y + x + z);
    motorFR.setPower(y - x - z);
    motorBL.setPower(y - x + z);
    motorBR.setPower(y + x - z);
  }

  private void resetEncoders() {
    for (DcMotor motor : Arrays.asList(motorFL, motorFR, motorBL, motorBR)) {
      motor.setPower(0);
      motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
      while (motor.getCurrentPosition() != 0 && opModeIsActive());
    }
  }

  private void initIMU() {
    BNO055IMU.Parameters params = new Parameters();
    params.mode = BNO055IMU.SensorMode.IMU;
    params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
    params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    params.loggingEnabled = false;

    imu = hardwareMap.get(BNO055IMU.class, "imu");
    imu.initialize(params);

    telemetry.addData("calibrating", "imu");
    telemetry.update();

    while (!isStopRequested() && !imu.isGyroCalibrated()) {
      sleep(50);
      idle();
    }

    telemetry.addData("imu calib status", imu.getCalibrationStatus());
    telemetry.update();
  }

  private void resolveMotors() {
    motorFL = hardwareMap.dcMotor.get("motorFL");
    motorFR = hardwareMap.dcMotor.get("motorFR");
    motorBL = hardwareMap.dcMotor.get("motorBL");
    motorBR = hardwareMap.dcMotor.get("motorBR");
  }

  private List<String> reportItems = new LinkedList<>();
  private void addCurrentPositionsToReport(String title) {
    reportItems.add(">>> " + title + " <<<");
    reportItems.add("motorFL " + motorFL.getCurrentPosition());
    reportItems.add("motorFR " + motorFR.getCurrentPosition());
    reportItems.add("motorBL " + motorBL.getCurrentPosition());
    reportItems.add("motorBR " + motorBR.getCurrentPosition());
    reportItems.add("<<< end " + title + " >>>");
  }

  private void showReport() {
    for (String item : reportItems) {
      telemetry.addData("", item);
    }
    telemetry.update();
  }
}
