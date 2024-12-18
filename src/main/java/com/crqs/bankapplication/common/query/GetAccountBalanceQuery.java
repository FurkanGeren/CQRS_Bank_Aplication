package com.crqs.bankapplication.common.query;

public class GetAccountBalanceQuery {

    private final String accountId;

    public GetAccountBalanceQuery(String accountId) {
        this.accountId = accountId;
    }


    public String getAccountId() {
        return accountId;
    }
}
