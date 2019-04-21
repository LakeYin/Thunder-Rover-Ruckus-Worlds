package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.tasks.RotateByIMUTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.Task;

import java.io.IOException;

@Autonomous(name = "Turn with IMU", group = "ARC")
public class TurnWithIMUDemo extends LinearOpMode {

  private BNO055IMU imu;
  private Bot bot;

  @Override
  public void runOpMode() throws InterruptedException {
    setupIMU();
    try {
      bot = new Bot(hardwareMap, telemetry, hardwareMap.appContext, this);
    } catch (IOException e) {
      e.printStackTrace();
    }
    waitForStart();

    Task task = new RotateByIMUTask(bot.drivetrain, 90);
    task.run();

    while (opModeIsActive());
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
