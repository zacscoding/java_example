package tutorials.injections.method;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_method_injection.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class MethodInjectionMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check method injection ..");

        Injector injector = Guice.createInjector(new TextEditorModule());
        TextEditor editor = injector.getInstance(TextEditor.class);
        editor.makeSpellCheck();

        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}

@ImplementedBy(SpellCheckerImpl.class)
interface SpellChecker {

    void checkSpelling();
}

class SpellCheckerImpl implements SpellChecker {

    private String dbUrl;

    public SpellCheckerImpl() {
        System.out.println("## SpellCheckerImpl() is called");
    }

    @Inject
    public void setDbUrl(@Named("JDBC") String dbUrl) {
        this.dbUrl = dbUrl;
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

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("JDBC")).toInstance("jdbc:mysql/localhost:5326/emp");
    }
}