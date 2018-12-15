package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

import android.hardware.camera2.CameraManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.CameraDevice;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

class VuforiaProvider {

  private static final String VUFORIA_KEY = "AbMQqsf/////AAAAGaPkhxQD4kw5s9Z8fi7zmCkf8bWukMiWXj1fDay0ukQ99WGt7m6apGGxRWFIrlX1ZQhhW4w3L//I9eNMcxJo5tmJufAAL07zp128UEtHHGNCfz349+M36iiyjanscpBwgktOxCDbIuJdg/PwPWBsVSiwCpGgtOc8ly/VJgCVbAMg9LLWZkpi2ejrVr0taXybw6BejzHkv3MJ8nvWPVPHbVxtMYo3AWa6Sl2PoTgjd8/pKwpIcgpUaLStc92tfigl1i/ZXemq7tkTcWIJkODajW6XeFklq/6U7fKXUbh1qzaRhBa0xpITjbfAeZlzspLWE/y8r2FABSWbJnQZ0/Phvi2aHlY/o0N8M8OGu8fqqQiu";
  private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;

  private static VuforiaLocalizer localizer = null;
  private static CameraManager cameraManager;

  static VuforiaLocalizer getLocalizer(HardwareMap hardwareMap) {
    if (localizer == null) {
      int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
          "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
      VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
      parameters.vuforiaLicenseKey = VUFORIA_KEY;
      parameters.cameraDirection = CAMERA_CHOICE;
      localizer = ClassFactory.getInstance().createVuforia(parameters);
    }
    if (cameraManager == null) {
      cameraManager = hardwareMap.appContext.getSystemService(CameraManager.class);
    }
    return localizer;
  }

  static void setFrontFlashlight(boolean lit) {
    try {
      CameraDevice.getInstance().setFlashTorchMode(lit);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
