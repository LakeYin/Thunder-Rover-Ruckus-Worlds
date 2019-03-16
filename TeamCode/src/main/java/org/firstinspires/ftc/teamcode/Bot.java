package org.firstinspires.ftc.teamcode;

import android.content.Context;
import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevExtensions2;

public class Bot {

  private static Bot instance = null;

  public static Bot getInstance() {
    if (instance == null) {
      throw new NullPointerException("Bot instance referenced before the Bot is instantiated");
    }
    return instance;
  }

  public final MecanumDrive drivetrain;

  // Configuration
  public final Configuration mainConfig;

  // Context
  public final Context context;
  public final OpMode opMode;
  public final Telemetry telemetry;

  // Hardware
  public final Arm leftArm, rightArm;
  public final SimpleArm hookArm;
  public final ExpansionHubEx hub2, hub7;

  protected Bot(HardwareMap hardware,
      Telemetry telemetry,
      Context context,
      OpMode opMode) throws IOException {

    mainConfig = Configuration.fromPropertiesFile("mainConfig.properties");

    this.context = context;
    this.opMode = opMode;
    this.telemetry = telemetry;

    RevExtensions2.init();
    hub2 = hardware.get(ExpansionHubEx.class, "Expansion Hub 2");
    hub7 = hardware.get(ExpansionHubEx.class, "Expansion Hub 7");

    DeviceMapping<DcMotor> motorHw = hardware.dcMotor;
    DeviceMapping<Servo> servoHw = hardware.servo;

//    if (mainConfig.getBoolean("useSimulation")) {
//      new SimulationRelay(mainConfig.getInt("simulationRelayPort"));
//      drivetrain = new SimDriveTrain(opMode);
//    } else {
      DcMotor frontLeft = motorHw.get("motorFL"),
          backLeft = motorHw.get("motorBL"),
          frontRight = motorHw.get("motorFR"),
          backRight = motorHw.get("motorBR");

      frontRight.setDirection(Direction.REVERSE);
      backRight.setDirection(Direction.REVERSE);

      drivetrain = MecanumDrive.fromOctagonalMotors(
          frontLeft,
          frontRight,
          backLeft,
          backRight,
          opMode, mainConfig.getInt("ticksPerInch"),
          mainConfig.getInt("ticksPer360")
      );
      drivetrain.setDefaultDrivePower(mainConfig.getDouble("defaultDrivePower"));
//    }

    DcMotor leftLift = motorHw.get("leftLift");
    DcMotor rightLift = motorHw.get("rightLift");
    rightLift.setDirection(Direction.REVERSE);
    leftLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    rightLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

    CRServo leftExtender = hardware.crservo.get("leftExtender");
    leftArm = new Arm(
        leftLift,
        servoHw.get("leftGrabber"),
        leftExtender,
        mainConfig.getDouble("leftClosed"),
        mainConfig.getDouble("leftOpen")
    );

    rightArm = new Arm(
        rightLift,
        servoHw.get("rightGrabber"),
        hardware.crservo.get("rightExtender"),
        mainConfig.getDouble("rightClosed"),
        mainConfig.getDouble("rightOpen")
    );

    DcMotor hookLift = motorHw.get("hookLift");
    hookLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    hookLift.setDirection(Direction.REVERSE);

    hookArm = new SimpleArm(hookLift);

    instance = this;
  }
}
