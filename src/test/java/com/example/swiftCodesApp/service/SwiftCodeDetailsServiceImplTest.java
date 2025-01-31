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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SwiftCodeDetailsServiceImplTest {
    @Spy
    private SwiftCodeDetailsMapper swiftMapper = Mappers.getMapper(SwiftCodeDetailsMapper.class);
    @Mock
    private SwiftCodeDetailsRepo swiftCodeDetailsRepo;
    @InjectMocks
    private SwiftCodeDetailsServiceImpl underTest;

    @Test
    void addSwiftCodeDetails() {
        SwiftCodeDetailsReq req = getSampleSwiftCodeDetailsReq();

        when(swiftCodeDetailsRepo.save(any(SwiftCodeDetails.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(underTest.addSwiftCodeDetails(req))
                .isEqualTo(swiftMapper.toSwiftCodeDetailsDto(swiftMapper.toSwiftCodeDetails(req)));
    }

    @Test
    void willThrowWhenSwiftCodeAlreadyExists() {
        when(swiftCodeDetailsRepo.existsBySwiftCode(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> underTest.addSwiftCodeDetails(getSampleSwiftCodeDetailsReq()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("This Swift code already exists");
    }

    @Test
    void deleteSwiftCodeDetails() {
        SwiftCodeDetails details = getSampleSwiftCodeDetails(false);

        when(swiftCodeDetailsRepo.findBySwiftCode(details.getSwiftCode()))
                .thenReturn(Optional.of(details));
        underTest.deleteSwiftCodeDetails(details.getSwiftCode());

        verify(swiftCodeDetailsRepo).delete(details);
    }

    @Test
    void willThrowWhenSwiftCodeToBeDeletedDoesNotExist() {
        String sampleSwiftCode = "abcdefghijk";

        assertThatThrownBy(() -> underTest.deleteSwiftCodeDetails(sampleSwiftCode))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Swift code not found");
    }

    @Test
    void getCountrySwiftCodes() {
        SwiftCodeDetails details = getSampleSwiftCodeDetails(false);

        when(swiftCodeDetailsRepo.findByCountryISO2(details.getCountryISO2()))
                .thenReturn(List.of(details));

        CountrySwiftCodesListDto result = underTest.getCountrySwiftCodes(details.getCountryISO2());

        assertThat(result.countryISO2()).isEqualTo(details.getCountryISO2());
        assertThat(result.countryName()).isEqualTo(details.getCountryName());
        assertThat(result.swiftCodes()).isEqualTo(
                List.of(
                        swiftMapper.toCountrySwiftCodeDto(details)
                )
        );
    }

    @Test
    void willThrowWhenSwiftCodesAreEmpty() {
        String sampleISO2 = "pl";
        assertThatThrownBy(() -> underTest.getCountrySwiftCodes(sampleISO2))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Swift code data for countryISO2 code %s not found".formatted(sampleISO2));
    }


    @Test
    void getSwiftCodeDetailsForBranch() {
        SwiftCodeDetails details = getSampleSwiftCodeDetails(false);

        when(swiftCodeDetailsRepo.findBySwiftCode(details.getSwiftCode()))
                .thenReturn(Optional.of(details));

        SwiftCodeDetailsDto result = underTest.getSwiftCodeDetails(details.getSwiftCode());

        assertThat(result).isEqualTo(swiftMapper.toSwiftCodeDetailsDto(details));
    }

    @Test
    void getSwiftCodeDetailsForHeadquarters() {
        SwiftCodeDetails headquarters = getSampleSwiftCodeDetails(true);
        SwiftCodeDetails branch = getSampleSwiftCodeDetails(false);
        HeadquartersDetailsDto expected = swiftMapper.toHeadquartersSwiftCodeDetailsDto(headquarters);
        expected.setBranches(
                List.of(
                        swiftMapper.toSwiftCodeDetailsDto(branch)
                )
        );

        when(swiftCodeDetailsRepo.findBySwiftCode(headquarters.getSwiftCode()))
                .thenReturn(Optional.of(headquarters));

        when(swiftCodeDetailsRepo.findBranches(headquarters.getSwiftCode()))
                .thenReturn(List.of(branch));

        SwiftCodeDetailsDto result = underTest.getSwiftCodeDetails(headquarters.getSwiftCode());

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void willThrowWhenSwiftCodeDoesNotExist() {
        String sampleSwiftCode = "abcdefghijk";
        assertThatThrownBy(() -> underTest.getSwiftCodeDetails(sampleSwiftCode))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Swift code not found");
    }

    private SwiftCodeDetailsReq getSampleSwiftCodeDetailsReq() {
        return new SwiftCodeDetailsReq(
                "sample-address",
                "sample-bank-name",
                "pl",
                "poland",
                false,
                "abcdefghijk"
        );
    }

    private SwiftCodeDetails getSampleSwiftCodeDetails(boolean isHeadquarters) {
        String swiftCode = isHeadquarters ? "abcdefghXXX" : "abcdefghijk";

        return new SwiftCodeDetails(
                1L,
                "sample-address",
                "sample-bank-name",
                "pl",
                "poland",
                swiftCode
        );
    }
}