package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

@Autonomous(name = "Drivetrain Encoder Self Test", group = "ARC Lightning")
public class DrivetrainSelfTestDemo extends LinearOpMode {

  private static final String[] kMotorNames = {
      "motorFL", "motorFR", "motorBL", "motorBR"
  };

  private AndroidTextToSpeech tts = new AndroidTextToSpeech();
  private Map<String, DcMotor> motors = new HashMap<>();

  @Override
  public void runOpMode() {
    initTts();
    resolveMotors();

    waitForStart();

    forEachMotor(it -> {
      it.setMode(RunMode.RUN_USING_ENCODER);
      it.setPower(0.4);
    });
    sleep(500);
    forEachMotor(it -> it.setPower(0));

    int[] positions = motors.values().stream()
        .mapToInt(DcMotor::getCurrentPosition).toArray();
    IntSummaryStatistics positionStats = IntStream.of(positions).summaryStatistics();
    IntSummaryStatistics deviationStats = IntStream.of(positions)
        .map(it -> (int) Math.abs(it - positionStats.getAverage())).summaryStatistics();

    telemetry.addData("Positions", "min=%d max=%d avg=%.2f", positionStats.getMin(),
        positionStats.getMax(), positionStats.getAverage());
    telemetry.addData("Deviations", "min=%d max=%d avg=%.2f", deviationStats.getMin(),
        deviationStats.getMax(), deviationStats.getAverage());
    for (Map.Entry<String, DcMotor> entry : motors.entrySet()) {
      int pos = entry.getValue().getCurrentPosition(),
          deviation = (int) Math.abs(pos - positionStats.getAverage());

      if (pos == 0) {
        tts.speak(entry.getKey() + " is bad. Its current position is 0.");
        return;
      }

      telemetry.addData(entry.getKey(), "pos=%d deviation=%d", pos, deviation);
    }
    telemetry.update();

    tts.speak(String.format(Locale.US,
        "Drivetrain self-test succeeded. The average position recorded was %.2f, and the mean deviation was %.2f.",
        positionStats.getAverage(), deviationStats.getAverage()));

    while (!isStopRequested() && opModeIsActive()) {
      telemetry.addData("Press STOP", "to stop");
      telemetry.update();
    }
  }

  private void forEachMotor(Consumer<DcMotor> action) {
    for (DcMotor motor : motors.values()) {
      action.accept(motor);
    }
  }

  private void resolveMotors() {
    for (String motorName : kMotorNames) {
      motors.put(motorName, hardwareMap.dcMotor.get(motorName));
    }
  }

  private void initTts() {
    tts.initialize();
  }
}
