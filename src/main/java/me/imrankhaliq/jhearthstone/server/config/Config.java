package me.imrankhaliq.jhearthstone.server.config;


import com.google.gson.JsonObject;

public class Config {

    private JsonObject jsonConfig;
    private int mPort;

    public Config() {
        this.jsonConfig = new JsonObject();
        this.setPort(0);
    }

    public Config(JsonObject config) {
        this.jsonConfig = config;
        this.mPort = config.get("port").getAsInt();
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int mPort) {
        this.jsonConfig.addProperty("port", mPort);
        this.mPort = mPort;
    }

    public String toJSONString() {
        return this.jsonConfig.toString();
    }
}
