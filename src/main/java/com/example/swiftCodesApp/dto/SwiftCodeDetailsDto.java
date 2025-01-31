package com.example.swiftCodesApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SwiftCodeDetailsDto {
    protected String address;
    protected String bankName;
    protected String countryISO2;
    protected String countryName;
    @JsonProperty("isHeadquarters")
    protected boolean isHeadquarters;
    protected String swiftCode;
}
