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
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    @JsonProperty("isHeadquarters")
    private boolean isHeadquarters;
    private String swiftCode;
}
