package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

@Autonomous(name = "TTS Demo", group = "ARC Lightning")
public class TtsDemo extends LinearOpMode {

  private static final String[] SPEECH = {
      "Comments are not like Schindler’s List. They are not “pure good.” Indeed, comments are, at best, a necessary evil.",
      "If our programming languages were expressive enough, or if we had the talent to subtly wield those languages to express our intent, we would not need\n"
          + "comments very much—perhaps not at all.",
      "The proper use of comments is to compensate for our failure to express ourself in code.",
      "Note that I used the word failure. I meant it. Comments are always failures.",
      "We must have them because we cannot always figure out how to express ourselves without them, but their use is not a cause for celebration.",
      "So when you find yourself in a position where you need to write a comment, think it through and see whether there isn’t some way to turn the tables and express yourself in code.",
      "Every time you express yourself in code, you should pat yourself on the back.",
      "Every time you write a comment, you should grimace and feel the failure of your ability of expression."
  };
  private static final String[] LANGUAGES = {
      "eng", "spa", "fra", "ger", "rus", "jpn"
  };

  @Override
  public void runOpMode() {
    AndroidTextToSpeech tts = new AndroidTextToSpeech();
    tts.initialize();

    tts.setSpeechRate(0.4f);
    tts.speak("Ready to begin");
    while (tts.isSpeaking()) {
      ;
    }

    waitForStart();

    for (int i = 0; i < SPEECH.length; ++i) {
      tts.setPitch((float) Math.random() * 2f + 0.25f);
      tts.setSpeechRate((float) Math.random() + 0.5f);
      tts.setLanguage(LANGUAGES[i % LANGUAGES.length]);

      tts.speak(SPEECH[i]);
      while (tts.isSpeaking()) {
        ;
      }
    }
  }
}
