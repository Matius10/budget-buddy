package com.budgetbuddy.callback;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("${server.callback.path}")
@AllArgsConstructor
class CallbackController {

    private final CallbackService callbackService;

    @GetMapping
    public ResponseEntity<Object> callback(HttpServletRequest request, @RequestParam Map<String, String> params) {
        final boolean wasProcessed = callbackService.processCallback(params);
        return wasProcessed ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

}
