package org.firstinspires.ftc.teamcode.simulation;

import android.util.Log;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import org.firstinspires.ftc.teamcode.Bot;

public class SimulationRelay {

  private ServerSocket socket;
  private Socket client;
  private PrintStream stream;

  public SimulationRelay(int port) {
    try {

      if (instance != null)
        return;

      this.socket = new ServerSocket(port);
      instance = this;

      new Thread(() -> {
        try {
          client = socket.accept();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();

    } catch (IOException e) {
      Log.e("SimulationRelay", "Unavailable because of error", e);
    }
  }

  public void relayAction(String action) {
    try {

      if (client == null) {
        Log.i("SimulationRelay", "Attempt to relayAction while client has not connected");
        return;
      }
      getStream().println(action);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private PrintStream getStream() throws IOException {
    if (stream == null) {
      stream = new PrintStream(client.getOutputStream());
    }
    return stream;
  }

  private static SimulationRelay instance = null;

  public static void relayServoSetPosition(Servo target, double newPosition) {
    relayHardware(target, "setPosition:" + newPosition);
  }

  public static void relayHardware(HardwareDevice device, String action) {
    Set<String> names = Bot.getInstance().opMode.hardwareMap.getNamesOf(device);
    if (names.isEmpty()) {
      Log.w("SimulationRelay", "Target HardwareDevice has no name!");
      return;
    }
    String name = names.iterator().next();

    relayString("%s:%s", name, action);
  }

  public static void relayString(String action, Object... args) {
    if (instance != null) {
      instance.relayAction(String.format(action, args));
    }
  }
}
