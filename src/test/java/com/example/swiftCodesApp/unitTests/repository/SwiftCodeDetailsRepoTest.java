package com.example.swiftCodesApp.unitTests.repository;

import com.example.swiftCodesApp.model.SwiftCodeDetails;
import com.example.swiftCodesApp.repository.SwiftCodeDetailsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SwiftCodeDetailsRepoTest {
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    private static String getTestContainerJdbcUrl() {
        return postgreSQLContainer.getJdbcUrl().replace("jdbc:", "jdbc:tc:");
    }

    @DynamicPropertySource
    private static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", SwiftCodeDetailsRepoTest::getTestContainerJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private SwiftCodeDetailsRepo underTest;

    @Test
    void findBranches() {
        // GIVEN
        String headquartersSwiftCode = "ABCDEFGHXXX";
        SwiftCodeDetails headquartersDetails = new SwiftCodeDetails(
                null,
                "SAMPLE-ADDRESS",
                "SAMPLE-BANK-NAME",
                "PL",
                "POLAND",
                headquartersSwiftCode
        );
        SwiftCodeDetails branchDetails = new SwiftCodeDetails(
                null,
                "SAMPLE-ADDRESS",
                "SAMPLE-BANK-NAME",
                "PL",
                "POLAND",
                "ABCDEFGHABC"
        );

        underTest.saveAll(List.of(headquartersDetails, branchDetails));

        // WHEN
        List<SwiftCodeDetails> branches = underTest.findBranches(headquartersSwiftCode);

        // THEN
        assertThat(branches.size()).isEqualTo(1);
        assertThat(branches.get(0).toString()).isEqualTo(branchDetails.toString());
    }
}