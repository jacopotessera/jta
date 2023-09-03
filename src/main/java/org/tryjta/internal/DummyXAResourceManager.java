package org.tryjta.internal;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public final class DummyXAResourceManager {
    @Inject
    XATransactionContextFactory factory;


    public int getCommitedTransactionsCounter() {
        final DummyXAResource dummyXAResource = new DummyXAResource(factory);
        return dummyXAResource.getCommitedTransactionsCounter();
    }
    public DummyXAResource buildDummyXAResource(){
        return new DummyXAResource(factory);
    }

}