package model;

import java.util.Objects;

public class Task {
    String nameOfTask;
    String taskDescription;
    Integer taskId;
    Status taskStatus; //DONE, IN_PROGRESS, NEW

    public Task(Integer taskId, String nameOfTask, String taskDescription, Status taskStatus) {
        this.taskId = taskId;
        this.nameOfTask = nameOfTask;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }


    @Override
    public String toString() {
        return "Task{" +
                "nameOfTask='" + nameOfTask + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(nameOfTask, task.nameOfTask)
                && Objects.equals(taskDescription, task.taskDescription)
                && Objects.equals(taskId, task.taskId)
                && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfTask, taskDescription, taskId, taskStatus);
    }
}

