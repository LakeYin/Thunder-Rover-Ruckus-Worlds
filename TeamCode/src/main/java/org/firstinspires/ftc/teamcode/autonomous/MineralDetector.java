package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.Vuforia;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class MineralDetector {

  public enum GoldPosition {
    LEFT, CENTER, RIGHT
  }

  public enum Mineral {
    SILVER(LABEL_SILVER_MINERAL), GOLD(LABEL_GOLD_MINERAL);

    private final String label;

    static Mineral byLabel(String label) {

      for (Mineral min : Mineral.values()) {
        if (min.label.equals(label)) {
          return min;
        }
      }

      throw new IllegalArgumentException("Invalid mineral label: " + label);
    }

    Mineral(String label) {
      this.label = label;
    }
  }

  private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
  private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
  private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

  private VuforiaLocalizer localizer;
  private TFObjectDetector detector;

  public MineralDetector(HardwareMap hardware) {
    localizer = VuforiaManager.getLocalizer();
    initTensorFlowIfNeeded(hardware);
  }

  public void activate() {
    detector.activate();
  }

  public Optional<Mineral> currentRecognition() {
    return detector.getRecognitions().stream()
        .findFirst()
        .map(Recognition::getLabel)
        .map(Mineral::byLabel);
  }

  public Optional<Mineral> bottomCenterRecognition() {
    return detector.getRecognitions().stream()
        .max(Comparator.comparingDouble(Recognition::getBottom))
        .map(Recognition::getLabel)
        .map(Mineral::byLabel);
  }

  public Optional<Mineral> rightmostRecognition() {
    return detector.getRecognitions()
        .stream()
        .max((r1, r2) -> Float.compare(r1.getLeft(), r2.getLeft()))
        .map(Recognition::getLabel)
        .map(Mineral::byLabel);
  }

  public Optional<Mineral> leftmostRecognition() {
    return detector.getRecognitions()
            .stream()
            .min((r1, r2) -> Float.compare(r1.getLeft(), r2.getLeft()))
            .map(Recognition::getLabel)
            .map(Mineral::byLabel);
  }

  public Optional<GoldPosition> goldPosition() {
    if (!isTrackingEnoughObjects()) {
      return Optional.empty();
    }

    List<Recognition> sortedRecs = detector.getRecognitions()
        .stream()
        .sorted((r1, r2) -> Float.compare(r1.getLeft(), r2.getLeft()))
        .collect(Collectors.toList());

    return IntStream.range(0, sortedRecs.size())
        .filter(index -> sortedRecs.get(index).getLabel().equals(LABEL_GOLD_MINERAL))
        .boxed()
        .findFirst()
        .map(index -> GoldPosition.values()[
            Range.clip(index, 0, GoldPosition.values().length - 1)]);
  }

  public boolean isTrackingEnoughObjects() {
    return detector.getRecognitions().size() >= GoldPosition.values().length;
  }

  public void shutdown() {
    detector.shutdown();
  }

  private void initTensorFlowIfNeeded(HardwareMap hardwareMap) {
    int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
        "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

    detector = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, localizer);
    detector.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);

  }
}
