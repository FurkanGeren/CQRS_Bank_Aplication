package com.crqs.bankapplication.aggregate.queries;
public class FindAccountHistoryQuery {
    private final String accountId;

    public FindAccountHistoryQuery(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
