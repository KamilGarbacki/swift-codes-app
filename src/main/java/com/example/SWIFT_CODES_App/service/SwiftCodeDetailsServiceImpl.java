package com.example.SWIFT_CODES_App.service;

import com.example.SWIFT_CODES_App.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.SWIFT_CODES_App.dto.CountrySwiftCodeDto;
import com.example.SWIFT_CODES_App.dto.CountrySwiftCodesListDto;
import com.example.SWIFT_CODES_App.dto.HeadquartersDetailsDto;
import com.example.SWIFT_CODES_App.dto.SwiftCodeDetailsDto;
import com.example.SWIFT_CODES_App.mapper.SwiftCodeDetailsMapper;
import com.example.SWIFT_CODES_App.model.SwiftCodeDetails;
import com.example.SWIFT_CODES_App.repository.SwiftCodeDetailsRepo;
import com.example.SWIFT_CODES_App.util.CsvUtil;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.SWIFT_CODES_App.util.ResourceUtil.readFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class SwiftCodeDetailsServiceImpl implements SwiftCodeDetailsService {
    private final SwiftCodeDetailsRepo swiftCodeDetailsRepo;
    private final SwiftCodeDetailsMapper swiftCodeDetailsMapper;

    @Override
    public SwiftCodeDetailsDto addSwiftCodeDetails(SwiftCodeDetailsReq req) {
        log.info("Adding Swift Code data: {}", req);
        SwiftCodeDetails newSwiftCodeDetails = swiftCodeDetailsMapper.toSwiftCodeDetails(req);
        SwiftCodeDetails savedCode = swiftCodeDetailsRepo.save(newSwiftCodeDetails);
        return swiftCodeDetailsMapper.toSwiftCodeDetailsDto(savedCode);
    }


    @Override
    public void deleteSwiftCodeDetails(String swiftCode) {
        log.info("Deleting data associated with Swift Code: {}", swiftCode);
        SwiftCodeDetails toBeDeleted = getSwiftCodeDetailsBySwiftCode(swiftCode);
        swiftCodeDetailsRepo.delete(toBeDeleted);
    }

    @Override
    public CountrySwiftCodesListDto getCountrySwiftCodes(String countryISO2) {
        log.info("Getting list of Swift Codes data for country of ISO2 code: {}", countryISO2);
        List<SwiftCodeDetails> swiftCodes = swiftCodeDetailsRepo.findByCountryISO2(countryISO2);
        List<CountrySwiftCodeDto> swiftCodesDto = swiftCodes.stream()
                .map(swiftCodeDetailsMapper::toCountrySwiftCodeDto)
                .toList();

        return new CountrySwiftCodesListDto(
                countryISO2,
                swiftCodes.getFirst().getCountryName(),
                swiftCodesDto
        );
    }

    @Override
    public SwiftCodeDetailsDto getSwiftCodeDetails(String swiftCode) {
        log.info("Getting data of a single Swift Code: {}", swiftCode);
        SwiftCodeDetails requestedDetails = getSwiftCodeDetailsBySwiftCode(swiftCode);

        if (!requestedDetails.isHeadquarters()) {
            return swiftCodeDetailsMapper.toSwiftCodeDetailsDto(requestedDetails);
        }

        HeadquartersDetailsDto headquarters = swiftCodeDetailsMapper.toHeadquartersSwiftCodeDetailsDto(requestedDetails);
        var branches = swiftCodeDetailsRepo.findBranches(swiftCode).stream()
                .map(swiftCodeDetailsMapper::toSwiftCodeDetailsDto)
                .toList();

        headquarters.setBranches(branches);

        return headquarters;
    }

    @PostConstruct
    private void loadData() {
        if (swiftCodeDetailsRepo.count() > 0) {
            return;
        }

        log.info("Loading Swift Codes data from csv file");

        try {
            String resourcePath = "/data/Interns_2025_SWIFT_CODES.csv";
            List<String> columns = List.of(
                    "countryISO2",
                    "swiftCode",
                    "codeType",
                    "bankName",
                    "address",
                    "townName",
                    "countryName"
            );
            List<SwiftCodeDetails> swiftCodes = CsvUtil.loadCsvFile(
                    resourcePath,
                    SwiftCodeDetails.class,
                    columns
            );

            swiftCodeDetailsRepo.saveAll(swiftCodes);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private SwiftCodeDetails getSwiftCodeDetailsBySwiftCode(String swiftCode) {
        return swiftCodeDetailsRepo.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new RuntimeException("Invalid swift code"));
    }

}
