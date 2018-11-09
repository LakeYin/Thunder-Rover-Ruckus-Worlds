package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Optional;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class VuMarkDetector {
  public enum Target {
    BLUE_ROVER, RED_FOOTPRINT, FRONT_CRATERS, BACK_SPACE
  }

  private static final String VUFORIA_KEY = "AbMQqsf/////AAAAGaPkhxQD4kw5s9Z8fi7zmCkf8bWukMiWXj1fDay0ukQ99WGt7m6apGGxRWFIrlX1ZQhhW4w3L//I9eNMcxJo5tmJufAAL07zp128UEtHHGNCfz349+M36iiyjanscpBwgktOxCDbIuJdg/PwPWBsVSiwCpGgtOc8ly/VJgCVbAMg9LLWZkpi2ejrVr0taXybw6BejzHkv3MJ8nvWPVPHbVxtMYo3AWa6Sl2PoTgjd8/pKwpIcgpUaLStc92tfigl1i/ZXemq7tkTcWIJkODajW6XeFklq/6U7fKXUbh1qzaRhBa0xpITjbfAeZlzspLWE/y8r2FABSWbJnQZ0/Phvi2aHlY/o0N8M8OGu8fqqQiu";
  private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;

  private VuforiaLocalizer vuforia;
  private VuforiaTrackables trackables;

  public VuMarkDetector(HardwareMap hardwareMap) {
    initVuforia(hardwareMap);
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

  private void initVuforia(HardwareMap hardwareMap) {
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
        "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    parameters.vuforiaLicenseKey = VUFORIA_KEY;
    parameters.cameraDirection = CAMERA_CHOICE;
    vuforia = ClassFactory.getInstance().createVuforia(parameters);
  }

}
