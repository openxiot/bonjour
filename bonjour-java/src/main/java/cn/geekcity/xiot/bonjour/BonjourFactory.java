package cn.geekcity.xiot.bonjour;

import cn.geekcity.xiot.bonjour.impl.BonjourJavaImpl;
import io.vertx.core.Vertx;

public class BonjourFactory {

    public static Bonjour create(Vertx vertx) {
        return BonjourJavaImpl.getInstance(vertx);
    }
}
