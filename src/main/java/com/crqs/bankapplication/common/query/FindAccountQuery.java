package com.crqs.bankapplication.common.query;

public class FindAccountQuery {

    private final String accountId;

    public FindAccountQuery(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
