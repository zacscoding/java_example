package basic.bindingannotations;

import basic.CreditCardProcessor;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class BindingAnnotationMain {

    public static void main(String[] args) {
//        Injector injector = Guice.createInjector(new AbstractModule() {
//            @Override
//            protected void configure() {
//                bind(CreditCardProcessor.class).annotatedWith(Names.named("Checkout")).to(CheckoutCreditCardProcessor.class);
//            }
//        });

    }
}
