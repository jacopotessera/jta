package org.tryjta.internal;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public final class DummyXAResourceManager2 {
    @Inject
    XATransactionContextFactory2 factory;


    public int getCommitedTransactionsCounter() {
        final DummyXAResource2 dummyXAResource = new DummyXAResource2(factory);
        return dummyXAResource.getCommitedTransactionsCounter();
    }
    public DummyXAResource2 buildDummyXAResource(){
        return new DummyXAResource2(factory);
    }

}