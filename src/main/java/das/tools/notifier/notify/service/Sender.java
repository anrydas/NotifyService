package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Pattern;

public interface Sender {
    String EMAIL_SEMICOLON = ";";
    Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    @Value("${vb.media.folder}")
    String FILES_RELATIVE_PATH = "media.tmp";
    @Value("${app.files.url.path}")
    String FILES_URL_PREFIX = "files";

    ApplicationResponse send(Request request) throws WrongRequestParameterException;
}
