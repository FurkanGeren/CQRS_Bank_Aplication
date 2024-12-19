package com.crqs.bankapplication.common.dto;

import com.crqs.bankapplication.common.enums.Sex;

public record CreateCustomerRequestDTO(String firstName, String lastName, String citizenID, Sex sex, String password) {
}
