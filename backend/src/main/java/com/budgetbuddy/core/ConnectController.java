package com.budgetbuddy.core;

import com.budgetbuddy.integration.tink.TinkClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/connect")
@AllArgsConstructor
class ConnectController {

    private final TinkClient tinkClient;

    @GetMapping
    public RedirectView getTinkLinkRedirect() {
        return new RedirectView(tinkClient.getTinkLinkURL());
    }

}
