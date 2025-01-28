package com.example.SWIFT_CODES_App.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquartersDetailsDto extends SwiftCodeDetailsDto {
    private List<SwiftCodeDetailsDto> branches;
}
