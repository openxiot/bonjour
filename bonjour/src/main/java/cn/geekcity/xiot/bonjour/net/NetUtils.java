package cn.geekcity.xiot.bonjour.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetUtils {

    private static final Logger logger = Logger.getLogger(NetUtils.class.getName());

    public static List<InetAddress> getAllInterfaces() {
        List<InetAddress> list = new ArrayList<>();

        Enumeration<NetworkInterface> allNetInterfaces;

        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            logger.log(Level.WARNING, "Failed to get network interfaces", e);
            return list;
        }

        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();

            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address instanceof Inet4Address) {
                    if (! address.isLoopbackAddress()) {
                        list.add(address);
                    }
                }
            }
        }

        return list;
    }
}
