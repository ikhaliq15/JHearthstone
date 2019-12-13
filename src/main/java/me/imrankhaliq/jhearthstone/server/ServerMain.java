package me.imrankhaliq.jhearthstone.server;

import me.imrankhaliq.jhearthstone.server.config.Config;
import me.imrankhaliq.jhearthstone.server.config.ConfigUtils;

public class ServerMain {
    public static void main(String args[]) {
        if (!ConfigUtils.configFileExists()) {
            ConfigUtils.writeConfigFile(ConfigUtils.getDefaultConfigSettings());
        }
        Config serverConfig = ConfigUtils.readConfigFile();

        System.out.println("Starting server connection on: " + serverConfig.getPort());
        ConnectionManager server = new ConnectionManager(serverConfig.getPort());
    }
}
