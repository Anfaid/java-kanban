package model;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class SubTask extends Task {
    Integer idOfEpicTask;

    public SubTask(String nameOfTask, String taskDescription, Status taskStatus, Integer idOfEpicTask, Duration duration, Instant startTime) {
        super(nameOfTask, taskDescription, taskStatus,duration, startTime);
        this.idOfEpicTask = idOfEpicTask;
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



    public void setIdOfEpicTask(Integer idOfEpicTask) {
        this.idOfEpicTask = idOfEpicTask;
    }

    @Override
    public String toString() {
        return "SubTask{}";
    }
}
