package com.crqs.bankapplication.common.dto;

import java.math.BigDecimal;

public record DepositMoneyRequest(String accountId, String description, BigDecimal amount) {
}
