package service;

public class Manager {
    public static TaskManager getDefaultInMemoryTaskManager(HistoryManager historyManager) {
        return new InMemoryTaskManager();
    }

        public static HistoryManager getDefaultHistory() {
            return new InMemoryHistoryManager();
        }
    }

