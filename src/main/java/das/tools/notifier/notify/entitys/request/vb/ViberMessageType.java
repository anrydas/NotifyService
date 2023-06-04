package das.tools.notifier.notify.entitys.request.vb;

import das.tools.notifier.notify.entitys.request.MessengerType;

public enum ViberMessageType {
    text,
    picture,
    video,
    file,
    contact,
    location,
    url;

    public static boolean contains(String test) {
        for (MessengerType c : MessengerType.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(ViberMessageType test) {
        for (ViberMessageType c : ViberMessageType.values()) {
            if (c.equals(test)) {
                return true;
            }
        }
        return false;
    }
}
