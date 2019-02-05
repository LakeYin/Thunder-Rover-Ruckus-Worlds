package org.firstinspires.ftc.teamcode.selfcheck.trials;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.selfcheck.Trial;

public class DrivetrainEncoderTrial implements Trial {
  private static final String[] kMotorNames = {
      "motorFL", "motorFR", "motorBL", "motorBR"
  };
  private Map<String, DcMotor> motors = new HashMap<>();

  @Override
  public void addPrerequisites(Telemetry telemetry) {
    telemetry.addData("Drivetrain moves", "Ensure movement space or stand");
  }

  @Override
  public boolean runTrial(Telemetry telemetry, LinearOpMode opMode) {
    resolveMotors(opMode.hardwareMap);

    forEachMotor(it -> {
      it.setMode(RunMode.STOP_AND_RESET_ENCODER);
      it.setMode(RunMode.RUN_USING_ENCODER);
      it.setPower(0.35);
    });

    opMode.sleep(500);
    forEachMotor(it -> it.setPower(0));

    return reportDeviations(telemetry);
  }

  private void resolveMotors(HardwareMap hardware) {
    for (String motorName : kMotorNames) {
      motors.put(motorName, hardware.dcMotor.get(motorName));
    }
  }

  private void forEachMotor(Consumer<DcMotor> action) {
    for (DcMotor motor : motors.values()) {
      action.accept(motor);
    }
  }

  private boolean reportDeviations(Telemetry telemetry) {
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
        telemetry.addData("DRIVE ENCODER FAIL",
            entry.getKey() + " has encoder position 0");
        return false;
      }

      telemetry.addData(entry.getKey(), "pos=%d deviation=%d", pos, deviation);
    }
    return true;
  }
}
