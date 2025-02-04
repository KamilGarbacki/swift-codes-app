package com.example.swiftCodesApp.service;

import com.example.swiftCodesApp.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.swiftCodesApp.dto.CountrySwiftCodesListDto;
import com.example.swiftCodesApp.dto.SwiftCodeDetailsDto;

public interface SwiftCodeDetailsService {
    SwiftCodeDetailsDto addSwiftCodeDetails(SwiftCodeDetailsReq swiftCodeDetailsReq);

    void deleteSwiftCodeDetails(String swiftCode);

    CountrySwiftCodesListDto getCountrySwiftCodes(String countryISO2);

    SwiftCodeDetailsDto getSwiftCodeDetails(String swiftCode);
}
