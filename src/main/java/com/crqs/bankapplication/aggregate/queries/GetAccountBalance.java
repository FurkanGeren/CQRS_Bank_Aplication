package com.crqs.bankapplication.aggregate.queries;

public class GetAccountBalance {

    private final String accountId;

    public GetAccountBalance(String accountId) {
        this.accountId = accountId;
    }


    public String getAccountId() {
        return accountId;
    }
}
