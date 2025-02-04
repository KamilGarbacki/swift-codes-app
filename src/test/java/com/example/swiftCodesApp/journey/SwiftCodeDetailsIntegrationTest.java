package com.example.swiftCodesApp.journey;

import com.example.swiftCodesApp.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.swiftCodesApp.dto.HeadquartersDetailsDto;
import com.example.swiftCodesApp.dto.SwiftCodeDetailsDto;
import com.example.swiftCodesApp.mapper.SwiftCodeDetailsMapper;
import com.example.swiftCodesApp.model.SwiftCodeDetails;
import com.example.swiftCodesApp.util.TransformUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SwiftCodeDetailsIntegrationTest {
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SwiftCodeDetailsMapper swiftCodeDetailsMapper;

    private static final String SWIFT_CODE_URI = "/api/v1/swift-codes";

    private static String getTestContainerJdbcUrl() {
        return postgreSQLContainer.getJdbcUrl().replace("jdbc:", "jdbc:tc:");
    }

    @DynamicPropertySource
    private static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", SwiftCodeDetailsIntegrationTest::getTestContainerJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void canAddSwiftCodeDetails() throws Exception {
        // create swift code request
        SwiftCodeDetailsReq req = getSwiftCodeRequest("asdfghjklcv");

        // send post request
        sendPostRequest(req);

        // make sure swift code is present
        SwiftCodeDetailsDto result = sendGetRequest(req.swiftCode(), HttpStatus.OK);


        SwiftCodeDetails expected = swiftCodeDetailsMapper.toSwiftCodeDetails(req);
        TransformUtil.allObjectFieldsToUpperCase(expected);

        // make sure swift code data was added correctly
        assertThat(swiftCodeDetailsMapper.toSwiftCodeDetails(result).toString()).isEqualTo(expected.toString());
    }

    @Test
    void canDeleteSwiftCode() {
        // create swift code request
        SwiftCodeDetailsReq req = getSwiftCodeRequest("gehjgiknefg");

        // send post request
        sendPostRequest(req);

        // delete the swift code details
        webTestClient.delete()
                .uri(SWIFT_CODE_URI + "/" + req.swiftCode())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // try to get the deleted swift code
        sendGetRequest(req.swiftCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void canGetHeadquartersDetails() throws IllegalAccessException {
        // create request for headquarters and associated branch
        SwiftCodeDetailsReq headquartersReq = getSwiftCodeRequest("abcdefghxxx");
        SwiftCodeDetailsReq branchReq = getSwiftCodeRequest("abcdefghabc");

        // send post requests
        sendPostRequest(headquartersReq);
        sendPostRequest(branchReq);

        // make sure swift code is present
        SwiftCodeDetailsDto result = sendGetRequest(headquartersReq.swiftCode(), HttpStatus.OK);

        SwiftCodeDetails expectedDetails = swiftCodeDetailsMapper.toSwiftCodeDetails(headquartersReq);
        TransformUtil.allObjectFieldsToUpperCase(expectedDetails);

        HeadquartersDetailsDto expected = swiftCodeDetailsMapper.toHeadquartersSwiftCodeDetailsDto(
                toExpectedSwiftCode(headquartersReq)
        );
        expected.setBranches(List.of(
                swiftCodeDetailsMapper.toSwiftCodeDetailsDto(
                        toExpectedSwiftCode(headquartersReq)
                )
        ));


        //make sure the returned data matches the expected result
        assertThat(result).isEqualTo(expected);
    }


    // util functions
    private SwiftCodeDetailsReq getSwiftCodeRequest(String swiftCode) {
        return new SwiftCodeDetailsReq(
                "sample address",
                "sample bank name",
                "pl",
                "poland",
                swiftCode.endsWith("XXX"),
                swiftCode
        );
    }

    private void sendPostRequest(SwiftCodeDetailsReq req) {
        webTestClient.post()
                .uri(SWIFT_CODE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(req), SwiftCodeDetailsReq.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    private SwiftCodeDetailsDto sendGetRequest(String uri, HttpStatus status) {
        return webTestClient.get()
                .uri(SWIFT_CODE_URI + "/" + uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isEqualTo(status)
                .expectBody(new ParameterizedTypeReference<SwiftCodeDetailsDto>() {
                })
                .returnResult()
                .getResponseBody();
    }

    private SwiftCodeDetails toExpectedSwiftCode(SwiftCodeDetailsReq req) throws IllegalAccessException {
        SwiftCodeDetails expected = swiftCodeDetailsMapper.toSwiftCodeDetails(req);
        TransformUtil.allObjectFieldsToUpperCase(expected);
        return expected;
    }
}
