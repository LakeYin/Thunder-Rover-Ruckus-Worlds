package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

public class ControlMapper {

  private IControlMode
      modeForGamepad1 = ControlMode.DRIVE,
      modeForGamepad2 = ControlMode.LEFT_SLIDE;
  private AndroidTextToSpeech tts;

  public ControlMapper() {
    tts = new AndroidTextToSpeech();
    tts.initialize();
    tts.setSpeechRate(1.5f);
  }

  public void applyGamepadInputs(Gamepad gamepad1, Gamepad gamepad2) {
    modeForGamepad1.apply(gamepad1);
    modeForGamepad2.apply(gamepad2);

    activateModes(gamepad1, gamepad2);
  }

  public void activateModes(Gamepad gamepad1, Gamepad gamepad2) {
    for (ControlMode mode : ControlMode.values()) {
      if (mode.shouldBeActivated(gamepad1) && modeForGamepad2 != mode) {
        announceModeActivation(mode, "Gamepad 1");
        modeForGamepad1 = mode;
      } else if (mode.shouldBeActivated(gamepad2) && modeForGamepad1 != mode) {
        announceModeActivation(mode, "Gamepad 2");
        modeForGamepad2 = mode;
      }
    }
  }

  public void cheer() {
    if (!tts.isSpeaking())
      tts.speak(String.valueOf(Math.random()));
  }

  private void announceModeActivation(ControlMode mode, String gamepad) {
    tts.speak(gamepad + " set to " + mode.name().replace('_', ' '));
  }

  public IControlMode getModeForGamepad1() {
    return modeForGamepad1;
  }

  public IControlMode getModeForGamepad2() {
    return modeForGamepad2;
  }

  public void setModeForGamepad1(IControlMode modeForGamepad1) {
    this.modeForGamepad1 = modeForGamepad1;
  }

  public void setModeForGamepad2(IControlMode modeForGamepad2) {
    this.modeForGamepad2 = modeForGamepad2;
  }
}
