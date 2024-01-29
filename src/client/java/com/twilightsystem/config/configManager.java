package com.twilightsystem.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class configManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_PATH = "config/autoreconnect.json";

    public static config loadConfig() {
        try {
            File configFile = new File(CONFIG_FILE_PATH);

            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();

                config defaultConfig = new config();
                saveConfig(defaultConfig);

                return defaultConfig;
            }

            try (FileReader reader = new FileReader(configFile)) {
                return GSON.fromJson(reader, config.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new config();
        }
    }

    public static void saveConfig(config config) {
        try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
