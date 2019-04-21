package org.firstinspires.ftc.teamcode.autonomous.tasks;

import android.util.Log;
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

  private static final double Kp = 0.8 / 30, Kd = 0.1;

  @Override
  public void run() {
    double prevError = error(initialHeading, targetHeading);
    int heading = initialHeading;
    for (; heading != targetHeading; heading = readHeading()) {
      double pError = error(heading, targetHeading),
          dError = (pError - prevError) / 0.05;
      prevError = pError;

      double output = pError * Kp + dError * Kd;
      drivetrain.setRotationPower(output);

      Log.d("PID Controller", String.format("pError=%.2f dError=%.2f output=%.2f", pError, dError, output));
      Bot.sleep(50);
    }
  }
}
