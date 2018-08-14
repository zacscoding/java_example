package tutorials.miscellaneous.aop;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import util.SimpleLogger;

/**
 * https://www.tutorialspoint.com/guice/guice_aop.htm
 *
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class AopTestMain {

    public static void main(String[] args) {
        System.out.println(">> Check Aop <<");

        Injector injector = Guice.createInjector(new TextEditorModule());
        TextEditor editor = injector.getInstance(TextEditor.class);
        editor.makeSpellCheck();

        SimpleLogger.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface CallTracker {

}

interface SpellChecker {

    void checkSpelling();
}

class SpellCheckerImpl implements SpellChecker {

    @Override
    @CallTracker
    public void checkSpelling() {
        System.out.println("Inside checkSpelling in SpellCheckerImpl");
    }
}

class CallTrackerService implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("BEFORE : " + invocation.getMethod().getName());
        Object result = invocation.proceed();
        System.out.println("AFTER : " + invocation.getMethod().getName());

        return result;
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
        bind(SpellChecker.class).to(SpellCheckerImpl.class);

        bindInterceptor(Matchers.any(), Matchers.annotatedWith(CallTracker.class), new CallTrackerService());
    }
}