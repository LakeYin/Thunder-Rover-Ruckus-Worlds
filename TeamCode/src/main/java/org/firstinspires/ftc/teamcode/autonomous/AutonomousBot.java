package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Bot;

import java.io.IOException;

public class AutonomousBot extends Bot {

  public AutonomousBot(OpMode opMode) throws IOException {
    super(opMode.hardwareMap, opMode.telemetry, opMode.hardwareMap.appContext, opMode);
  }
}
