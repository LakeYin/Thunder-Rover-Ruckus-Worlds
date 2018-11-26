package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
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

  private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
  private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
  private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

  private VuforiaLocalizer localizer;
  private TFObjectDetector detector;

  public MineralDetector(HardwareMap hardware) {
    localizer = VuforiaProvider.getLocalizer(hardware);
    initTensorFlow(hardware);
  }

  public void activate() {
    detector.activate();
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

  private void initTensorFlow(HardwareMap hardwareMap) {
    int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
        "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

    detector = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, localizer);
    detector.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
  }
}
