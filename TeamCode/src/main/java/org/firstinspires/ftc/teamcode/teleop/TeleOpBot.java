package org.firstinspires.ftc.teamcode.teleop;

import android.content.Context;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;

public class TeleOpBot extends Bot {

  // package-scope, final fields

  public static TeleOpBot fromOpMode(OpMode opMode) throws IOException {
    return new TeleOpBot(opMode.hardwareMap,
            opMode.telemetry, opMode.hardwareMap.appContext, opMode);
  }

  public TeleOpBot(HardwareMap hardware,
      Telemetry telemetry, Context context,
      OpMode opMode) throws IOException {
    super(hardware, telemetry, context, opMode);
  }
}
