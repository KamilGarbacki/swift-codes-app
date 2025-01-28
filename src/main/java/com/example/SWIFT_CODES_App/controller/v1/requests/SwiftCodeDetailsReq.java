package com.example.SWIFT_CODES_App.controller.v1.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SwiftCodeDetailsReq(
        @NotBlank
        String address,
        @Pattern(regexp = "[\\w0-9., ]+", message = "Country name must be ")
        String bankName,
        @Pattern(regexp = "\\w{2}", message = "ISO2 code must be only 2 characters long")
        String countryISO2,
        @Pattern(regexp = "[\\w ]+", message = "Country name can not be empty")
        String countryName,
        boolean isHeadquarters,
        @Pattern(regexp = "[\\w0-9]{8}|[\\w0-9]{11}", message = "Swift code must be 8 or 11 characters long")
        String swiftCode
) {
}
