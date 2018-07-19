package basic;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * https://github.com/google/guice/wiki/LinkedBindings
 */
public class LinkedBindingMain {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(CreditCardProcessor.class).to(KakaoPayCreditCardProcessor.class);

                bind(TransactionLog.class).to(DatabaseTransactionLog.class);
                bind(DatabaseTransactionLog.class).to(MySqlDatabaseTransactionLog.class);
            }
        });

        System.out.println("// -------------------------------------------------------------------------");
        System.out.println("## injector.getInstance(BillingService.class)");
        BillingService billingService = injector.getInstance(BillingService.class);
        billingService.pay();
        System.out.println(" ----------------------------------------------------------------------------// \n\n");
    }
}
