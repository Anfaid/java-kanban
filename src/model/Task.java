package model;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Task {
    String nameOfTask;
    ;
    int taskId;
    String taskDescription;
    Status taskStatus; //DONE, IN_PROGRESS, NEW
    Duration duration;
    Instant startTime;

    public Task(String nameOfTask, String taskDescription, Status taskStatus, Duration duration, Instant startTime) {
        this.nameOfTask = nameOfTask;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getName() {
        return nameOfTask;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setTaskDescription(String description) {
        this.taskDescription = taskDescription;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && Objects.equals(nameOfTask, task.nameOfTask) && Objects.equals(taskDescription, task.taskDescription) && taskStatus == task.taskStatus && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfTask, taskId, taskDescription, taskStatus, duration, startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "nameOfTask='" + nameOfTask + '\'' +
                ", taskId=" + taskId +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}



