package com.poc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    @CrossOrigin(origins = {"http://localhost:1313", "https://avergnaud.github.io"})
    @GetMapping(value = "/listings/latest", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getAll(@RequestParam Integer start,
                                         @RequestParam Integer limit,
                                         @RequestParam String convert) {

        String uri = coinmarketcapUrl + "/cryptocurrency/listings/latest";

        try {
            String response = client.makeAPICall(new URI(uri), start.toString(), limit.toString(), convert);
            return ResponseEntity.ok()
                    .body(response);
        } catch (IOException | URISyntaxException e) {
            String message = "Error sending request. " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(message);
        }
    }
}