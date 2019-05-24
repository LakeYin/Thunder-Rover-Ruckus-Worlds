package org.firstinspires.ftc.teamcode;

import android.content.Context;
import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.Diagnosable;
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

  public static void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
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
  public final BNO055IMU imu;

  public Bot(HardwareMap hardware,
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
    imu = hardware.get(BNO055IMU.class, "imu");
    initializeImu();

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

    frontLeft.setDirection(Direction.REVERSE);
    backLeft.setDirection(Direction.REVERSE);

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

    DcMotor intakeSlide = motorHw.get("intakeSlide");
    intake = new Intake(
        intakeSlide,
        servoHw.get("intakeOrientator"),
        hardware.crservo.get("intakeSweeper"));
    DcMotor depositSlide = motorHw.get("depositSlide");
    depositSlide.setDirection(Direction.REVERSE);
    Servo depositOrientator = servoHw.get("depositOrientator");
    deposit = new DepositSystem(
        depositSlide, depositOrientator, opMode);

    DcMotor hookLift = motorHw.get("hookLift");
    hookLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    hookLift.setDirection(Direction.REVERSE);
    this.hookLift = new HookLift(hookLift);

    instance = this;
  }

  public void addAllDiagnosableData() {
    Diagnosable.addAll(telemetry, deposit, hookLift, intake);
  }

  private void initializeImu() {
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
    parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    imu.initialize(parameters);
  }
}
