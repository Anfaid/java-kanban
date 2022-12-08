package service;

import model.Task;
import org.w3c.dom.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public List<Task> getHistory() {
       return getTasks();
    }

    @Override
    public void addLast(Task task) {
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        if (first == null)
            first = newNode;
        else
            l.next = newNode;
        last = newNode;

        removeNode(nodeMap.get(task.getTaskId()));
        nodeMap.put(task.getTaskId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        for (Node x = first; x != null; x = x.next) {
            taskList.add(x.task);
        }
        return taskList;

    }

    private void removeNode(Node n) {
        if (n == null) {
            return;
        }
        nodeMap.remove(n.task.getTaskId());
        final Node next = n.next;
        final Node prev = n.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            n.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            n.next = null;
        }
    }

    @Override
    public void remove(int id) {
        removeNode(nodeMap.get(id));

    }

    static class Node {
        Task task;
        Node next;
        Node prev;


        public Node(Node prev, Task task, Node next) {
            this.next = next;
            this.task = task;
            this.prev = prev;
        }
    }
}


