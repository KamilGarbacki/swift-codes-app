package com.example.swiftCodesApp.service;

import com.example.swiftCodesApp.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.swiftCodesApp.dto.CountrySwiftCodeDto;
import com.example.swiftCodesApp.dto.CountrySwiftCodesListDto;
import com.example.swiftCodesApp.dto.HeadquartersDetailsDto;
import com.example.swiftCodesApp.dto.SwiftCodeDetailsDto;
import com.example.swiftCodesApp.exception.ConflictException;
import com.example.swiftCodesApp.exception.NotFoundException;
import com.example.swiftCodesApp.mapper.SwiftCodeDetailsMapper;
import com.example.swiftCodesApp.model.SwiftCodeDetails;
import com.example.swiftCodesApp.repository.SwiftCodeDetailsRepo;
import com.example.swiftCodesApp.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SwiftCodeDetailsServiceImpl implements SwiftCodeDetailsService {
    private final SwiftCodeDetailsRepo swiftCodeDetailsRepo;
    private final SwiftCodeDetailsMapper swiftCodeDetailsMapper;

    private boolean isSwiftCodePresent(String swiftCode) {
        return swiftCodeDetailsRepo.existsBySwiftCode(swiftCode);
    }

    @Override
    public SwiftCodeDetailsDto addSwiftCodeDetails(SwiftCodeDetailsReq req) {
        log.info("Adding Swift code data: {}", req);

        if (isSwiftCodePresent(req.swiftCode())) {
            throw new ConflictException("This Swift code already exists");
        }

        SwiftCodeDetails newSwiftCodeDetails = swiftCodeDetailsMapper.toSwiftCodeDetails(req);
        SwiftCodeDetails savedCode = swiftCodeDetailsRepo.save(newSwiftCodeDetails);
        return swiftCodeDetailsMapper.toSwiftCodeDetailsDto(savedCode);
    }


    @Override
    public void deleteSwiftCodeDetails(String swiftCode) {
        log.info("Deleting data associated with Swift code: {}", swiftCode);
        SwiftCodeDetails toBeDeleted = getSwiftCodeDetailsBySwiftCode(swiftCode);
        swiftCodeDetailsRepo.delete(toBeDeleted);
    }

    @Override
    public CountrySwiftCodesListDto getCountrySwiftCodes(String countryISO2) {
        log.info("Getting list of Swift codes data for country of ISO2 code: {}", countryISO2);
        List<SwiftCodeDetails> swiftCodes = swiftCodeDetailsRepo.findByCountryISO2(countryISO2);

        if (swiftCodes.isEmpty()) {
            throw new NotFoundException("Swift code data for countryISO2 code %s not found".formatted(countryISO2));
        }

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
        log.info("Getting data of a single Swift code: {}", swiftCode);
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

    private SwiftCodeDetails getSwiftCodeDetailsBySwiftCode(String swiftCode) {
        return swiftCodeDetailsRepo.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new NotFoundException("Swift code not found"));
    }

    @PostConstruct
    private void loadData() {
        if (swiftCodeDetailsRepo.count() > 0) {
            return;
        }

        log.info("Loading Swift codes data from csv file");

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
}
