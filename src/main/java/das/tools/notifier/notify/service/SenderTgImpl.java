package das.tools.notifier.notify.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
import com.pengrad.telegrambot.response.SendResponse;
import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service("telegram")
@Slf4j
public class SenderTgImpl implements Sender {

    private final RestTemplate restTemplate;
    private final TelegramBot telegramBot;

    @Value("${tg.baseUrl}")
    private String tgBaseUrl;
    @Value("${tg.api.key}")
    private String tgApiKey;
    @Value("${tg.chatId}")
    private String tgChatId;

    public SenderTgImpl(RestTemplate restTemplate, TelegramBot telegramBot) {
        this.restTemplate = restTemplate;
        this.telegramBot = telegramBot;
    }

    @Override
    public ApplicationResponse send(Request request) throws WrongRequestParameterException {
        String message = Utils.getNotNullString(request.getMessage());
        ApplicationResponse appResponse;
        String chat = Utils.getNotNullStringOrDefault(request.getChatId(), tgChatId);
        if ("".equals(chat)) {
            throw new WrongRequestParameterException("Telegram's chat ID is empty");
        }
        String file = Utils.getNotNullString(request.getFile());
        File f = new File(file);
        if (!"".equals(file) && !f.exists()) {
            throw new WrongRequestParameterException(String.format("Send to Telegram: File '%s' doesn't exists", file));
        }
        Long chatId = Long.valueOf(chat);
        SendResponse response = !"".equals(file) ? getResponse(f, chatId, message) : getResponse(chatId, message);
        if (response.isOk()) {
            appResponse = ApplicationResponse.builder()
                    .status(ResponseStatus.OK)
                    .build();
        } else {
            appResponse = ApplicationResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .errorMessage("Error occurred during sending message to Telegram")
                    .comment(response.toString())
                    .build();
        }
        return appResponse;
    }

    private SendResponse getResponse(File file, Long chat, String message) {
        SendResponse res;
        if (Utils.isImageFile(file.getAbsolutePath())) {
            SendPhoto tgRequest = new SendPhoto(chat, file)
                    .parseMode(ParseMode.HTML)
                    .caption(message);
            res = telegramBot.execute(tgRequest);
        } else if(Utils.isVideoFile(file.getAbsolutePath())){
            SendVideo tgRequest = new SendVideo(chat, file)
                    .parseMode(ParseMode.HTML)
                    .caption(message);
            res = telegramBot.execute(tgRequest);
        } else {
            res = getResponse(chat, message);
        }
        return res;
    }

    private SendResponse getResponse(Long chat, String message) {
        SendResponse res;
        SendMessage tgRequest = new SendMessage(chat, message)
                .parseMode(ParseMode.HTML);
        res = telegramBot.execute(tgRequest);
        return res;
    }
}
