package org.firstinspires.ftc.teamcode;

import android.content.Context;
import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Bot {

  private static Bot instance = null;

  public static Bot getInstance() {
    if (instance == null) {
      throw new NullPointerException("Bot instance referenced before the Bot is instantiated");
    }
    return instance;
  }

  public final StrafingDriveTrain drivetrain;

  // Configuration
  public final Configuration mainConfig;

  // Context
  public final Context context;
  public final OpMode opMode;
  public final Telemetry telemetry;

  // Hardware
  public final Arm leftArm, rightArm;
  public final SimpleArm backArm;

  public Bot(HardwareMap hardware,
      Telemetry telemetry,
      Context context,
      OpMode opMode) throws IOException {

    mainConfig = Configuration.fromPropertiesFile("mainConfig.properties");

    this.context = context;
    this.opMode = opMode;
    this.telemetry = telemetry;

    DeviceMapping<DcMotor> motorHw = hardware.dcMotor;
    DeviceMapping<Servo> servoHw = hardware.servo;

    drivetrain = MecanumDrive.fromOctagonalMotors(
        motorHw.get("motorFL"),
        motorHw.get("motorFR"),
        motorHw.get("motorRL"),
        motorHw.get("motorRR"),
        opMode, mainConfig.getInt("ticksPerInch"),
        mainConfig.getInt("ticksPer360")
    );
    leftArm = new Arm(
        motorHw.get("leftLift"),
        servoHw.get("leftGrabber"),
        servoHw.get("leftLateral"),
        servoHw.get("leftVertical")
    );
    rightArm = new Arm(
        motorHw.get("rightLift"),
        servoHw.get("rightGrabber"),
        servoHw.get("rightLateral"),
        servoHw.get("rightVertical")
    );
    backArm = new SimpleArm(
        motorHw.get("backLift"),
        servoHw.get("backGrabber")
    );

    instance = this;
  }
}
