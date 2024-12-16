package com.crqs.bankapplication.aggregate.queries;

public class FindAccountQuery {

    private final String accountId;

    public FindAccountQuery(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
