package org.tryjta;

import com.arjuna.ats.jta.transaction.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import org.tryjta.internal.DummyXAResourceManager;
import org.tryjta.internal.XATransactionContextFactory;

@ApplicationScoped
public class XACreateFileService {
    @Inject
    TransactionManager transactionManager;
    @Inject
    DummyXAResourceManager manager;
    @Inject
    XATransactionContextFactory factory;

    public void createFile(String filename) throws SystemException, RollbackException {
        if(transactionManager.getTransaction() == null)
            throw new RuntimeException("CreateFileService.createFile must be called inside a transaction, add @Transactional to your method.");
        factory.createTransactionContext(((Transaction) transactionManager.getTransaction()).getTxId());
        transactionManager.getTransaction().enlistResource(manager.buildDummyXAResource());
        factory.get(((Transaction) transactionManager.getTransaction()).getTxId()).setFilename(filename);
    }
}
