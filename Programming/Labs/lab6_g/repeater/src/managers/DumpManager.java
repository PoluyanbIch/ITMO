package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;
//import models.Worker;
import utility.Console;
//import utility.LocalDateTimeAdapter;
//import utility.ZonedDateTimeAdapter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

public class DumpManager {
    private Properties properties = new Properties();
    private final Gson gson = new GsonBuilder()
    .setPrettyPrinting()
    .serializeNulls()
//    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//    .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
    .create();

    private final String dumpFilePath;
    private final Console console;

    public DumpManager(String dumpFilePath, Console console) {
        this.dumpFilePath = dumpFilePath;
        this.console = console;
    }

//    public void writeCollection(Collection<Worker> collection) {
//        try (FileWriter writer = new FileWriter(dumpFilePath)) {
//            String json = gson.toJson(collection);
//            writer.write(json);
//            console.println("Collection saved to " + dumpFilePath);
//        } catch (Exception e) {
//            console.printErr("Error saving collection to " + dumpFilePath + "\n");
//            e.printStackTrace();
//        }
//    }
//
//    public Collection<Worker> readCollection() {
//        if (dumpFilePath != null && !dumpFilePath.isEmpty()) {
//            try {
//                var fileReader = new FileReader(dumpFilePath);
//                var reader = new BufferedReader(fileReader);
//                var jsonString = new StringBuilder();
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    line = line.trim();
//                    if (!line.equals("")) {
//                        jsonString.append(line);
//                    }
//                }
//                if (jsonString.length() == 0) {
//                    jsonString = new StringBuilder("[]");
//                }
//
//                HashSet<Worker> workers = gson.fromJson(jsonString.toString(), new TypeToken<HashSet<Worker>>() {}.getType());
//                return workers;
//            } catch (Exception e) {
//                console.printErr("Ошибка");
//                console.printErr(e);
//            }
//            return new HashSet<>();
//        }
//        return null;
//    }
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
