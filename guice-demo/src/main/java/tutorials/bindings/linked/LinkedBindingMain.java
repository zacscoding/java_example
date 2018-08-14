package tutorials.bindings.linked;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_linked_binding.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class LinkedBindingMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check linked binding..");
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

    @Override
    public void checkSpelling() {
        System.out.println("## Inside checkSpelling in SpellCheckerImpl");
    }
}

class WinWordSpellCheckerImpl extends SpellCheckerImpl {

    @Override
    public void checkSpelling() {
        System.out.println("Inside checkSpelling in WinWordSpellCheckerImpl");
    }
}


class TextEditor {

    private SpellChecker spellChecker;

    @Inject
    public TextEditor(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
        System.out.println("## SpellChecker impl : " + spellChecker.getClass().getName() + " in TextEditor(SpellChecker)");
    }

    public void makeSpellCheck() {
        spellChecker.checkSpelling();
    }
}

class TextEditorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SpellChecker.class).to(SpellCheckerImpl.class);

        // linked binding
        bind(SpellCheckerImpl.class).to(WinWordSpellCheckerImpl.class);
    }
}