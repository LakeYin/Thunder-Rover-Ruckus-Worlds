package org.firstinspires.ftc.teamcode;

import android.content.Context;
import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
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

    DcMotor frontLeft = motorHw.get("motorFL"),
        backLeft = motorHw.get("motorBL");

    frontLeft.setDirection(Direction.REVERSE);
    backLeft.setDirection(Direction.REVERSE);

    drivetrain = MecanumDrive.fromOctagonalMotors(
        frontLeft,
        motorHw.get("motorFR"),
        backLeft,
        motorHw.get("motorBR"),
        opMode, mainConfig.getInt("ticksPerInch"),
        mainConfig.getInt("ticksPer360")
    );
    leftArm = new Arm(
        motorHw.get("leftLift"),
        servoHw.get("leftGrabber"),
        servoHw.get("leftLateral"),
        hardware.crservo.get("leftVertical"),
        mainConfig.getDouble("leftClosed"),
        mainConfig.getDouble("leftOpen")
    );

    DcMotor rightLift = motorHw.get("rightLift");
    CRServo rightVertical = hardware.crservo.get("rightVertical");
    rightLift.setDirection(Direction.REVERSE);
    rightVertical.setDirection(Direction.REVERSE);

    rightArm = new Arm(
        rightLift,
        servoHw.get("rightGrabber"),
        servoHw.get("rightLateral"),
        rightVertical,
        mainConfig.getDouble("rightClosed"),
        mainConfig.getDouble("rightOpen")
    );
    backArm = new SimpleArm(
        motorHw.get("backLift"),
        servoHw.get("backGrabber"),
        mainConfig.getDouble("backClosed"),
        mainConfig.getDouble("backOpen")
    );

    instance = this;
  }
}
