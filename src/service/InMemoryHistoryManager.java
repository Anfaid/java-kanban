package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static final int LIMIT_OF_TASK = 10;
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (task != null) {
            if (!(historyList.size() >= LIMIT_OF_TASK)) {
                historyList.add(task);
            } else {
                historyList.remove(0);
                historyList.add(task);
            }

        }

    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

}
