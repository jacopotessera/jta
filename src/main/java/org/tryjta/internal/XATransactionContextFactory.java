package org.tryjta.internal;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.jta.xa.XidImple;
import jakarta.enterprise.context.ApplicationScoped;

import javax.transaction.xa.Xid;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class XATransactionContextFactory {

    private final Map<Uid, XATransactionContext> transactionContextMap = new ConcurrentHashMap<>();

    public XATransactionContextFactory() {
    }

    public XATransactionContext createTransactionContext(Xid transactionId) {
        XATransactionContext transactionContext = new XATransactionContext(transactionId);
        transactionContextMap.put(((XidImple) transactionId).getTransactionUid(), transactionContext);
        return transactionContext;
    }

    public XATransactionContext get(Xid transactionId) {
        return transactionContextMap.get(((XidImple) transactionId).getTransactionUid());
    }

    public void destroy(Uid transactionId) {
        transactionContextMap.remove(transactionId);
    }

    public boolean contains(Uid transactionId) {
        return transactionContextMap.containsKey(transactionId);
    }

}