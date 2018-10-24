package org.firstinspires.ftc.teamcode.autonomous;

import android.content.Context;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;

public class AutonomousBot extends Bot {

  public AutonomousBot(HardwareMap hardware,
      Telemetry telemetry, Context context,
      OpMode opMode) throws IOException {
    super(hardware, telemetry, context, opMode);
  }
}
