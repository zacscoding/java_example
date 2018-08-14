package test.action;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.concurrent.TimeUnit;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class ActionMain {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new ActionModule());

        ActionListener listener = injector.getInstance(DefaultActionListener.class);
        ActionExecutioner actionExecutioner = injector.getInstance(DefaultActionExecutioner.class);

        Action a1 = new Action();
        a1.setId("1");
        a1.setServiceName("MODULE-1");
        a1.setActionType("START");
        listener.requestAction(a1);

        Action a2 = new Action();
        a2.setId("2");
        a2.setServiceName("MODULE-2");
        a2.setActionType("RESTART");
        listener.requestAction(a2);

        Action a3 = new Action();
        a3.setId("3");
        a3.setServiceName("MODULE-3");
        a3.setActionType("STOP");
        listener.requestAction(a3);

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("## Check executioner : " + actionExecutioner.isExecuting());
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();

        TimeUnit.SECONDS.sleep(10L);
    }
}
