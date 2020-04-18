package com.poc;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoinmarketcapClient {

    @Cacheable(
            value = "coinmarketcapCache",
            key = "#uri.toString() + #start + #limit + #convert"
    )
    public String makeAPICall(URI uri, String start, String limit, String convert) throws IOException, URISyntaxException {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("start",start));
        parameters.add(new BasicNameValuePair("limit",limit));
        parameters.add(new BasicNameValuePair("convert",convert));
        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);
        HttpGet request = new HttpGet(query.build());
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", System.getenv("API_KEY"));

        String responseContent = "";
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request);) {

            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity);
        }
        return responseContent;
    }
}
