package tutorials.bindings.inbuilt;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.logging.Logger;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_inbuilt_bindings.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class InbuiltBindingMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check inbuilt binding..");

        Injector injector = Guice.createInjector(new TextEditorModule());
        TextEditor editor = injector.getInstance(TextEditor.class);
        editor.makeSpellCheck();

        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}

class TextEditor {

    private Logger logger;

    @Inject
    public TextEditor(Logger logger) {
        this.logger = logger;
        SimpleLogger.println("TextEditor(Logger logger) is called > Logger : {}", logger.getClass().getName());
    }

    public void makeSpellCheck() {
        logger.info("In TextEditor.makeSpellCheck() method");
    }
}

class TextEditorModule extends AbstractModule {

    @Override
    protected void configure() {
    }
}