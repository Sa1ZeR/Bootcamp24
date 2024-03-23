package com.nexign.bootcamp24.udr.service;

import com.nexign.bootcamp24.common.domain.dto.ParsedCdrDto;
import com.nexign.bootcamp24.common.domain.dto.UdrDto;
import com.nexign.bootcamp24.common.domain.enums.CallType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UdrServiceTest {

    @Autowired
    UdrService udrService;

    @Test
    void calculateTotalCallTime() {
        List<ParsedCdrDto> cdrData = new ArrayList<>();

        String number1 = "79101952523";
        String number2 = "79124307378";

        //35 seconds
        cdrData.add(new ParsedCdrDto(CallType.INCOMING, number1,
                LocalDateTime.of(2024, 1, 5, 1, 5, 0),
                LocalDateTime.of(2024, 1, 5, 1, 5, 35)));

        //10 seconds
        cdrData.add(new ParsedCdrDto(CallType.OUTGOING, number1,
                LocalDateTime.of(2024, 1, 5, 5, 3, 0),
                LocalDateTime.of(2024, 1, 5, 5, 3, 10)));

        //25 seconds
        cdrData.add(new ParsedCdrDto(CallType.INCOMING, number1,
                LocalDateTime.of(2024, 1, 7, 1, 5, 0),
                LocalDateTime.of(2024, 1, 7, 1, 5, 25)));

        //45 seconds
        cdrData.add(new ParsedCdrDto(CallType.OUTGOING, number1,
                LocalDateTime.of(2024, 1, 9, 3, 15, 0),
                LocalDateTime.of(2024, 1, 9, 3, 15, 45)));

        //135 seconds
        cdrData.add(new ParsedCdrDto(CallType.INCOMING, number1,
                LocalDateTime.of(2024, 1, 11, 1, 5, 0),
                LocalDateTime.of(2024, 1, 11, 1, 7, 15)));

        //month 2
        //25 seconds
        cdrData.add(new ParsedCdrDto(CallType.INCOMING, number1,
                LocalDateTime.of(2024, 2, 7, 1, 5, 0),
                LocalDateTime.of(2024, 2, 7, 1, 5, 25)));

        //45 seconds
        cdrData.add(new ParsedCdrDto(CallType.OUTGOING, number1,
                LocalDateTime.of(2024, 2, 9, 3, 15, 0),
                LocalDateTime.of(2024, 2, 9, 3, 15, 45)));

        //================== user 2
        //35 seconds
        cdrData.add(new ParsedCdrDto(CallType.INCOMING, number2,
                LocalDateTime.of(2024, 1, 5, 1, 5, 0),
                LocalDateTime.of(2024, 1, 5, 1, 5, 35)));

        //10 seconds
        cdrData.add(new ParsedCdrDto(CallType.OUTGOING, number2,
                LocalDateTime.of(2024, 1, 5, 5, 3, 0),
                LocalDateTime.of(2024, 1, 5, 5, 3, 10)));

        //25 seconds
        cdrData.add(new ParsedCdrDto(CallType.INCOMING, number2,
                LocalDateTime.of(2024, 1, 7, 1, 5, 0),
                LocalDateTime.of(2024, 1, 7, 1, 5, 25)));

        Map<String, Map<Integer, UdrDto>> stringMapMap = udrService.calculateTotalCallTime(cdrData);

        Map<Integer, UdrDto> integerUdrDtoMap = stringMapMap.get(number1);
        assertAll(() -> {
            assertNotNull(integerUdrDtoMap);
            UdrDto udrDto = integerUdrDtoMap.get(1);
            assertNotNull(udrDto);
            assertEquals(number1, udrDto.msisdn());
            assertEquals("00:03:15", udrDto.incomingCall().totalTime());
            assertEquals("00:00:55", udrDto.outcomingCall().totalTime());
        });
    }
}