package cn.geekcity.xiot.bonjour;

import io.vertx.core.Future;

import java.util.List;
import java.util.Map;

public interface Bonjour {

    Future<Bonjour> start(List<String> addresses);

    Future<Bonjour> start(String address);

    Future<Bonjour> start();

    Future<Void> stop();

    Future<Void> startDiscovery(String serviceType, DiscoveryListener listener);

    Future<Void> stopAllDiscovery();

    Future<Void> registerService(BonjourServiceInfo serviceInfo);

    Future<Void> unregisterService(BonjourServiceInfo serviceInfo);

    interface DiscoveryListener {

        void onServiceFound(BonjourServiceInfo serviceInfo);

        void onServiceLost(BonjourServiceInfo serviceInfo);
    }
}