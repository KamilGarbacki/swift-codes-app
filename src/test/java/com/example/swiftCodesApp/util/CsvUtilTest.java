package com.example.swiftCodesApp.util;

import com.example.swiftCodesApp.model.SwiftCodeDetails;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvUtilTest {

    @Test
    void loadCsvFile() throws IOException {
        // Given
        String pathToTestFile = "/data/testCsvFile.csv";
        List<String> columns = List.of(
                "countryISO2",
                "swiftCode",
                "codeType",
                "bankName",
                "address",
                "townName",
                "countryName"
        );

        SwiftCodeDetails swiftCode = new SwiftCodeDetails();
        swiftCode.setAddress("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023");
        swiftCode.setBankName("UNITED BANK OF ALBANIA SH.A");
        swiftCode.setCountryISO2("AL");
        swiftCode.setCountryName("ALBANIA");
        swiftCode.setSwiftCode("AAISALTRXXX");

        List<SwiftCodeDetails> expected = List.of(swiftCode);

        // When
        List<SwiftCodeDetails> result = CsvUtil.loadCsvFile(
                pathToTestFile,
                SwiftCodeDetails.class,
                columns
        );

        // Then
        assertThat(result.toString()).isEqualTo(expected.toString());
    }
}