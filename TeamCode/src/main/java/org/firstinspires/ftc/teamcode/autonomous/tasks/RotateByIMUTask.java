package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.andoverrobotics.core.drivetrain.DriveTrain;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Bot;

public class RotateByIMUTask extends RotateTask {
  // returns IMU oriented heading
  public static int targetHeading(int initialHeading, int turnDegrees) {
    return denormalize(normalize(initialHeading) + turnDegrees);
  }

  public static int normalize(int imuAngle) {
    int output = imuAngle + 180;
    while (output < 0) {
      output += 360;
    }
    return output % 360;
  }

  public static int denormalize(int normalAngle) {
    while (normalAngle < 0) {
      normalAngle += 360;
    }
    return normalAngle % 360 - 180;
  }

  // returns diff
  public static int error(int currentReading, int targetHeading) {
    int output = (normalize(targetHeading) - normalize(currentReading)) % 360;
    if (output < -180) output += 360;
    if (output > 180) output -= 360;
    return output;
  }

  private final int initialHeading, targetHeading;

  public RotateByIMUTask(DriveTrain drivetrain, int degrees) {
    super(drivetrain, degrees);
    initialHeading = readHeading();
    targetHeading = targetHeading(initialHeading, degrees);
  }

  private int readHeading() {
    Orientation orientation = Bot.getInstance().imu.getAngularOrientation();
    return (int) orientation.toAngleUnit(AngleUnit.DEGREES).firstAngle;
  }


  // Start P controller
  public void run() {
    int heading, error;
    do {
      heading = readHeading();
      error = error(heading, targetHeading);

      drivetrain.setRotationPower(powerByError(error));

    } while (Math.abs(error) > 2);
  }

  private double powerByError(int error) {
    if (Math.abs(error) > 20) return Math.copySign(0.8, error);
    double mag = 0.8 * Math.sin(error / (4 * Math.PI));
    return Math.copySign(mag, error);
  }

  // Start PD controller
  private static final double Kp = 0.8 / 30, Kd = 0.1;

  public void runIMU() {
    int prevError = error(initialHeading, targetHeading),
        heading = initialHeading;
    for (; heading != targetHeading; heading = readHeading()) {
      int pError = error(heading, targetHeading),
          dError = (pError - prevError) * 10;
      prevError = pError;

      drivetrain.setRotationPower(pError * Kp + dError * Kd);
    }
  }
}
