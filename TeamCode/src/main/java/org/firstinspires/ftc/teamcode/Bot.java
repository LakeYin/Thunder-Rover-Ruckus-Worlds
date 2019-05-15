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
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevExtensions2;

public class Bot extends ChassisBot{

  private static Bot instance = null;

  public static Bot getInstance() {
    if (instance == null) {
      throw new NullPointerException("Bot instance referenced before the Bot is instantiated");
    }
    return instance;
  }

  // Hardware
  public final Intake intake;
  public final DepositSystem deposit;
  public final HookLift hookLift;
  public final BNO055IMU imu;

  protected Bot(HardwareMap hardware,
      Telemetry telemetry,
      Context context,
      LinearOpMode opMode) throws IOException {

    super(hardware, telemetry, context, opMode);

    imu = hardware.get(BNO055IMU.class, "imu");
    initializeImu();

    DeviceMapping<DcMotor> motorHw = hardware.dcMotor;
    DeviceMapping<Servo> servoHw = hardware.servo;

//    if (mainConfig.getBoolean("useSimulation")) {
//      new SimulationRelay(mainConfig.getInt("simulationRelayPort"));
//      drivetrain = new SimDriveTrain(opMode);
//    } else {
//    }

    intake = new Intake(
        motorHw.get("intakeSlide"),
        servoHw.get("intakeOrientator"),
        hardware.crservo.get("intakeSweeper"), opMode);
    deposit = new DepositSystem(
        motorHw.get("depositSlide"),
        servoHw.get("depositOrientator"), opMode);

    DcMotor hookLift = motorHw.get("hookLift");
    hookLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    this.hookLift = new HookLift(hookLift);

    instance = this;
  }

  private void initializeImu() {
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
    parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    imu.initialize(parameters);
  }
}
