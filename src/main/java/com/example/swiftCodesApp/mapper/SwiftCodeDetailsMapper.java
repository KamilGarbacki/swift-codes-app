package com.example.swiftCodesApp.mapper;

import com.example.swiftCodesApp.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.swiftCodesApp.dto.CountrySwiftCodeDto;
import com.example.swiftCodesApp.dto.HeadquartersDetailsDto;
import com.example.swiftCodesApp.dto.SwiftCodeDetailsDto;
import com.example.swiftCodesApp.model.SwiftCodeDetails;
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
