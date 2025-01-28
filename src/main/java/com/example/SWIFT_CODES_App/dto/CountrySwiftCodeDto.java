package com.example.SWIFT_CODES_App.dto;

public record CountrySwiftCodeDto(
        String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarters,
        String swiftCode
) {}
