package reflection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Extract class method name + desc
 *
 * @author zacconding
 * @Date 2018-02-15
 * @GitHub : https://github.com/zacscoding
 */
public class ExtractMethodDescTest {

    Map<String, List<String>> methods;

    @Test
    public void extractMethods() throws IOException {
        methods = new HashMap<>();
        ClassReader cr = new ClassReader(ExtractMethodDescTestDomain.class.getName());
        cr.accept(new ClassVisitor(Opcodes.ASM5) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (!"<init>".equals(name)) {
                    List<String> descs = methods.get(name);
                    if (descs == null) {
                        descs = new ArrayList<>();
                        methods.put(name, descs);
                    }
                    descs.add(desc);
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        }, 0);

        methods.forEach((k, v) -> {
            System.out.print("Method : " + k);
            v.forEach(desc -> {
                System.out.print(" " + desc + " ");
            });
            System.out.println();
        });
    }
}


class ExtractMethodDescTestDomain {

    public static void setId(String id) {

    }

    public static void setId(String id, Integer a) {

    }

    public static void setLogin(String login) {

    }

    public static void setStringValue(int idx, String value) {

    }

    public static void setIntegerValue(int idx, Integer value) {

    }
}