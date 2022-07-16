package das.tools.notifier.notify.controller;


import das.tools.notifier.notify.entitys.request.vb.ViberWebhookRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/viber")
class ViberController {

    @RequestMapping("/webhook")
    public ResponseEntity<?> setWebhook(@RequestBody ViberWebhookRequest request) {
        if ("webhook".equals(request.getEvent())) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
