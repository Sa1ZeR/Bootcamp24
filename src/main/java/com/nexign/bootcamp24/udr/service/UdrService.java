package com.nexign.bootcamp24.udr.service;

import com.nexign.bootcamp24.common.domain.dto.CallDto;
import com.nexign.bootcamp24.common.domain.dto.CdrDto;
import com.nexign.bootcamp24.common.domain.dto.ParsedCdrDto;
import com.nexign.bootcamp24.common.domain.dto.UdrDto;
import com.nexign.bootcamp24.common.domain.enums.CallType;
import com.nexign.bootcamp24.common.utils.GsonUtils;
import com.nexign.bootcamp24.common.utils.IOUtils;
import com.nexign.bootcamp24.common.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UdrService {

    @Value("${cdr.file-name}")
    private String cdrFileName;

    /**
     * Считывает данные из cdr файла
     * @return список cdr записей
     */
    public List<ParsedCdrDto> parseCdrFile() {
        List<ParsedCdrDto> cdrList = new ArrayList<>();
        try {
            //читаем данные из файла
            List<String> strings = Files.readAllLines(Paths.get(cdrFileName));

            //преобразовываем данные в cdr запись
            strings.forEach(line -> {
                String[] splitData = line.split(",");
                if(splitData.length < 4) throw new RuntimeException(String.format("Incorrect data %s", line));

                CallType callType = CallType.getType(splitData[0]);
                String phone = splitData[1];
                long startDate = Long.parseLong(splitData[2]);
                long endDate = Long.parseLong(splitData[3]);

                cdrList.add(new ParsedCdrDto(callType, phone, TimeUtils.toLocalDateTime(startDate), TimeUtils.toLocalDateTime(endDate)));
            });

        } catch (IOException e) {
            log.error("Error while reading cdr file", e);
        }

        return cdrList;
    }

    /**
     * Производит подсчет времени в рамках одного месяца для переданных абонентов
     * @param cdrList cdr записи всех абонентов за все месяца
     * @return udr записи сгруппированные по месяцам
     */
    public Map<String, Map<Integer, UdrDto>> calculateTotalCallTime(List<ParsedCdrDto> cdrList) {
        Map<String, Map<Integer, UdrDto>> calcData = new HashMap<>(); //(абонент ->(месяц -> udr)))

        //Сгруппируем полученный результат (абонент ->(месяц -> данные за месяц)))
        Map<String, Map<Integer, List<ParsedCdrDto>>> grouped = cdrList.stream().collect(Collectors.groupingBy(ParsedCdrDto::phoneNumber,
                Collectors.groupingBy(dto -> dto.dateStart().getMonthValue())));

        //собираем данные
        grouped.forEach((phone, value) -> {
            value.forEach((month, data) -> {
                //собираем статистику для входящих звонков
                String incTotalTime = TimeUtils.getDurationString(data.stream()
                        .filter(dto -> dto.callType() == CallType.INCOMING)
                        .mapToLong(dto -> Duration.between(dto.dateStart(), dto.dateEnd()).toSeconds())
                        .sum());

                //собираем статистику для исходящих
                String outTotalTime = TimeUtils.getDurationString(data.stream()
                        .filter(dto -> dto.callType() == CallType.OUTGOING)
                        .mapToLong(dto -> Duration.between(dto.dateStart(), dto.dateEnd()).toSeconds())
                        .sum());

                //получаем данные по абоненту. если нет, то создаем новые
                Map<Integer, UdrDto> monthToUdr = calcData.getOrDefault(phone, new HashMap<>());
                //добавляем запись по месяцу
                monthToUdr.put(month, new UdrDto(phone, new CallDto(incTotalTime), new CallDto(outTotalTime)));
                //save
                calcData.put(phone, monthToUdr);
            });
        });

        return calcData;
    }

    /**
     * Данный метод создает отчеты в папке /reports
     * @param data данные на запись
     */
    public void writeUdrData(Map<String, Map<Integer, UdrDto>> data) {
        log.info("Start saving udr data");

        //удалить /reports, если она есть. чтоб не перемешивались отчеты
        IOUtils.deleteDir(Paths.get("reports"));
        data.forEach((phone, monthData) -> {
            monthData.forEach((month, udr) -> {
                Path file = Paths.get(String.format("reports/%s_%s.json", udr.msisdn(), month));
                IOUtils.createParentDir(file);
                IOUtils.writeData(file, GsonUtils.toJson(udr));
            });
        });

        log.info("Udr data successfully saved");
    }
}
