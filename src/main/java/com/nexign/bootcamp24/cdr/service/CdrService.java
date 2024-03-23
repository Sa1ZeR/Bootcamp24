package com.nexign.bootcamp24.cdr.service;

import com.nexign.bootcamp24.common.domain.dto.CdrDto;
import com.nexign.bootcamp24.common.mapper.CdrMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdrService {

    private final CdrMapper cdrMapper;

    @Value("${cdr.file-name}")
    private String cdrFileName;

    /**
     * Данный метод генерирует CDR файл со всеми звонками
     */
    public void generateCdrFile(List<CdrDto> cdrDtoList) {
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(cdrFileName))) {
            cdrDtoList.forEach(cdr -> {
                String cdrLine = cdrMapper.cdrToString(cdr);
                try {
                    bufferedWriter.write(cdrLine + "\n");
                } catch (IOException e) {
                    log.error("Error while writing cdr: ", e);
                }
            });
        } catch (IOException e) {
            log.error("Error while saving cdr file: ", e);
        }
    }
}
