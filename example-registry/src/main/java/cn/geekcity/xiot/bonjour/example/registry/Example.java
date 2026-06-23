package cn.geekcity.xiot.bonjour.example.registry;

import cn.geekcity.xiot.bonjour.BonjourFactory;
import cn.geekcity.xiot.bonjour.impl.HapBonjourServiceInfo;
import io.vertx.core.Vertx;

public class Example {

    public static void main(String args[]) {
        System.out.println("hello");

        HapBonjourServiceInfo serviceInfo = new HapBonjourServiceInfo(
                "test",
                8080,
                true,
                "xxx",
                "aaa",
                "#1",
                "#2",
                "0",
                "aa");

        BonjourFactory.create(Vertx.vertx())
                .start()
                .compose(x -> x.registerService(serviceInfo))
                .onSuccess(x -> {
                    System.out.println("registerService success!");
                })
                .onFailure(Throwable::printStackTrace);
    }
}
