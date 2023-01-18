package model;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;


public class EpicTask extends Task {
    List<Integer> listOfSubTusk = new ArrayList<>();
    Instant endTime;

    public EpicTask(String nameOfTask, String taskDescription, Status taskStatus, Instant startTime, Duration duration) {
        super(taskDescription, nameOfTask, taskStatus, startTime, duration );
        this.endTime = super.getEndTime();
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "nameOfTask='" + nameOfTask + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }

    public List<Integer> getListOfSubTask() {
        return listOfSubTusk;
    }

    public void setSubTaskId(int idOfSubTask) {
        listOfSubTusk.add(idOfSubTask);
    }
}




