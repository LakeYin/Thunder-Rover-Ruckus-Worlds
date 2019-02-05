package org.firstinspires.ftc.teamcode;

import android.content.Context;
import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.simulation.SimDriveTrain;
import org.firstinspires.ftc.teamcode.simulation.SimulationRelay;

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

  protected Bot(HardwareMap hardware,
      Telemetry telemetry,
      Context context,
      OpMode opMode) throws IOException {

    mainConfig = Configuration.fromPropertiesFile("mainConfig.properties");

    this.context = context;
    this.opMode = opMode;
    this.telemetry = telemetry;

    DeviceMapping<DcMotor> motorHw = hardware.dcMotor;
    DeviceMapping<Servo> servoHw = hardware.servo;

//    if (mainConfig.getBoolean("useSimulation")) {
//      new SimulationRelay(mainConfig.getInt("simulationRelayPort"));
//      drivetrain = new SimDriveTrain(opMode);
//    } else {
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
      drivetrain.setDefaultDrivePower(mainConfig.getDouble("defaultDrivePower"));
//    }

    DcMotor leftLift = motorHw.get("leftLift");
    DcMotor rightLift = motorHw.get("rightLift");
    rightLift.setDirection(Direction.REVERSE);
    leftLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    rightLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

    leftArm = new Arm(
        leftLift,
        servoHw.get("leftGrabber"),
        mainConfig.getDouble("leftClosed"),
        mainConfig.getDouble("leftOpen")
    );

    rightArm = new Arm(
        rightLift,
        servoHw.get("rightGrabber"),
        mainConfig.getDouble("rightClosed"),
        mainConfig.getDouble("rightOpen")
    );

    DcMotor hookLift = motorHw.get("hookLift");
    hookLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    hookLift.setDirection(Direction.FORWARD);

    hookArm = new SimpleArm(hookLift);

    instance = this;
  }
}
