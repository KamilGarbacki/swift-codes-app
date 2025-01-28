package com.example.SWIFT_CODES_App.mapper;

import com.example.SWIFT_CODES_App.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.SWIFT_CODES_App.dto.CountrySwiftCodeDto;
import com.example.SWIFT_CODES_App.dto.HeadquartersDetailsDto;
import com.example.SWIFT_CODES_App.dto.SwiftCodeDetailsDto;
import com.example.SWIFT_CODES_App.model.SwiftCodeDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SwiftCodeDetailsMapper {
    SwiftCodeDetails toSwiftCodeDetails(SwiftCodeDetailsReq swiftCodeDetailsReq);
    SwiftCodeDetailsDto toSwiftCodeDetailsDto(SwiftCodeDetails SwiftCodeDetails);
    @Mapping(expression = "java(swiftCodeDetails.isHeadquarters())", target = "isHeadquarters")
    CountrySwiftCodeDto toCountrySwiftCodeDto(SwiftCodeDetails swiftCodeDetails);
    HeadquartersDetailsDto toHeadquartersSwiftCodeDetailsDto(SwiftCodeDetails swiftCodeDetails);
}
