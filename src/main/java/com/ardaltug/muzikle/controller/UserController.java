package com.ardaltug.muzikle.controller;

import com.ardaltug.muzikle.service.UserProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserProducerService userProducer;

    // Async send
    @GetMapping("/sendAsync")
    public String sendAsync(@RequestParam String userId, @RequestParam String event) {
        userProducer.sendMessage(userId, event);
        return "Async message sent!";
    }

    // Sync send
    @GetMapping("/sendSync")
    public String sendSync(@RequestParam String userId, @RequestParam String event) {
        userProducer.sendMessageSync(userId, event, 5000); // waits max 5s
        return "Sync message sent!";
    }
}
