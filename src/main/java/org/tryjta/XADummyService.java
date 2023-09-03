package org.tryjta;

import com.arjuna.ats.jta.transaction.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import org.tryjta.internal.DummyXAResourceManager2;
import org.tryjta.internal.XATransactionContextFactory2;

@ApplicationScoped
public class XADummyService {
    @Inject
    XATransactionContextFactory2 factory2;
    @Inject
    DummyXAResourceManager2 dummyXAResourceManager2;
    @Inject
    TransactionManager transactionManager;

    public void writeOnConsole(String field) throws SystemException, RollbackException {
        if(transactionManager.getTransaction() == null)
            throw new RuntimeException("DummyService.write must be called inside a transaction, add @Transactional to your method.");
        factory2.createTransactionContext(((Transaction) transactionManager.getTransaction()).getTxId());
        transactionManager.getTransaction().enlistResource(dummyXAResourceManager2.buildDummyXAResource());
        factory2.get(((Transaction) transactionManager.getTransaction()).getTxId()).setFilename(field);
    }
}
