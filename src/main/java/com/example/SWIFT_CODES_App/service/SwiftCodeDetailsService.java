package com.example.SWIFT_CODES_App.service;
import com.example.SWIFT_CODES_App.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.SWIFT_CODES_App.dto.CountrySwiftCodesListDto;
import com.example.SWIFT_CODES_App.dto.SwiftCodeDetailsDto;

public interface SwiftCodeDetailsService {
    SwiftCodeDetailsDto addSwiftCodeDetails(SwiftCodeDetailsReq swiftCodeDetailsReq);
    void deleteSwiftCodeDetails(String swiftCode);
    CountrySwiftCodesListDto getCountrySwiftCodes(String countryISO2);
    SwiftCodeDetailsDto getSwiftCodeDetails(String swiftCode);
}
