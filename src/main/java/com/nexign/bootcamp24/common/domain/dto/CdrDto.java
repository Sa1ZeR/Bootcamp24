package com.nexign.bootcamp24.common.domain.dto;

import com.nexign.bootcamp24.common.domain.enums.CallType;

/**
 *
 * @param callType - тип звонка
 * @param phoneNumber - номер абонента
 * @param startDate - дата и время начала звонка (Unix time);
 * @param endDate - дата и время окончания звонка (Unix time);
 */
public record CdrDto(CallType callType, String phoneNumber, long startDate, long endDate) {
}
