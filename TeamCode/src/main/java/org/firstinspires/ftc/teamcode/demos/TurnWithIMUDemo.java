package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

@Autonomous(name = "Turn with IMU", group = "ARC")
public class TurnWithIMUDemo extends LinearOpMode {

  private BNO055IMU imu;

  @Override
  public void runOpMode() throws InterruptedException {
    setupIMU();
    waitForStart();

    float heading = 0;
    while (Math.abs(heading - 180) > 5 && opModeIsActive()) {
      for (DcMotor motor : hardwareMap.getAll(DcMotor.class)) {
        heading = imu.getAngularOrientation().toAngleUnit(
            org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES).firstAngle + 180;
        motor.setMode(RunMode.RUN_USING_ENCODER);
        motor.setPower(Math.abs(heading) / -180.0);

      }
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
