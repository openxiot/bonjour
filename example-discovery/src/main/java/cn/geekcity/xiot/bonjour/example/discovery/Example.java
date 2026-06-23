package cn.geekcity.xiot.bonjour.example.discovery;

import cn.geekcity.xiot.bonjour.Bonjour;
import cn.geekcity.xiot.bonjour.BonjourFactory;
import cn.geekcity.xiot.bonjour.BonjourServiceInfo;
import cn.geekcity.xiot.bonjour.impl.HapBonjourServiceInfo;
import io.vertx.core.Vertx;

public class Example {

    public static void main(String args[]) {
        System.out.println("hello");

        BonjourFactory.create(Vertx.vertx())
                .start()
                .compose(x -> x.startDiscovery(HapBonjourServiceInfo.SERVICE_TYPE, new Bonjour.DiscoveryListener() {
                    @Override
                    public void onServiceFound(BonjourServiceInfo serviceInfo) {
                        System.out.println("onServiceFound: " + serviceInfo.name() + "/" + serviceInfo.ip());
                    }

                    @Override
                    public void onServiceLost(BonjourServiceInfo serviceInfo) {
                        System.out.println("onServiceLost: " + serviceInfo.name() + "/" + serviceInfo.ip());
                    }
                }))
                .onSuccess(x -> {
                    System.out.println("startDiscovery success!");
                })
                .onFailure(Throwable::printStackTrace);
    }
}
