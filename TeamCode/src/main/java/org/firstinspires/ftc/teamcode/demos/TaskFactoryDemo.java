package org.firstinspires.ftc.teamcode.demos;

import com.andoverrobotics.core.drivetrain.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import java.io.FileNotFoundException;
import org.firstinspires.ftc.teamcode.autonomous.tasks.TaskFactory;

@Autonomous(name = "TaskFactory Demo", group = "ARC Lightning")
public class TaskFactoryDemo extends LinearOpMode {
  private static final String TASK_SCRIPT_PATH = "/storage/self/primary/FIRST/config/taskFactoryDemo.task";

  private TankDrive driveTrain;

  @Override
  public void runOpMode() {
    DcMotor motorR = hardwareMap.dcMotor.get("motorR");
    DcMotor motorL = hardwareMap.dcMotor.get("motorL");
    motorR.setDirection(Direction.REVERSE);

    driveTrain = TankDrive.fromMotors(
        motorL,
        motorR,
        this,
        (int)Math.round(1440.0 / 4 / Math.PI),
        1440 * 2
    );
    driveTrain.setDefaultDrivePower(1.0);

    TaskFactory theFactory = new TaskFactory(driveTrain);
    try {

      String[] commands = theFactory.commandsInFile(TASK_SCRIPT_PATH);

      waitForStart();

      for (String command : commands) {
        Runnable task = theFactory.parseTask(command);
        telemetry.addData("Now running", command);
        telemetry.update();
        task.run();
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }


  }
}
