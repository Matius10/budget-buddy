package com.budgetbuddy.integration.tink;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TinkClient {

    private final String clientId;
    private final String clientSecret;

    // TODO: change to TinkAppConfiguration
    TinkClient(@Value("${integration.tink.clientId}") String clientId, @Value("${integration.tink.clientSecret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    // TODO: make it nice
    public String getTinkLinkURL() {
        return "https://link.tink.com/1.0/transactions/connect-accounts/" +
                "?client_id=" + clientId +
                "&redirect_uri=http%3A%2F%2Flocalhost%3A8888%2Fapi%2Fcallback" +
                "&market=PL" +
                "&locale=en_US";
    }

}
