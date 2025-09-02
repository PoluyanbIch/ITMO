package managers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import models.Worker;

public class CollectionManager {
    private int currentId = 1;
    private HashMap<Integer, Worker> workers = new HashMap<>();
    private Set<Worker> collection = new HashSet<>();
    private LocalDateTime lastChangeTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;

    public CollectionManager(DumpManager dumpManager) {
        this.lastChangeTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;
    }

    public LocalDateTime getLastInitTime() {
        return lastChangeTime;
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public Set<Worker> getCollection() {
        return collection;
    }

    public Worker getById(int id) {
        return workers.get(id);
    }

    public boolean isContains(Worker w) {
        return getById(w.getId()) != null;
    }

    public int getFreeId() {
        currentId++;
        return currentId;
    }

    public boolean add(Worker w) {
        workers.put(w.getId(), w);
        collection.add(w);
        lastChangeTime = LocalDateTime.now();
        return true;
    }

    public boolean update(Worker w) {
        if (!isContains(w)) return false;
        collection.remove(workers.get(w.getId()));
        workers.put(w.getId(), w);
        collection.add(w);
        lastChangeTime = LocalDateTime.now();
        return true;
    }

    public boolean remove(int id) {
        Worker w = getById(id);
        if (w == null) return false;
        workers.remove(id);
        collection.remove(w);
        return true;
    }

    public boolean init() {
        collection.clear();
        workers.clear();
        collection = (Set<Worker>) dumpManager.readCollection();
        lastChangeTime = LocalDateTime.now();
        return true;
        /*for (Worker w : collection) {
            if (getById(w.getId()) != null) {
                collection.clear();
                workers.clear();
                return false;
            } else {
                if (w.getId() > currentId) currentId = w.getId();
                workers.put(w.getId(), w);
            }
        }
        return true;*/
    }


    public void clear() {
        collection.clear();
        workers.clear();
    }

    public void saveCollection() {
        dumpManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста";
        StringBuilder info = new StringBuilder();
        for (Worker worker : collection) {
            info.append(worker.toString()+"\n\n");
        }
        return info.toString().trim();
    }
}