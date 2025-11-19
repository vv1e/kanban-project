package edu.sdccd.cisc190.kanban.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class SaveManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path SAVE_FILE = Path.of(System.getProperty("user.home"), "kanban-save.json");

    // Generic save
    public static <T> void save(T data) throws IOException {
        FileWriter writer = new FileWriter(SAVE_FILE.toFile());
        gson.toJson(data, writer);
        writer.close();
    }

    // Generic load
    public static <T> T load(Class<T> type) throws IOException {
        File file = SAVE_FILE.toFile();
        if (!file.exists()) return null;

        FileReader reader = new FileReader(file);
        T data = gson.fromJson(reader, type);
        reader.close();
        return data;
    }

    public static boolean saveExists() {
        return SAVE_FILE.toFile().exists();
    }
}
