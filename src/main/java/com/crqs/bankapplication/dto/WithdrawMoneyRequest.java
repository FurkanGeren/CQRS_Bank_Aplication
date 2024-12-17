package com.crqs.bankapplication.dto;

import java.math.BigDecimal;

public record WithdrawMoneyRequest(String accountId, String description, BigDecimal amount) {
}
