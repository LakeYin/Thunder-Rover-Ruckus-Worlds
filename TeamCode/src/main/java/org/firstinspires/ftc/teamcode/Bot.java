package org.firstinspires.ftc.teamcode;

import android.content.Context;
import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
  public final LinearOpMode opMode;
  public final Telemetry telemetry;

  // Hardware
  public final Intake intake;
  public final DepositSystem deposit;
  public final HookLift hookLift;
  public final ExpansionHubEx hub2, hub7;

  protected Bot(HardwareMap hardware,
      Telemetry telemetry,
      Context context,
      LinearOpMode opMode) throws IOException {

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

    intake = new Intake(
        motorHw.get("intakeSlide"),
        servoHw.get("intakeOrientator"),
        hardware.crservo.get("intakeSweeper"));
    deposit = new DepositSystem(
        motorHw.get("depositSlide"),
        servoHw.get("depositOrientator"),
        this
    );


    DcMotor hookLift = motorHw.get("hookLift");
    hookLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    hookLift.setDirection(Direction.REVERSE);
    this.hookLift = new HookLift(hookLift, this);

    instance = this;
  }
}
