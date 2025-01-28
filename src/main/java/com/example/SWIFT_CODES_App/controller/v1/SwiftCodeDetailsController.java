package com.example.SWIFT_CODES_App.controller.v1;

import com.example.SWIFT_CODES_App.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.SWIFT_CODES_App.dto.CountrySwiftCodesListDto;
import com.example.SWIFT_CODES_App.dto.ResponseMessageDto;
import com.example.SWIFT_CODES_App.dto.SwiftCodeDetailsDto;
import com.example.SWIFT_CODES_App.service.SwiftCodeDetailsService;
import com.example.SWIFT_CODES_App.util.ResponseMessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/swift-codes")
public record SwiftCodeDetailsController(
        SwiftCodeDetailsService swiftCodeDetailsService
) {

    @PostMapping()
    @Operation(summary = "Adds new SWIFT code entries to the database for a specific country.")
    public ResponseEntity<ResponseMessageDto> addSwiftCodeDetails(
            @Valid
            @RequestBody
            SwiftCodeDetailsReq req
    ) {
        SwiftCodeDetailsDto swift = swiftCodeDetailsService.addSwiftCodeDetails(req);
        ResponseMessageDto res = ResponseMessageUtil.createMessage(
                ResponseMessageUtil.ResponseMessageType.CREATED_MESSAGE,
                swift.getSwiftCode()
        );

        return ResponseEntity
                .created(URI.create("/api/v1/swift-codes/%s".formatted(swift.getSwiftCode())))
                .body(res);
    }

    @DeleteMapping("/{swift-codes}")
    @Operation(summary = "Deletes swift-code data if swiftCode, bankName and countryISO2 matches the one in the database.")
    public ResponseEntity<ResponseMessageDto> deleteSwiftCodeDetails(@PathVariable("swift-codes") String swiftCode) {
        swiftCodeDetailsService.deleteSwiftCodeDetails(swiftCode);

        ResponseMessageDto res = ResponseMessageUtil.createMessage(
                ResponseMessageUtil.ResponseMessageType.DELETED_MESSAGE,
                swiftCode
        );

        return ResponseEntity.ok(res);
    }

    @GetMapping("/country/{countryISO2code}")
    @Operation(summary = "Retrieve all SWIFT codes with details for a specific country corresponding to the given ISO2 code")
    public ResponseEntity<CountrySwiftCodesListDto> getCountrySwiftCodes(@PathVariable String countryISO2code) {
        return ResponseEntity.ok(swiftCodeDetailsService.getCountrySwiftCodes(countryISO2code));
    }

    @GetMapping("/{swift-codes}")
    @Operation(summary = "Retrieve details of a single SWIFT code whether for a headquarters or branches.")
    public ResponseEntity<SwiftCodeDetailsDto> getSwiftCodeDetails(@PathVariable("swift-codes") String swiftCode) {
        return ResponseEntity.ok(swiftCodeDetailsService.getSwiftCodeDetails(swiftCode));
    }
}
