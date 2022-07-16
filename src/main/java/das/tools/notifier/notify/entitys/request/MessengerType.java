package das.tools.notifier.notify.entitys.request;

public enum MessengerType {
    NONE,
    //ALL,
    TELEGRAMM,
    //VIBER,
    MATRIX,
    //MATRIX_E,
    EMAIL;

    public static boolean contains(String test) {
        for (MessengerType c : MessengerType.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(MessengerType test) {
        for (MessengerType c : MessengerType.values()) {
            if (c.equals(test)) {
                return true;
            }
        }
        return false;
    }

}
