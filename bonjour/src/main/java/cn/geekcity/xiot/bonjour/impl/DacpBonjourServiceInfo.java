package cn.geekcity.xiot.bonjour.impl;

import cn.geekcity.xiot.bonjour.BonjourServiceInfo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DacpBonjourServiceInfo implements BonjourServiceInfo {

    public static final String SERVICE_TYPE = "_dacp._tcp";
    private String name;
    private String type;
    private String subtype;
    private String ip;
    private int port;
    private int priority;
    private int weight;
    private Map<String, String> properties = new HashMap<>();
    private List<String> subtypes = new ArrayList<>();

    public DacpBonjourServiceInfo(String name, int port) {
        this.type = SERVICE_TYPE;
        this.name = name;
        this.port = port;

        properties.put("txtvers", "1");
        properties.put("Ver", "131075");
        properties.put("DbId", "63B5E5C0C201542E");
        properties.put("OSsi", "0x1F5");
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public DacpBonjourServiceInfo name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String type() {
        return this.type;
    }

    @Override
    public DacpBonjourServiceInfo type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String ip() {
        return ip;
    }

    @Override
    public DacpBonjourServiceInfo ip(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public DacpBonjourServiceInfo port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public DacpBonjourServiceInfo priority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public int weight() {
        return weight;
    }

    @Override
    public DacpBonjourServiceInfo weight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public Map<String, String> properties() {
        return properties;
    }

    @Override
    public DacpBonjourServiceInfo properties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public DacpBonjourServiceInfo property(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    @Override
    public DacpBonjourServiceInfo property(String key, byte[] value) {
        this.property(key, new String(value, StandardCharsets.UTF_8));
        return this;
    }

    @Override
    public String subType() {
        return subtype;
    }

    @Override
    public DacpBonjourServiceInfo subType(String subtype) {
        this.subtype = subtype;
        return this;
    }

    @Override
    public List<String> subTypes() {
        return subtypes;
    }

    @Override
    public BonjourServiceInfo subTypes(List<String> subtypes) {
        this.subtypes = subtypes != null ? subtypes : new ArrayList<>();
        return this;
    }
}
