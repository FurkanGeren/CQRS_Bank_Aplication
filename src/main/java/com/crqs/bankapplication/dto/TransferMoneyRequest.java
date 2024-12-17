package com.crqs.bankapplication.dto;

import java.math.BigDecimal;

public record TransferMoneyRequest(String fromAccount, String toAccount, String description, BigDecimal amount) {
}
