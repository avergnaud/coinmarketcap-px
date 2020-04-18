package com.poc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cryptocurrency")
public class MarketCapController {

    private static final Logger log = LoggerFactory.getLogger(MarketCapController.class);

    @Value("${coinmarketcap.url}")
    private String coinmarketcapUrl;

    @Autowired
    CoinmarketcapClient client;

    @CrossOrigin(origins = "http://localhost:1313")
    @GetMapping(value = "/listings/latest", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getAll(@RequestParam Integer start,
                                         @RequestParam Integer limit,
                                         @RequestParam String convert) {

        String uri = coinmarketcapUrl + "/cryptocurrency/listings/latest";
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("start",start.toString()));
        parameters.add(new BasicNameValuePair("limit",limit.toString()));
        parameters.add(new BasicNameValuePair("convert",convert));

        HttpGet request = null;
        try {
            URIBuilder query = new URIBuilder(uri);
            query.addParameters(parameters);
            request = new HttpGet(query.build());
            String response = client.makeAPICall(request);
            return ResponseEntity.ok()
                    .body(response);
        } catch (IOException | URISyntaxException e) {
            String message = "Error sending request: " + request + " " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(message);
        }
    }
}