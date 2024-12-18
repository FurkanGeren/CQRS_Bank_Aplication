package com.crqs.bankapplication.common.query;
public class FindAccountHistoryQuery {
    private final String accountId;

    public FindAccountHistoryQuery(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
