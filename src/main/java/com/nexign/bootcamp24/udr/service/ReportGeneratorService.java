package com.nexign.bootcamp24.udr.service;

import com.nexign.bootcamp24.common.domain.dto.CdrDto;
import com.nexign.bootcamp24.common.domain.dto.ParsedCdrDto;
import com.nexign.bootcamp24.common.domain.dto.UdrDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportGeneratorService {

    private final UdrService udrService;

    /**
     * Генерирует полный отчет
     */
    public void generateReport() {
        List<ParsedCdrDto> cdrList = udrService.parseCdrFile();
        Map<String, Map<Integer, UdrDto>> monthToUrdData = udrService.calculateTotalCallTime(cdrList);

        udrService.writeUdrData(monthToUrdData);
    }

    public void generateReport(String msisdn) {

    }

    public void generateReport(String msisdn, int month) {

    }
}
