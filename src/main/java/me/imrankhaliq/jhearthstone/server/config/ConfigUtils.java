package me.imrankhaliq.jhearthstone.server.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.imrankhaliq.jhearthstone.shared.FileUtils;

import java.io.File;

public class ConfigUtils {

    public static final String DEFAULT_CONFIG_LOCATION = "server.config";

    public static boolean configFileExists() {
        File configFile = new File(DEFAULT_CONFIG_LOCATION);
        return configFile.exists() && configFile.isFile();
    }

    public static Config getDefaultConfigSettings(){
        JsonObject defaultConfig = new JsonObject();
        defaultConfig.addProperty("port", 8787);
        return new Config(defaultConfig);
    }

    public static void writeConfigFile(Config config) {
        FileUtils.writeFile(DEFAULT_CONFIG_LOCATION, config.toJSONString());
    }

    public static Config readConfigFile() {
        String configString = FileUtils.readFile(DEFAULT_CONFIG_LOCATION);
        JsonObject configJSON = new Gson().fromJson(configString, JsonObject.class);
        return new Config(configJSON);
    }
}
