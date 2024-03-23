package com.nexign.bootcamp24.udr.service;

import com.nexign.bootcamp24.common.domain.dto.ParsedCdrDto;
import com.nexign.bootcamp24.common.domain.dto.UdrDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportGeneratorService {

    private final UdrService udrService;

    /**
     * Генерирует полный отчет со всеми данными
     * @return udr data для всех абонентов за все месяца
     */
    public Map<String, Map<Integer, UdrDto>> generateReport() {
        Map<String, Map<Integer, UdrDto>> monthToUrdData = getDataFromCdr();

        printHeader();
        //отобразить информацию для всех абонентов по всем месяцам
        monthToUrdData.forEach((number, data) ->
                data.forEach((month, dto) -> printUdr(dto, month)));

        return monthToUrdData;
    }

    /**
     * Генерирует полный отчет по переданному номеру
     * @param msisdn - номер абонента
     * @return udr данные абонента за все месяца
     */
    public Map<String, Map<Integer, UdrDto>> generateReport(String msisdn) {
        Map<String, Map<Integer, UdrDto>> data = getDataFromCdr();

        //отобразить информацию по текущему абоненту;
        Map<Integer, UdrDto> userData = data.get(msisdn);
        if(userData == null || userData.isEmpty()){
            log.error(String.format("Customer with %s msisdn not found", msisdn));
        } else {
            printHeader();
            userData.forEach((month, dto) ->
                    printUdr(dto, month));
        }

        return data;
    }

    /**
     *
     * @param msisdn
     * @param month
     * @return
     */
    public Map<String, Map<Integer, UdrDto>> generateReport(String msisdn, int month) {
        Map<String, Map<Integer, UdrDto>> data = getDataFromCdr();

        //отобразить информацию по текущему абоненту;
        Map<Integer, UdrDto> userData = data.get(msisdn);
        if(userData == null || userData.isEmpty()){
            log.error(String.format("Customer with %s msisdn not found", msisdn));
        } else {
            UdrDto dtoByMonth = userData.get(month);
            if(dtoByMonth == null) {
                log.error(String.format("udr for %s month not found", month));
            } else {
                printHeader();
                printUdr(dtoByMonth, month);
            }
        }

        return data;
    }

    /**
     * получение данные из cdr
     * запуск чтения из файла и последующий подсчет разговоров за месяц
     * @return udr данные за все месяца всех пользователей
     */
    private Map<String, Map<Integer, UdrDto>> getDataFromCdr() {
        List<ParsedCdrDto> cdrList = udrService.parseCdrFile();
        Map<String, Map<Integer, UdrDto>> monthToUrdData = udrService.calculateTotalCallTime(cdrList);

        udrService.writeUdrData(monthToUrdData);

        return monthToUrdData;
    }

    /**
     * вывод заголовка таблицы
     */
    private void printHeader() {
        //отступ от логов спринга
        log.info("");
        log.info("");
        log.info("");
        log.info("==================================================");
        log.info("=  customer  === month === incoming === outgoing =");
    }

    /**
     * вывод udr записи в консоль
     * @param dto запись
     * @param month месяц udr записи
     */
    private void printUdr(UdrDto dto, int month) {
        log.info(String.format(" %s ===   %s   === %s === %s =",
                dto.msisdn(),
                month,
                dto.incomingCall().totalTime(),
                dto.outcomingCall().totalTime()
        ));
    }
}
