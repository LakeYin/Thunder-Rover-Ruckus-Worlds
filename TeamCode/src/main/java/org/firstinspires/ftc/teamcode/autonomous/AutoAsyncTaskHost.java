package org.firstinspires.ftc.teamcode.autonomous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AutoAsyncTaskHost {
  public final ExecutorService executor = Executors.newSingleThreadExecutor();
  private Future<?> future;

  public void submit(Runnable task) {
    future = executor.submit(task);
  }

  public boolean isIdle() {
    return future == null || future.isDone();
  }
}
