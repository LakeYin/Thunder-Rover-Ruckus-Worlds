package org.firstinspires.ftc.teamcode;

import android.content.Context;
import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Bot {
  private static Bot instance = null;

  public static Bot getInstance() {
    if (instance == null)
      throw new NullPointerException("Bot instance referenced before the Bot is instantiated");
    return instance;
  }

  public final StrafingDriveTrain drivetrain;

  // Configuration
  public final Configuration mainConfig;

  // Context
  public final Context context;
  public final OpMode opMode;
  public final Telemetry telemetry;

  public Bot(HardwareMap hardware,
      Telemetry telemetry,
      Context context,
      OpMode opMode) throws IOException {

    mainConfig = Configuration.fromPropertiesFile("mainConfig.properties");

    this.context = context;
    this.opMode = opMode;
    this.telemetry = telemetry;

    drivetrain = MecanumDrive.fromOctagonalMotors(
        hardware.dcMotor.get("motorFL"),
        hardware.dcMotor.get("motorFR"),
        hardware.dcMotor.get("motorRL"),
        hardware.dcMotor.get("motorRR"),
        opMode, mainConfig.getInt("ticksPerInch"),
        mainConfig.getInt("ticksPer360")
    );

    instance = this;
  }
}
