package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.UploadFileInfo;

import java.util.List;

public interface ViberApi {
    int MAX_PICTURE_MESSAGE_LENGTH = 512;
    List<String> getMembers();

    ApplicationResponse sendBroadcastTextMessage(String message, String[] recipients, UploadFileInfo fileInfo);
    void sendMessageToUser(String userId, String message);

    List<String> getMembersList();

    String[] getMembersListAsArray();
}
