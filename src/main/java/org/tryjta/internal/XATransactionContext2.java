package org.tryjta.internal;

import lombok.extern.slf4j.Slf4j;

import javax.transaction.xa.Xid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class XATransactionContext2 {


    private final Xid transactionId;

    private String filename;

    XATransactionContext2(Xid transactionId) {
        this.transactionId = transactionId;
    }

    public boolean hasTimedOut() {
        return false;
    }

    public Xid getTransactionId() {
        return transactionId;
    }

    public boolean setFilename(String filename){
        this.filename = filename;
        return true;
    }

    public int prepare() throws IllegalStateException, TransactionTimeoutException, IOException {
        if (hasTimedOut()) {
            throw new TransactionTimeoutException();
        }
        log.info("{}" , filename);
        if(filename.equals("c"))
            throw new IOException();
        if(filename.equals("e"))
            throw new IOException();
        return 1;
    }

    public void commit(boolean recovering) throws IllegalStateException, IllegalArgumentException {
    }

    public void commitInOnePhase() throws IllegalStateException, TransactionTimeoutException, IOException {
        int prepared = prepare();
        if (prepared > 0) {
            commit(false);
        }
    }

    public void rollback(boolean recovering) throws IllegalStateException, IOException {
        if(filename.equals("d"))
            throw new IOException();
        if(filename.equals("e"))
            throw new IOException();
        log.info("-{}" , filename);
    }

    static class TransactionTimeoutException extends RuntimeException {
        private static final long serialVersionUID = -4629992436523905812L;
    }

}