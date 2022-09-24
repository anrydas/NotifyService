package das.tools.notifier.notify.controller;


import das.tools.notifier.notify.entitys.request.vb.ViberWebhookRequest;
import das.tools.notifier.notify.service.ViberApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/viber")
@Slf4j
class ViberHookController {
    protected static final String THE_END_SUBSCRIPTION_COMMAND = "/TheEnd";
    private final ViberApi viberApi;

    ViberHookController(ViberApi viberApi) {
        this.viberApi = viberApi;
    }

    @RequestMapping("/webhook")
    public ResponseEntity<?> processWebhook(@RequestBody ViberWebhookRequest request) {
        if(log.isDebugEnabled()) log.debug("[processWebhook]: request={}", request);
        String event = request.getEvent();
        if(log.isDebugEnabled()) log.debug("[processWebhook]: got event={}", event);
        if ("webhook".equals(event)) {
            if(log.isDebugEnabled()) log.debug("[processWebhook]: process event={}", event);
            return ResponseEntity.ok("");
        } else if ("subscribed".equals(event)) {
            if(log.isDebugEnabled()) log.debug("[processWebhook]: process event={}", event);
            processSubscription(request);
            return ResponseEntity.ok("");
        } else if ("unsubscribed".equals(event)) {
            if(log.isDebugEnabled()) log.debug("[processWebhook]: process event={}", event);
            processUnSubscription(request);
            return ResponseEntity.ok("");
        } else if ("conversation_started".equals(event)) {
            if(log.isDebugEnabled()) log.debug("[processWebhook]: process event={}", event);
            processConversation(request);
            return ResponseEntity.ok("");
        } else if ("message".equals(event)) {
            if(log.isDebugEnabled()) log.debug("[processWebhook]: process event={}", event);
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
    //ToDo: Subscribe/Unsubscribe functional doesnt works yet
    private void processSubscription(ViberWebhookRequest request) {
        String message;
        String userName = request.getUserInfo().getName();
        if(request.isSubscribed()){
            message = String.format("Hello %s! You already subscribed to this conversation.", userName);
        } else {
            message = String.format("Hello %s! You have been subscribed to receive messages from this bot.", userName) +
                    "\r" +
                    String.format("To unsubscribe type *%s*", THE_END_SUBSCRIPTION_COMMAND);
        }
        viberApi.sendMessageToUser(request.getUserInfo().getId(), message);
    }

    private void processConversation(ViberWebhookRequest request) {
        String userName = request.getUserInfo().getName();
        String message = String.format("%s, you already have been subscribed to this bot.", userName);
        viberApi.sendMessageToUser(request.getUserInfo().getId(), message);
    }

    private void processUnSubscription(ViberWebhookRequest request) {
        String userName = request.getUserInfo().getName();
        String message = String.format("%s, you have been UN-subscribed.", userName);;
        viberApi.sendMessageToUser(request.getUserInfo().getId(), message);
    }
}
