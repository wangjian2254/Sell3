package com.wj.sell3.eve.task;
import android.util.Log;
import java.util.Observable;
import java.util.Observer;

public class TaskManager extends Observable
{
  public static final Integer CANCEL_ALL = Integer.valueOf(1);
  private static final String TAG = "TaskManager";

  public void addTask(Observer paramObserver)
  {
    super.addObserver(paramObserver);
  }

  public void cancelAll()
  {
    Log.d("TaskManager", "All task ready to Cancel.");
    setChanged();
    notifyObservers(CANCEL_ALL);
  }

  public void deleteTask(Observer paramObserver)
  {
    super.deleteObserver(paramObserver);
  }
}
