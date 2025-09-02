package com.lab6.server.managers;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import com.lab6.common.models.Worker;
import com.lab6.common.models.Position;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Класс, управляющий коллекцией
 */
public class CollectionManager {
    private static volatile CollectionManager instance;
    private Long id = 1L;
    private final DumpManager dumpManager = DumpManager.getInstance();
    private final Map<Long, Worker> Bands = new HashMap<>();
    private Stack<Worker> collection = new Stack<>();
    private LocalDateTime InitializationDate;
    private LocalDateTime lastSaveDate;

    /**
     * Конструктор класса CollectionManager.
     */
    private CollectionManager() { }

    /**
     * Возвращает единственный экземпляр CollectionManager.
     * @return Экземпляр CollectionManager.
     */
    public static CollectionManager getInstance() {
        if (instance == null) {
            synchronized (CollectionManager.class) {
                if (instance == null) {
                    instance = new CollectionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Возвращает коллекцию
     * @return Коллекция
     */
    public Stack<Worker> getBands() {
        return collection;
    }

    /**
     * Сортирует коллекцию
     */
    public void sort() {
        collection = collection.stream()
                .sorted(Comparator.comparing(Worker::getId))
                .collect(Collectors.toCollection(Stack::new));
    }

    /**
     * Удаляет первую
     */
    public void removeFirst() {
        if (!collection.isEmpty()) {
            Bands.remove(collection.firstElement().getId());
            collection.remove(0);
        }
    }

    /**
     * Возвращает свободный идентификатор.
     * @return Свободный идентификатор.
     */
    public Long getFreeId() {
        while (Bands.containsKey(id)) {
            id++;
        }
        return id;
    }

    /**
     * Возвращает дату инициализации коллекции.
     * @return Дата инициализации коллекции.
     */
    public LocalDateTime getInitializationDate() {
        return InitializationDate;
    }

    /**
     * Возвращает дату последнего сохранения коллекции.
     * @return Дата последнего сохранения коллекции.
     */
    public LocalDateTime getLastSaveDate() {
        return lastSaveDate;
    }

    /**
     * Возвращает коллекцию
     * @return Коллекция
     */
    public Stack<Worker> getCollection() {
        return collection;
    }

    /**
     * Возвращает  по идентификатору.
     * @param id Идентификатор
     * @return  с указанным идентификатором.
     */
    public Worker getById(Long id) {
        return Bands.get(id);
    }

    /**
     * Удаляет все Worker с указанным позициями.
     * @param position Позиция для удаления.
     * @return Количество удалённых
     */
    public int removeAllByPosition(Position position) {
        int initialSize = collection.size();
        collection = collection.stream()
                .filter(band -> !band.getPosition().equals(position))
                .collect(Collectors.toCollection(Stack::new));
        Bands.entrySet().removeIf(entry -> entry.getValue().getPosition().equals(position));
        return initialSize - collection.size();
    }

    /**
     * Сохраняет коллекцию .
     */
    public ExecutionStatus saveCollection() {
        ExecutionStatus savingStatus = dumpManager.WriteCollection(collection);
        if (savingStatus.isSuccess()){
            lastSaveDate = LocalDateTime.now();
        }
        return savingStatus;
    }

    /**
     * Загружает коллекцию.
     * @return Статус выполнения.
     */
    public ExecutionStatus loadCollection() {
        collection.clear();
        Bands.clear();
        ExecutionStatus loadStatus = dumpManager.ReadCollection(collection);
        if (loadStatus.isSuccess()) {
            InitializationDate = LocalDateTime.now();
            lastSaveDate = LocalDateTime.now();

            boolean hasDuplicates = collection.stream()
                    .anyMatch(band -> Bands.putIfAbsent(band.getId(), band) != null);

            if (hasDuplicates) {
                return new ExecutionStatus(false, "Ошибка загрузки коллекции: обнаружены дубликаты id!");
            }

            return loadStatus;
        }
        else {
            return loadStatus;
        }
    }

    /**
     * Очищает коллекцию
     */
    public void clear() {
        collection.clear();
        Bands.clear();
    }

    /**
     * Добавляет в коллекцию.
     * @param worker для добавления.
     * @return true, если успешно добавлена, иначе false.
     */
    public boolean add(Worker worker) {
        if ((worker != null) && worker.validate() && !Bands.containsKey(worker.getId())) {
            collection.push(worker);
            Bands.put(worker.getId(), worker);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Удаляет по идентификатору.
     * @param elementId Идентификатор для удаления.
     */
    public void removeById(Long elementId) {
        collection = collection.stream()
                .filter(band -> !band.getId().equals(elementId))
                .collect(Collectors.toCollection(Stack::new));
        Bands.remove(elementId);
    }
}
