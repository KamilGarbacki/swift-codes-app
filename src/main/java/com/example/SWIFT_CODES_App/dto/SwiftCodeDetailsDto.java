package com.example.SWIFT_CODES_App.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCodeDetailsDto {
    protected String address;
    protected String bankName;
    protected String countryISO2;
    protected String countryName;
    @JsonProperty("isHeadquarters")
    protected boolean isHeadquarters;
    protected String swiftCode;
}
