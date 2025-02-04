package com.example.swiftCodesApp.dto;

import java.util.List;

public record CountrySwiftCodesListDto(
        String countryISO2,
        String countryName,
        List<CountrySwiftCodeDto> swiftCodes
) {
}
