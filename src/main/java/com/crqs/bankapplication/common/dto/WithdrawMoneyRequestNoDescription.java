package com.crqs.bankapplication.common.dto;

import java.math.BigDecimal;

public record WithdrawMoneyRequestNoDescription(String accountId, BigDecimal amount) {
}
