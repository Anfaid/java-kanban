package model;

import java.util.List;
import java.util.ArrayList;


public class EpicTask extends Task {
    List<Integer> listOfSubTusk = new ArrayList<>();

    public EpicTask(String nameOfTask, String taskDescription, Status taskStatus) {
        super(taskDescription, nameOfTask, taskStatus);
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




