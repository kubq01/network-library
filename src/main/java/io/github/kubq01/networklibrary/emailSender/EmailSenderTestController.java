package io.github.kubq01.networklibrary.emailSender;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmailSenderTestController {

    private final EmailAlertService emailAlertService;

    @GetMapping("/test")
    public void test() {
        emailAlertService.sendAlert("Email Sender Test");
    }
}
