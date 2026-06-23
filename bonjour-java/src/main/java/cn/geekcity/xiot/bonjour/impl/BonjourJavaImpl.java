package cn.geekcity.xiot.bonjour.impl;

import cn.geekcity.xiot.bonjour.Bonjour;
import cn.geekcity.xiot.bonjour.BonjourServiceInfo;
import cn.geekcity.xiot.bonjour.net.NetUtils;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;

public class BonjourJavaImpl implements Bonjour, ServiceListener {

    private static final String TAG = BonjourJavaImpl.class.getSimpleName();
    private static BonjourJavaImpl instance = null;
    private static final Object classLock = BonjourJavaImpl.class;
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    public static BonjourJavaImpl getInstance(Vertx vertx) {
        synchronized (classLock) {
            if (instance == null) {
                instance = new BonjourJavaImpl(vertx);
            }

            return instance;
        }
    }


    private final Vertx vertx;
    private final List<JmDNS> jmdnsInstances = new ArrayList<>();
    private final Map<String, Map<String, ServiceInfo>> servicesRegistered = new HashMap<>();
    private final Map<String, Map<String, BonjourServiceInfo>> servicesFound = new HashMap<>();
    private final Map<String, DiscoveryListener> listeners = new HashMap<>();

    private BonjourJavaImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Future<Bonjour> start(List<String> addresses) {
        Promise<Bonjour> promise = Promise.promise();

        vertx.executeBlocking(
                p -> {
                    try {
                        startBlocking(addresses);
                        p.complete();
                    } catch (Exception e) {
                        p.fail(e);
                    }
                },
                ar -> {
                    if (ar.succeeded()) {
                        promise.complete(this);
                    } else {
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }

    public void startBlocking(List<String> addresses) {
        logger.info("start");

        if (!jmdnsInstances.isEmpty()) {
            logger.info("jmdns already started");
            throw new IllegalArgumentException("jmdns already started");
        }

        for (InetAddress address : NetUtils.getAllInterfaces()) {
            if (addresses.contains(address.getHostAddress())) {
                try {
                    logger.info("jmdns create: " + address.getHostAddress());
                    JmDNS instance = JmDNS.create(address);
                    jmdnsInstances.add(instance);
                    servicesRegistered.put(instance.getHostName(), new HashMap<>());
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }

        if (jmdnsInstances.isEmpty()) {
            throw new IllegalArgumentException("jmdns instance is 0");
        }

        logger.info(String.format("JmDNS version: %s", JmDNS.VERSION));
    }

    @Override
    public Future<Bonjour> start(String address) {
        return start(Collections.singletonList(address));
    }

    @Override
    public Future<Bonjour> start() {
        List<String> addresses = new ArrayList<>();
        for (InetAddress address : NetUtils.getAllInterfaces()) {
            logger.info("Found network address: " + address.getHostAddress());
            addresses.add(address.getHostAddress());
        }

        return start(addresses);
    }

    @Override
    public Future<Void> stop() {
        Promise<Void> promise = Promise.promise();

        vertx.executeBlocking(
                p -> {
                    try {
                        _stop();
                        p.complete();
                    } catch (Exception e) {
                        p.fail(e);
                    }
                },
                promise);

        return promise.future();
    }

    private void _stop() throws IOException {
        if (jmdnsInstances.isEmpty()) {
            throw new IllegalArgumentException("jmdns not started");
        }

        for (JmDNS instance : jmdnsInstances) {
            instance.unregisterAllServices();
            instance.close();
        }

        servicesRegistered.clear();
        servicesFound.clear();
        listeners.clear();
        jmdnsInstances.clear();
    }

    @Override
    public Future<Void> startDiscovery(String serviceType, DiscoveryListener listener) {
        if (jmdnsInstances.isEmpty()) {
            return Future.failedFuture("jmdns not started");
        }

        // _hap._tcp.local.
        String type = serviceType + "local.";

        Promise<Void> promise = Promise.promise();

        vertx.executeBlocking(
                p -> {
                    for (JmDNS instance : jmdnsInstances) {
                        instance.addServiceListener(type, this);
                    }
                    p.complete();
                },
                ar -> {
                    if (ar.succeeded()) {
                        listeners.put(type, listener);
                        promise.complete();
                    } else {
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }

    @Override
    public Future<Void> stopAllDiscovery() {
        if (jmdnsInstances.isEmpty()) {
            return Future.failedFuture("jmdns not started");
        }

        Promise<Void> promise = Promise.promise();

        vertx.executeBlocking(
                p -> {
                    for (JmDNS instance : jmdnsInstances) {
                        listeners.forEach((type, listener) -> instance.removeServiceListener(type, this));
                    }
                    p.complete();
                },
                ar -> {
                    if (ar.succeeded()) {
                        listeners.clear();
                        promise.complete();
                    } else {
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }

    @Override
    public Future<Void> registerService(BonjourServiceInfo serviceInfo) {
        ServiceInfo info = ServiceInfo.create(serviceInfo.type() + "local.",
                serviceInfo.name(),
                serviceInfo.port(),
                serviceInfo.weight(),
                serviceInfo.priority(),
                serviceInfo.properties());

        logger.info("registerService: " + info.getType());

        if (jmdnsInstances.isEmpty()) {
            return Future.failedFuture("jmdns not started");
        }

        for (JmDNS instance : jmdnsInstances) {
            Map<String, ServiceInfo> list = servicesRegistered.get(instance.getHostName());
            if (list == null) {
                continue;
            }

            if (list.containsKey(info.getType())) {
                logger.info(String.format("%s already registered", info.getType()));
                continue;
            }

            ServiceInfo newInfo = info.clone();

            list.put(info.getType(), newInfo);

            try {
                instance.registerService(newInfo);
            } catch (IOException e) {
                logger.error(e);
            }
        }

        return Future.succeededFuture();
    }

    @Override
    public Future<Void> unregisterService(BonjourServiceInfo serviceInfo) {
        if (jmdnsInstances.isEmpty()) {
            return Future.failedFuture("jmdns not started");
        }

        Promise<Void> promise = Promise.promise();

        vertx.executeBlocking(
                p -> {
                    try {
                        _unregisterService(serviceInfo);
                        p.complete();
                    } catch (Exception e) {
                        p.fail(e);
                    }
                },
                promise);

        return promise.future();
    }

    private void _unregisterService(BonjourServiceInfo serviceInfo) {
        String type = serviceInfo.type() + "local.";

        for (JmDNS instance : jmdnsInstances) {
            Map<String, ServiceInfo> list = servicesRegistered.get(instance.getHostName());
            if (list == null) {
                continue;
            }

            ServiceInfo info = list.remove(type);
            if (info == null) {
                continue;
            }

            instance.unregisterService(info);
            logger.info(String.format("unregisterService: %s from %s", type, instance.getHostName()));
        }
    }

    @Override
    public void serviceAdded(ServiceEvent event) {
        logger.info("serviceAdded");

        if (jmdnsInstances.isEmpty()) {
            logger.info("jmdns not started");
            return;
        }

        // TODO: 这里可能有问题，谁发现的，应该让谁去解析
        for (JmDNS instance : jmdnsInstances) {
            logger.info("requestServiceInfo: " + event.getType());
            instance.requestServiceInfo(event.getType(), event.getName());
        }
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        logger.info("serviceRemoved: " + event.getType());

        if (jmdnsInstances.isEmpty()) {
            logger.info("jmdns not started");
            return;
        }

        Map<String, BonjourServiceInfo> map = servicesFound.get(event.getType());
        if (map == null) {
            logger.info("map not exist");
            return;
        }

        BonjourServiceInfo s = map.get(event.getName());
        if (s == null) {
            logger.info("service not exist");
            return;
        }

        DiscoveryListener listener = listeners.get(event.getType());
        if (listener == null) {
            return;
        }

        listener.onServiceLost(s);
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        logger.info("serviceResolved: " + event.getType());

        if (event.getInfo() == null) {
            logger.info("event.getInfo() == null");
            return;
        }

        String name = event.getName();
        String type = event.getType();
        int port = event.getInfo().getPort();
        String ip = null;

        Inet4Address[] addresses = event.getInfo().getInet4Addresses();
        if (addresses == null) {
            logger.info("event.getInfo().getInet4Addresses() == null");
            return;
        }

        for (Inet4Address addr : addresses) {
            ip = addr.getHostAddress();
        }

        if (ip == null) {
            logger.info("ip == null");
            return;
        }

        Map<String, String> properties = new HashMap<>();
        Enumeration<String> propertyNames = event.getInfo().getPropertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = propertyNames.nextElement();
            String value = event.getInfo().getPropertyString(key);
            properties.put(key, value);
        }

        BonjourServiceInfo s = new BonjourServiceInfoImpl();
        s.name(name);
        s.type(type);
        s.ip(ip);
        s.port(port);
        s.properties(properties);

        Map<String, BonjourServiceInfo> map = servicesFound.computeIfAbsent(type, k -> new HashMap<>());
        if (map.containsKey(s.name())) {
            return;
        }

        map.put(s.name(), s);

        DiscoveryListener listener = listeners.get(type);
        if (listener == null) {
            logger.error("listener not found");
            return;
        }

        listener.onServiceFound(s);
    }
}
