package com.ardaltug.muzikle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ardaltug.muzikle.model.User;
import com.ardaltug.muzikle.service.UserProducerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserProducerService userProducer;

    // Async send
    /**
     * Accepts JSON body with User, and produces it to Kafka asynchronously.
     * {
     *   "id": "user123",
     *   "name": "Alice",
     *   "password": "secret",
     *   "signUpDate":"2025-10-17T17:00:00",
     *   "loginDate":"2025-10-17T17:05:00"
     * }
     */
    @PostMapping("/sendAsync")
    public ResponseEntity<String> sendAsync(@RequestBody User user) {
        if (user == null || user.getId() == null || user.getId().isBlank()) {
                return ResponseEntity.badRequest().body("user.id is required");
            }
        userProducer.sendMessage(user);
        return ResponseEntity.ok("Async message sent!");
    }

    // Sync send
    /* Commented FOr Testing
    @GetMapping("/sendSync")
    public String sendSync(@RequestParam String userId, @RequestParam String event) {
        userProducer.sendMessageSync(userId, event, 5000); // waits max 5s
        return "Sync message sent!";
    }*/
}
