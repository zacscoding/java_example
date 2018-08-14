package tutorials.injections.optional;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class OptionalInjectionMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check optional injection ..");

        SimpleLogger.println("## >>> Create TextEditorModule(false)");
        Injector injector = Guice.createInjector(new TextEditorModule(false));
        TextEditor editor = injector.getInstance(TextEditor.class);
        editor.makeSpellCheck();
        SimpleLogger.println("------------------------------------------------");

        SimpleLogger.println("## >>> Create TextEditorModule(true)");
        injector = Guice.createInjector(new TextEditorModule(true));
        editor = injector.getInstance(TextEditor.class);
        editor.makeSpellCheck();

        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}

@ImplementedBy(SpellCheckerImpl.class)
interface SpellChecker {

    void checkSpelling();
}

class SpellCheckerImpl implements SpellChecker {

    private String dbUrl = "jdbc:mysql://localhost:5326/emp";

    // injecting the dependency if exists
    @Inject(optional = true)
    public void setDbUrl(@Named("JDBC") String dbUrl) {
        this.dbUrl = dbUrl;
        SimpleLogger.println("## setDbUrl(@Named(\"JDBC\") String dbUrl) is called. dbUrl : {}", dbUrl);
    }

    @Override
    public void checkSpelling() {
        SimpleLogger.println("Inside checkSpelling.. \n>dbUrl : {}", dbUrl);
    }
}

class TextEditor {

    private SpellChecker spellChecker;

    @Inject
    public TextEditor(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    public void makeSpellCheck() {
        spellChecker.checkSpelling();
    }
}

class TextEditorModule extends AbstractModule {

    private boolean bindJDBC = false;

    public TextEditorModule(boolean bindJDBC) {
        super();
        this.bindJDBC = bindJDBC;
    }

    @Override
    protected void configure() {
        if (bindJDBC) {
            bind(String.class).annotatedWith(Names.named("JDBC")).toInstance("jdbc:mysql/localhost:5326/dynamic");
        }
    }
}