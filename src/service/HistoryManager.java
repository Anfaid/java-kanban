package service;

import model.Task;

import java.util.LinkedList;


public interface HistoryManager {
    public LinkedList<Task> getHistory();

    public void addTask(Task task);
}
