package com.example.swiftCodesApp.dto;

public record CountrySwiftCodeDto(
        String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarters,
        String swiftCode
) {}
