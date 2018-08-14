package tutorials.bindings.annotations;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_binding_annotations.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class AnnotationBindingMain {

    public static void main(String[] args) {
        SimpleLogger.println("## >> Check annotation binding..");

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
        System.out.println("## Try to check spelling. in WinWordSpellCheckerImpl");
    }
}


class TextEditor {

    private SpellChecker spellChecker;

    @Inject
    public TextEditor(@WinWord SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
        System.out.println("## Inject SpellChecker : " + spellChecker.getClass().getName() + " in TextEditor(SpellChecker)");
    }

    public void makeSpellCheck() {
        spellChecker.checkSpelling();
    }
}

class TextEditorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SpellChecker.class).annotatedWith(WinWord.class).to(WinWordSpellCheckerImpl.class);
    }
}

@BindingAnnotation
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface WinWord {

}


