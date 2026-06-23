package cn.geekcity.xiot.bonjour;

import java.util.Map;

public interface BonjourServiceInfo {
    
    String name();

    BonjourServiceInfo name(String name);

    String type();

    BonjourServiceInfo type(String type);

    String ip();

    BonjourServiceInfo ip(String ip);

    int port();

    BonjourServiceInfo port(int port);

    int priority();

    BonjourServiceInfo priority(int priority);

    int weight();

    BonjourServiceInfo weight(int weight);

    Map<String, String> properties();

    BonjourServiceInfo properties(Map<String, String> properties);

    BonjourServiceInfo property(String key, String value);

    BonjourServiceInfo property(String key, byte[] value);
}
