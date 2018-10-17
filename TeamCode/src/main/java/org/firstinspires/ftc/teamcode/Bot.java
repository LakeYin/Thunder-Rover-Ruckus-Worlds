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
  final StrafingDriveTrain drivetrain;

  // Configuration
  private final Configuration mainConfig;

  // Context
  private final Context context;
  private final OpMode opMode;

  public Bot(HardwareMap hardware,
      Telemetry telemetry,
      Context context,
      OpMode opMode) throws IOException {

    mainConfig = Configuration.fromPropertiesFile("mainConfig.properties");

    this.context = context;
    this.opMode = opMode;

    drivetrain = MecanumDrive.fromOctagonalMotors(
        hardware.dcMotor.get("motorFL"),
        hardware.dcMotor.get("motorFR"),
        hardware.dcMotor.get("motorRL"),
        hardware.dcMotor.get("motorRR"),
        opMode, mainConfig.getInt("ticksPerInch"),
        mainConfig.getInt("ticksPer360")
    );


  }
}
