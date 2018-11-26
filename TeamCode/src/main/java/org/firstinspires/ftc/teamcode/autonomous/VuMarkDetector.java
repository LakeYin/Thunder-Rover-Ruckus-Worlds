package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Optional;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class VuMarkDetector {
  public enum Target {
    BLUE_ROVER, RED_FOOTPRINT, FRONT_CRATERS, BACK_SPACE
  }

  private VuforiaLocalizer vuforia;
  private VuforiaTrackables trackables;

  public VuMarkDetector(HardwareMap hardwareMap) {
    vuforia = VuforiaProvider.getLocalizer(hardwareMap);
    loadTrackables();
  }

  public void activate() {
    trackables.activate();
  }

  public Optional<Target> visibleTarget() {
    for (VuforiaTrackable trackable : trackables) {
      if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
        return Optional.of(Target.valueOf(trackable.getName()));
      }
    }
    return Optional.empty();
  }

  public void deactivate() {
    trackables.deactivate();
  }

  private void loadTrackables() {
    trackables = this.vuforia.loadTrackablesFromAsset("RoverRuckus");

    for (int i = 0; i < Target.values().length; ++i) {
      trackables.get(i).setName(Target.values()[i].name());
    }
  }
}
