package com.example.swiftCodesApp.unitTests.util;

import com.example.swiftCodesApp.model.SwiftCodeDetails;
import com.example.swiftCodesApp.util.TransformUtil;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TransformUtilTest {

    @Test
    void allObjectFieldsToUpperCase() throws IllegalAccessException {
        // Given
        SwiftCodeDetails swiftCodeDetails = new SwiftCodeDetails(
                1L,
                "sample-address",
                "sample-bank-name",
                "pl",
                "poland",
                "swift-code1"
        );

        SwiftCodeDetails expected = new SwiftCodeDetails(
                1L,
                "SAMPLE-ADDRESS",
                "SAMPLE-BANK-NAME",
                "PL",
                "POLAND",
                "SWIFT-CODE1"
        );

        TransformUtil.allObjectFieldsToUpperCase(swiftCodeDetails);

        // When
        // Then
        assertThat(swiftCodeDetails.toString()).isEqualTo(expected.toString());
    }
}