package tutorials.bindings.constant;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_constant_bindings.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class ConstantBindingMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check constant binding..");

        Injector injector = Guice.createInjector(new TextEditorModule());
        TextEditor editor = injector.getInstance(TextEditor.class);
        editor.makeConnection();

        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}

class TextEditor {

    private String dbUrl;

    @Inject
    public TextEditor(@Named("JDBC") String dbUrl) {
        SimpleLogger.println("## TextEditor @Named(\"JDBC\") String dbUrl : {}", dbUrl);
        this.dbUrl = dbUrl;
    }

    public void makeConnection() {
        SimpleLogger.println("## >> Try to connect : {} in TextEditor", dbUrl);
    }
}

class TextEditorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("JDBC")).toInstance("jdbc:mysql://localhost:5326/emp");
    }
}