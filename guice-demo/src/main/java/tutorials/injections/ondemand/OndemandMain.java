package tutorials.injections.ondemand;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Injector;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_on_demand_injection.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class OndemandMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check ondemand injection ..");

        Injector injector = Guice.createInjector(new TextEditorModule());
        // existing object
        SpellChecker spellChecker = new SpellCheckerImpl();
        injector.injectMembers(spellChecker);

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

    @Override
    public void checkSpelling() {
        System.out.println("## Inside checkSpelling()... : " + this.toString());
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
    }
}