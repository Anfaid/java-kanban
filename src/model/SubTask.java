package model;

import java.util.Objects;

public class SubTask extends Task {
    Integer idOfEpicTask;

    public SubTask(Integer taskId, String nameOfTask, String taskDescription, Status taskStatus, Integer idOfEpicTask) {
        super(taskId, nameOfTask, taskDescription, taskStatus);
        this.idOfEpicTask = idOfEpicTask;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "idOfEpicTask=" + idOfEpicTask +
                ", nameOfTask='" + nameOfTask + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }




    public int getIdOfEpicTask() {
        return idOfEpicTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(idOfEpicTask, subTask.idOfEpicTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idOfEpicTask);
    }
}
