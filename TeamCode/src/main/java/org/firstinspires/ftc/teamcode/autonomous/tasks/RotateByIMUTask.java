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

  /*
  IMU input unit circle
     0
  90   -90
   +-180

  Normal unit circle
       180
    270   90
        0
   */

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
    while (output < -180) output += 360;
    while (output > 180) output -= 360;
    return output;
  }

  private final int initialHeading, targetHeading;

  public RotateByIMUTask(DriveTrain drivetrain, int degrees) {
    super(drivetrain, degrees);
    initialHeading = readHeading();
    targetHeading = targetHeading(initialHeading, -degrees);
  }

  public static int readHeading() {
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

    } while (Math.abs(error) > 1 && Bot.getInstance().opMode.opModeIsActive());
    drivetrain.stop();
  }

  private double powerByError(int error) {
    double rawOut = -error / 90.0;
    if (rawOut == 0) return 0;
    rawOut += rawOut / Math.abs(rawOut) * 0.27;
    return rawOut;
  }

  // Start PD controller
  private static final double Kp = 0.8 / 30, Kd = 0.1;

  public void runPD() {
    int prevError = error(initialHeading, targetHeading),
        heading = initialHeading;
    for (; heading != targetHeading; heading = readHeading()) {
      int pError = error(heading, targetHeading),
          dError = (pError - prevError) * 10;
      prevError = pError;

      drivetrain.setRotationPower(pError * Kp + dError * Kd);
    }
  }

  public static void rotate(int degrees) {
    new RotateByIMUTask(Bot.getInstance().drivetrain, degrees).run();
  }
}
