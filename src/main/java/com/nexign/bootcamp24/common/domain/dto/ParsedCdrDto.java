package com.nexign.bootcamp24.common.domain.dto;

import com.nexign.bootcamp24.common.domain.enums.CallType;

import java.time.LocalDateTime;

public record ParsedCdrDto(CallType callType, String phoneNumber, LocalDateTime dateStart, LocalDateTime dateEnd) {
}
