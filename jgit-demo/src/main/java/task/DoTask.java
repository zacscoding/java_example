package task;

/**
 * @author zacconding
 * @Date 2018-12-28
 * @GitHub : https://github.com/zacscoding
 */
public class DoTask {

    public static void step(String title, Task task) throws Exception {
        System.out.println("## ======================================================== ");
        System.out.println(title);
        task.run();
        System.out.println("## ======================================================== //\n\n");
    }

    @FunctionalInterface
    public interface Task {

        void run() throws Exception;
    }
}
