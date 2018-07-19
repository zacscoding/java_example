package basic;

import com.google.inject.Inject;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-07-19
 * @GitHub : https://github.com/zacscoding
 */
public class BillingService {

    private final CreditCardProcessor creditCardProcessor;
    private final TransactionLog transactionLog;

    @Inject
    public BillingService(CreditCardProcessor creditCardProcessor, TransactionLog transactionLog) {
        this.creditCardProcessor = creditCardProcessor;
        this.transactionLog = transactionLog;
        SimpleLogger.println("## BillingService is created : {}", this);
    }


    public void pay() {
        SimpleLogger.println("pay() --> CreditCardProcessor : {} | TransactionLog : {}", creditCardProcessor, transactionLog);
    }
}