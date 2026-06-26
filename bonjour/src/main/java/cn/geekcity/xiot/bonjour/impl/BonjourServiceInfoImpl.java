package cn.geekcity.xiot.bonjour.impl;

import cn.geekcity.xiot.bonjour.BonjourServiceInfo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BonjourServiceInfoImpl implements BonjourServiceInfo {

    private String name;
    private String type;
    private String subtype;
    private List<String> subtypes = new ArrayList<>();
    private String ip;
    private int port;
    private int priority;
    private int weight;
    private Map<String, String> properties = new HashMap<String, String>();

    @Override
    public String name() {
        return name;
    }

    @Override
    public BonjourServiceInfoImpl name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public BonjourServiceInfoImpl type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String ip() {
        return ip;
    }

    @Override
    public BonjourServiceInfoImpl ip(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public BonjourServiceInfoImpl port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public BonjourServiceInfoImpl priority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public int weight() {
        return weight;
    }

    @Override
    public BonjourServiceInfoImpl weight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public Map<String, String> properties() {
        return properties;
    }

    @Override
    public BonjourServiceInfoImpl properties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public BonjourServiceInfoImpl property(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    @Override
    public BonjourServiceInfoImpl property(String key, byte[] value) {
        this.property(key, new String(value, StandardCharsets.UTF_8));
        return this;
    }

    @Override
    public String subType() {
        return subtype;
    }

    @Override
    public BonjourServiceInfoImpl subType(String subtype) {
        this.subtype = subtype;
        return this;
    }

    @Override
    public List<String> subTypes() {
        return subtypes;
    }

    @Override
    public BonjourServiceInfoImpl subTypes(List<String> subtypes) {
        this.subtypes = subtypes != null ? subtypes : new ArrayList<>();
        return this;
    }
}
