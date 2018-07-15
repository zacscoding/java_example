package util;

/**
 * @author zacconding
 * @Date 2018-07-14
 * @GitHub : https://github.com/zacscoding
 */
public class DemoTask {

    public static void doTask(String title, DoTask task) {
        System.out.println("// ======================================================== ");
        System.out.println("----> Start to " + title);
        try {
            task.doSomething();
        } catch (Exception e) {
            SimpleLogger.error("failed to " + title, e);
        }

        System.out.println("=========================================================== //\n");
    }

    @FunctionalInterface
    public interface DoTask {

        void doSomething() throws Exception;
    }

    private void taskSomething() {
        System.out.println("do something...");
    }

}
