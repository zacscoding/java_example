package tutorials.bindings.constructor;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_constructor_bindings.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class ConstructorBindingMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check constructor binding..");

        Injector injector = Guice.createInjector(new TextEditorModule());
        TextEditor editor = injector.getInstance(TextEditor.class);
        editor.makeSpellCheck();

        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}

interface SpellChecker {

    void checkSpelling();
}

class SpellCheckerImpl implements SpellChecker {

    private String dbUrl;

    public SpellCheckerImpl() {
        SimpleLogger.println("SpellCheckerImpl() is called..");
    }

    public SpellCheckerImpl(@Named("JDBC") String dbUrl) {
        this.dbUrl = dbUrl;
        SimpleLogger.println("SpellCheckerImpl(@Named(\"JDBC\") String dbUrl) is called..");
    }

    @Override
    public void checkSpelling() {
        SimpleLogger.println("Check spelling in SpellCheckerImpl\ndbUrl : {}", dbUrl);
    }
}

class TextEditor {

    private SpellChecker spellChecker;

    @Inject
    public TextEditor(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
        SimpleLogger.println("## SpellChecker inst : {} in TextEditor.", spellChecker.getClass().getName());
    }

    public void makeSpellCheck() {
        spellChecker.checkSpelling();
    }
}

class TextEditorModule extends AbstractModule {

    @Override
    protected void configure() {
        try {
            bind(SpellChecker.class).toConstructor(SpellCheckerImpl.class.getConstructor(String.class));
        } catch (NoSuchMethodException | SecurityException e) {
            SimpleLogger.error("Required constructor missing", null);
        }

        bind(String.class).annotatedWith(Names.named("JDBC")).toInstance("jdbc:mysql://localhost:5326/emp");
    }
}