package me.imrankhaliq.jhearthstone.server;

import me.imrankhaliq.jhearthstone.server.config.Config;
import me.imrankhaliq.jhearthstone.server.config.ConfigUtils;

public class ServerMain {
    public static void main(String args[]) {
        if (!ConfigUtils.configFileExists()) {
            ConfigUtils.writeConfigFile(ConfigUtils.getDefaultConfigSettings());
        }
        Config serverConfig = ConfigUtils.readConfigFile();
    }
}
