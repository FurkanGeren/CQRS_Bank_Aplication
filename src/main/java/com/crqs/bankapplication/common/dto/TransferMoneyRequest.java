package com.crqs.bankapplication.common.dto;

import java.math.BigDecimal;

public record TransferMoneyRequest(String fromAccount, String toAccount, String description, BigDecimal amount) {
}
