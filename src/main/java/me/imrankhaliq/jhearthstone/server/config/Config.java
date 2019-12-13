package me.imrankhaliq.jhearthstone.server.config;


import com.google.gson.JsonObject;

public class Config {

    private JsonObject mJsonConfig;
    private int mPort;

    public Config() {
        this.mJsonConfig = new JsonObject();
        this.setPort(0);
    }

    public Config(JsonObject config) {
        this.mJsonConfig = config;
        this.mPort = config.get("port").getAsInt();
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int mPort) {
        this.mJsonConfig.addProperty("port", mPort);
        this.mPort = mPort;
    }

    public String toJSONString() {
        return this.mJsonConfig.toString();
    }
}
