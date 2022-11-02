package model;

import java.util.HashMap;

public class EpicTask extends Task {
    HashMap<Integer, SubTask> mapOfSubTusk = new HashMap<>();

    public EpicTask(Integer taskId, String nameOfTask, String taskDescription, Status taskStatus) {
        super(taskId, taskDescription, nameOfTask, taskStatus);
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

    public HashMap<Integer, SubTask> getMapOfSubTusk() {
        return mapOfSubTusk;
    }

    public void setMapOfSubTusk(HashMap<Integer, SubTask> mapOfSubTusk ) {
        this.mapOfSubTusk = mapOfSubTusk;
    }
}




