package org.tryjta.internal;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.jta.xa.XidImple;
import jakarta.enterprise.context.ApplicationScoped;

import javax.transaction.xa.Xid;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class XATransactionContextFactory2 {

    private final Map<Uid, XATransactionContext2> transactionContextMap = new ConcurrentHashMap<>();

    public XATransactionContextFactory2() {
    }

    public XATransactionContext2 createTransactionContext(Xid transactionId) {
        XATransactionContext2 transactionContext = new XATransactionContext2(transactionId);
        transactionContextMap.put(((XidImple) transactionId).getTransactionUid(), transactionContext);
        return transactionContext;
    }

    public XATransactionContext2 get(Xid transactionId) {
        return transactionContextMap.get(((XidImple) transactionId).getTransactionUid());
    }

    public void destroy(Uid transactionId) {
        transactionContextMap.remove(transactionId);
    }

    public boolean contains(Uid transactionId) {
        return transactionContextMap.containsKey(transactionId);
    }

}