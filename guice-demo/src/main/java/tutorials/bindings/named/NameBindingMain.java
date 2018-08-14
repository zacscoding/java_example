package tutorials.bindings.named;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_named_binding.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class NameBindingMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check named binding..");
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

class OpenOfficeWordSpellCheckerImpl extends SpellCheckerImpl {

    @Override
    public void checkSpelling() {
        System.out.println("## Inside checkSpelling in OpenOfficeWorkSpellCheckerImpl");
    }
}

class TextEditor {

    private SpellChecker spellChecker;

    @Inject
    public TextEditor(@Named("OpenOffice") SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
        System.out.println("## @Inject SpellChecker : " + spellChecker.getClass().getName() + " in TextEditor(SpellChecker)");
    }

    public void makeSpellCheck() {
        spellChecker.checkSpelling();
    }
}

class TextEditorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SpellChecker.class).annotatedWith(Names.named("OpenOffice")).to(OpenOfficeWordSpellCheckerImpl.class);
    }
}