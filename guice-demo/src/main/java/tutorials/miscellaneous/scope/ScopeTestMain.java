package tutorials.miscellaneous.scope;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import java.util.Random;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class ScopeTestMain {

    public static void main(String[] args) {

        System.out.println(">> Check singleton with class level");
        Injector injector = Guice.createInjector(new TextEditorModule());
        TextEditor editor = injector.getInstance(TextEditor.class);
        System.out.println(editor.getSpellCheckerId());

        TextEditor editor1 = injector.getInstance(TextEditor.class);
        System.out.println(editor1.getSpellCheckerId());
        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");

        System.out.println(">> Check singleton with config level");

        SpellChecker configLevelChecker1 = injector.getInstance(Key.get(SpellChecker.class, Names.named("CONFIG_LEVEL")));
        SpellChecker configLevelChecker2 = injector.getInstance(Key.get(SpellChecker.class, Names.named("CONFIG_LEVEL")));
        System.out.println("1`id : " + configLevelChecker1.getId());
        System.out.println("2`id : " + configLevelChecker2.getId());
        configLevelChecker1.checkSpelling();
        configLevelChecker2.checkSpelling();

        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");

        System.out.println(">> Check singleton with method level");

        SpellChecker methodLevelChecker1 = injector.getInstance(Key.get(SpellChecker.class, Names.named("METHOD_LEVEL")));
        SpellChecker methodLevelChecker2 = injector.getInstance(Key.get(SpellChecker.class, Names.named("METHOD_LEVEL")));
        System.out.println("1`id : " + methodLevelChecker1.getId());
        System.out.println("2`id : " + methodLevelChecker2.getId());
        methodLevelChecker1.checkSpelling();
        methodLevelChecker2.checkSpelling();

        SimpleLogger.println("## >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");

    }
}

interface SpellChecker {

    double getId();

    void checkSpelling();
}

@Singleton
class SpellCheckerInClassLevel implements SpellChecker {

    private double id;

    public SpellCheckerInClassLevel() {
        this.id = new Random().nextDouble();
    }

    @Override
    public double getId() {
        return this.id;
    }

    @Override
    public void checkSpelling() {
        SimpleLogger.println("Inside checkSpelling id : {} | addr : {} in SpellCheckerInClassLevel", this.id, this.toString());
    }
}

class SpellCheckerInConfigLevel implements SpellChecker {

    private double id;

    public SpellCheckerInConfigLevel() {
        this.id = new Random().nextDouble();
    }

    @Override
    public double getId() {
        return this.id;
    }

    @Override
    public void checkSpelling() {
        SimpleLogger.println("Inside checkSpelling id : {} | addr : {} in SpellCheckerInConfigLevel", this.id, this.toString());
    }
}

class SpellCheckerInMethodLevel implements SpellChecker {

    private double id;

    public SpellCheckerInMethodLevel() {
        this.id = new Random().nextDouble();
    }

    @Override
    public double getId() {
        return this.id;
    }

    @Override
    public void checkSpelling() {
        SimpleLogger.println("Inside checkSpelling id : {} | addr : {} in SpellCheckerInMethodLevel", this.id, this.toString());
    }
}

class TextEditor {

    private SpellChecker spellChecker;

    @Inject
    public void setSpellChecker(@Named("CLASS_LEVEL") SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    public void makeSpellCheck() {
        this.spellChecker.checkSpelling();
    }

    public double getSpellCheckerId() {
        return spellChecker.getId();
    }
}

class TextEditorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SpellChecker.class).annotatedWith(Names.named("CLASS_LEVEL")).to(SpellCheckerInClassLevel.class);

        bind(SpellChecker.class).annotatedWith(Names.named("CONFIG_LEVEL")).to(SpellCheckerInConfigLevel.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Named("METHOD_LEVEL")
    public SpellChecker provideConnector() {
        return new SpellCheckerInMethodLevel();
    }
}


