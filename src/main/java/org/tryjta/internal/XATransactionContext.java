package org.tryjta.internal;

import com.arjuna.ats.arjuna.common.Uid;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.xa.Xid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class XATransactionContext {


    private final Xid transactionId;

    private String filename;

    XATransactionContext(Xid transactionId) {
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
        Path newFilePath = Paths.get(filename);
        Files.createFile(newFilePath);
        log.info("File {} created.", filename);
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

    public void rollback(boolean recovering) throws IllegalStateException, IOException, InterruptedException {
        Path newFilePath = Paths.get(filename);
        Thread.sleep(2000l);
        Files.deleteIfExists(newFilePath);
        log.info("File {} deleted.", filename);
    }

    static class TransactionTimeoutException extends RuntimeException {
        private static final long serialVersionUID = -4629992436523905812L;
    }

}