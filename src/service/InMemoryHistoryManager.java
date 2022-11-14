package service;

import model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static final int LIMIT_OF_TASK = 10;
    private final LinkedList<Task> historyList = new LinkedList<>();

    @Override
    public void addTask(Task task) {
        if (task != null) {
            if (!(historyList.size() >= LIMIT_OF_TASK)) {
                historyList.add(task);
            } else if (historyList.size() > LIMIT_OF_TASK) {
                historyList.removeFirst();
                historyList.add(task);
            }

        }
        historyList.add(task);
    }

    @Override
    public LinkedList<Task> getHistory() {
        return historyList;
    }

}
