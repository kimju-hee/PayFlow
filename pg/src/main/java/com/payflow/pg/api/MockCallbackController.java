package com.payflow.pg.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockCallbackController {
    @PostMapping("/mock/callback")
    public ResponseEntity<Void> callback(@RequestBody(required=false) String body) {
        System.out.println("WEBHOOK RECEIVED: " + body);
        return ResponseEntity.ok().build();
    }
}