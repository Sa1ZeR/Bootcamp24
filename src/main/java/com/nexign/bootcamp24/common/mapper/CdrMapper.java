package com.nexign.bootcamp24.common.mapper;

import com.nexign.bootcamp24.common.domain.dto.CdrDto;
import com.nexign.bootcamp24.common.domain.entity.CallTransaction;
import com.nexign.bootcamp24.common.utils.TimeUtils;
import org.springframework.stereotype.Component;

@Component
public class CdrMapper {

    /**
     * Преобразовывает cdr dto в строковую запись
     * @param cdr dto
     * @return преобразованная строка
     */
    public String cdrToString(CdrDto cdr) {
        return String.format("%s,%s,%s,%s",
                cdr.callType().getCode(),
                cdr.phoneNumber(),
                cdr.startDate(),
                cdr.endDate());
    }

    /**
     * Преобразовывает транзакцию в cdr запись
     * @param transaction транзакция звонка
     * @return cdr запись
     */
    public CdrDto transactionToCdr(CallTransaction transaction) {
        return new CdrDto(transaction.getCallType(),
                transaction.getCustomer().getPhone(),
                TimeUtils.toUnixTime(transaction.getDateStart()),
                TimeUtils.toUnixTime(transaction.getDateEnd()));
    }
}
