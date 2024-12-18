package com.crqs.bankapplication.common.dto;

import java.math.BigDecimal;

public record WithdrawMoneyRequest(String accountId, BigDecimal amount) {
}
