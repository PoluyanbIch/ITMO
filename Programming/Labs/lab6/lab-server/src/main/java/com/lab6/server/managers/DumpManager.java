package com.lab6.server.managers;

import com.lab6.common.models.Worker;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.Server;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Stack;


/**
 * Класс, управляющий сохранением и загрузкой коллекции музыкальных групп.
 */
public class DumpManager {
    private final String filePath;
    private static DumpManager instance;

    /**
     * Конструктор для создания объекта DumpManager.
     */
    private DumpManager() {
        this.filePath = System.getenv("LAB5_FILE_PATH");
        // Проверка наличия и корректности переменной окружения LAB5_FILE_PATH
        if (filePath == null) {
            Server.logger.severe("Environment variable LAB5_FILE_PATH not found!");
            System.exit(1);
        } else if (filePath.isEmpty()) {
            Server.logger.severe("Environment variable LAB5_FILE_PATH does not contain a file path!");
            System.exit(1);
        } else if (!filePath.endsWith(".csv")) {
            Server.logger.severe("The file must be in .csv format!");
            System.exit(1);
        } else if (!new File(filePath).exists()) {
            Server.logger.severe("The file at the specified path was not found!");
            System.exit(1);
        }
    }

    public static DumpManager getInstance() {
        if (instance == null) {
            instance = new DumpManager();
        }
        return instance;
    }

    /**
     * Сохраняет коллекцию  в файл.
     * @param collection коллекция
     */
    public ExecutionStatus WriteCollection(Stack<Worker> collection) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));
            for (Worker band : collection) {
                writer.writeNext(Worker.toArray(band));
            }
            writer.close();
            return new ExecutionStatus(true, "Коллекция успешно сохранена в файл!");
        } catch (IOException e) {
            return new ExecutionStatus(false, "Произошла ошибка при записи коллекции в файл!");
        }
    }
    /**
     * Загружает коллекцию из файла.
     * @param collection коллекция
     */
    public ExecutionStatus ReadCollection(Stack<Worker> collection) {
        InputStreamReader input;
        try {
            input = new InputStreamReader(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (CSVReader reader = new CSVReader(input)) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                Worker buf = Worker.fromArray(line);
                if (buf != null && buf.validate()) {
                    collection.push(buf);
                } else {
                    return new ExecutionStatus(false, "Введены некорректные данные элемента коллекции!");
                }
            }
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при чтении коллекции из файла!");
        }
        return new ExecutionStatus(true, "Коллекция успешно загружена!");
    }
}
