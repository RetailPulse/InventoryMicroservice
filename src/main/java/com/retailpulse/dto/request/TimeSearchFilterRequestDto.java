package com.retailpulse.dto.request;

import java.time.Instant;

public record TimeSearchFilterRequestDto(Instant startDateTime, Instant endDateTime) {
}
