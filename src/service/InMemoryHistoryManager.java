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
        return null;
    }

    @Override
    public void addLast(Task task) {
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        if (nodeMap.containsKey(task.getTaskId()))
            removeNode(nodeMap.get(task.getTaskId()));
        else
            nodeMap.put(task.getTaskId(), newNode);


    }

    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        for (Node x = first; x != null; x = x.next) {
            taskList.add(x.tasks);
        }
        return taskList;

    }

    private void removeNode(Node n) {
        nodeMap.remove(n.tasks.getTaskId());
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

    }

    static class Node {
        Task tasks;
        Node next;
        Node prev;


        public Node(Node prev, Task task, Node next) {
            this.next = next;
            this.tasks = task;
            this.prev = prev;
        }
    }
}


