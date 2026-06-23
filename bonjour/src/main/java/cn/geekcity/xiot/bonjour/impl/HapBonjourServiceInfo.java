package cn.geekcity.xiot.bonjour.impl;

import cn.geekcity.xiot.bonjour.BonjourServiceInfo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HapBonjourServiceInfo implements BonjourServiceInfo {

    public static final String SERVICE_TYPE = "_hap._tcp.";
    private String name;
    private String type;
    private String ip;
    private int port;
    private int priority;
    private int weight;
    private Map<String, String> properties = new HashMap<>();

    public HapBonjourServiceInfo(
            String name,
            int port,
            boolean discoverable,
            String id,
            String modelName,
            String configurationNumber,
            String currentStateNumber,
            String featureFlags,
            String accessoryCategoryIdentifier
    ) {
        this.name = name;
        this.port = port;
        this.type = SERVICE_TYPE;

        properties.put("pv", "1.0");
        properties.put("sf", discoverable ? "1" : "0");
        properties.put("id", id);
        properties.put("md", modelName);
        properties.put("c#", configurationNumber);
        properties.put("s#", currentStateNumber);
        properties.put("ff", featureFlags);
        properties.put("ci", accessoryCategoryIdentifier);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public HapBonjourServiceInfo name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String type() {
        return this.type;
    }

    @Override
    public HapBonjourServiceInfo type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String ip() {
        return ip;
    }

    @Override
    public HapBonjourServiceInfo ip(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public HapBonjourServiceInfo port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public HapBonjourServiceInfo priority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public int weight() {
        return weight;
    }

    @Override
    public HapBonjourServiceInfo weight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public Map<String, String> properties() {
        return properties;
    }

    @Override
    public HapBonjourServiceInfo properties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public HapBonjourServiceInfo property(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    @Override
    public HapBonjourServiceInfo property(String key, byte[] value) {
        this.property(key, new String(value, StandardCharsets.UTF_8));
        return this;
    }

    public HapBonjourServiceInfo setDiscoverable(boolean discoverable) {
        properties.put("sf", discoverable ? "1" : "0");
        return this;
    }
}