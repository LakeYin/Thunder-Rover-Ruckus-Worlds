package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.Vuforia;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;

class VuforiaManager {

  private static final String VUFORIA_KEY = "AbMQqsf/////AAAAGaPkhxQD4kw5s9Z8fi7zmCkf8bWukMiWXj1fDay0ukQ99WGt7m6apGGxRWFIrlX1ZQhhW4w3L//I9eNMcxJo5tmJufAAL07zp128UEtHHGNCfz349+M36iiyjanscpBwgktOxCDbIuJdg/PwPWBsVSiwCpGgtOc8ly/VJgCVbAMg9LLWZkpi2ejrVr0taXybw6BejzHkv3MJ8nvWPVPHbVxtMYo3AWa6Sl2PoTgjd8/pKwpIcgpUaLStc92tfigl1i/ZXemq7tkTcWIJkODajW6XeFklq/6U7fKXUbh1qzaRhBa0xpITjbfAeZlzspLWE/y8r2FABSWbJnQZ0/Phvi2aHlY/o0N8M8OGu8fqqQiu";
  private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;

  public static VuforiaLocalizer getVuMarkLocalizer(HardwareMap map) {
    int cameraMonitorViewId = map.appContext.getResources().getIdentifier(
        "cameraMonitorViewId", "id", map.appContext.getPackageName());
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    return getVuforiaLocalizerWithParams(parameters);
  }

  public static VuforiaLocalizer getMineralLocalizer(HardwareMap map) {
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
    return getVuforiaLocalizerWithParams(parameters);
  }

  public static void deinitVuforia() {
    Vuforia.deinit();
  }

  private static VuforiaLocalizer getVuforiaLocalizerWithParams(Parameters parameters) {
    parameters.vuforiaLicenseKey = VUFORIA_KEY;
    parameters.cameraDirection = CAMERA_CHOICE;
    return ClassFactory.getInstance().createVuforia(parameters);
  }

  static void setFrontFlashlight(boolean lit) {
    try {
      //CameraDevice.getInstance().setFlashTorchMode(lit);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
