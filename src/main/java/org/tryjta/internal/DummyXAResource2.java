package org.tryjta.internal;

import jakarta.persistence.Transient;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public final class DummyXAResource2 implements XAResource {

    private static final AtomicInteger counter = new AtomicInteger();
    @Transient
    private final XATransactionContextFactory2 transactionContextFactory;

    public DummyXAResource2(XATransactionContextFactory2 transactionContextFactory) {
        this.transactionContextFactory = transactionContextFactory;
    }
    /**
     * Returns number of successfully committed transactions.
     *
     * @return Number of committed transactions.
     */
    public int getCommitedTransactionsCounter() {
        log.debug("DummyXAResource - returning counter of successfully committed transactions: " + counter.get());

        return counter.get();
    }

    public void commit(Xid xid, boolean onePhase) throws XAException {
        log.debug("DummyXAResource - commit invoked");
        transactionContextFactory.get(xid).commit(false);
        counter.incrementAndGet();
    }

    public void end(Xid xid, int flags) throws XAException {
        log.debug("DummyXAResource: {} commit.", counter.get());
        log.debug("DummyXAResource - end invoked");
    }

    public void forget(Xid xid) throws XAException {
        log.debug("DummyXAResource - forget invoked");
    }

    public int getTransactionTimeout() throws XAException {
        log.debug("DummyXAResource - returning transaction timeout: 0");

        return 0;
    }

    public boolean isSameRM(XAResource xares) throws XAException {
        log.debug("DummyXAResource - isSameRM invoked");

        return false;
    }

    public int prepare(Xid xid) throws XAException {
        log.debug("DummyXAResource - prepare invoked");
        log.debug("{}", xid);
        try {
            transactionContextFactory.get(xid).prepare();
        } catch (IOException e) {
            throw new XAException(XAException.XAER_RMERR);
        }
        return 0;
    }

    public Xid[] recover(int flag) throws XAException {
        log.debug("DummyXAResource - recover invoked");

        return null;
    }

    public void rollback(Xid xid) throws XAException {
        log.debug("DummyXAResource - rollback invoked");
        try {
            transactionContextFactory.get(xid).rollback(false);
        } catch (IOException e) {
            throw new XAException(XAException.XAER_RMERR);
        }
    }

    public boolean setTransactionTimeout(int seconds) throws XAException {
        log.debug("DummyXAResource - setTransactionTimeout invoked");

        return false;
    }

    public void start(Xid xid, int flags) throws XAException {
        log.debug("DummyXAResource - start invoked");
    }

}