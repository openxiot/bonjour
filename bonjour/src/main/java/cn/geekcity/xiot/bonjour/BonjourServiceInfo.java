package cn.geekcity.xiot.bonjour;

import java.util.List;
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

    /**
     * Returns the service subtype.
     * <p>
     * The subtype is used for selective instance enumeration in DNS-SD.
     * For example, a HomeKit accessory might use subtype to indicate it's a specific type of device.
     * The full service type with subtype would be: _subtype._sub._service._proto.local.
     * </p>
     *
     * @return the subtype string, or null/empty if no subtype is set
     */
    String subType();

    /**
     * Sets the service subtype.
     * <p>
     * The subtype is used for selective instance enumeration in DNS-SD.
     * For example, a HomeKit bridge might use "bridge" as subtype.
     * </p>
     *
     * @param subtype the subtype string (without the "_sub" prefix)
     * @return this BonjourServiceInfo instance for method chaining
     */
    BonjourServiceInfo subType(String subtype);

    /**
     * Returns the list of service subtypes (e.g. "_S15", "_L3840" for Matter).
     * Used for DNS-SD selective instance enumeration with multiple subtypes.
     *
     * @return the list of subtype strings, or empty list if none set
     */
    List<String> subTypes();

    /**
     * Sets the list of service subtypes.
     *
     * @param subtypes the list of subtype strings
     * @return this BonjourServiceInfo instance for method chaining
     */
    BonjourServiceInfo subTypes(List<String> subtypes);
}
