package tutorials.injections.field;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_field_injection.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class FiledInjectionMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check field injection ..");

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

    @Inject
    @Named("JDBC")
    private String dbUrl;

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
        bind(String.class).annotatedWith(Names.named("JDBC")).toInstance("jdbc:mysql://localhost:5326/emp");
    }
}