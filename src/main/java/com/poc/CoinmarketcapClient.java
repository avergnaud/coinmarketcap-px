package com.poc;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Service
public class CoinmarketcapClient {

    @Cacheable(
            value = "coinmarketcapCache",
            key = "#request.toString()"
    )
    public String makeAPICall(HttpGet request) throws IOException {

        String responseContent = "";
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", System.getenv("API_KEY"));

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request);) {

            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity);
        }
        return responseContent;
    }
}
