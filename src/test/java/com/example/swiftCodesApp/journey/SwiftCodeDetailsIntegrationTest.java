package com.example.swiftCodesApp.journey;

import com.example.swiftCodesApp.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.swiftCodesApp.model.SwiftCodeDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SwiftCodeDetailsIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canAddSwiftCodeDetails(){
        //create request
        SwiftCodeDetailsReq req = new SwiftCodeDetailsReq(
                "sample address",
                "sample bank name",
                "pl",
                "poland",
                false,
                "abcabcabcab"
        );
        //send post request
        webTestClient.post()
                .uri("/api/v1/swift-codes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(req), SwiftCodeDetailsReq.class)
                .exchange()
                .expectStatus()
                .isCreated();
        //make sure swift code is present
        //get details by added swift code
    }
}
