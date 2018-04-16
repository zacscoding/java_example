package util;

import dto.Pair;
import java.util.HashMap;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 * @author zacconding
 * @Date 2018-04-08
 * @GitHub : https://github.com/zacscoding
 */
public class HashMapNoResizeTest extends ClassLoader {

    @BeforeClass
    public static void test2() throws Exception {
        String className = "java.util.HashMap";
        ClassLoader cl = HashMapNoResizeTest.class.getClassLoader();
        ClassReader cr = new ClassReader(cl.getResourceAsStream(className.replace('.', '/') + ".class"));
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new HashMapClassVisitor();
        cr.accept(cv, 0);
        byte[] bytes = cw.toByteArray();

        Map<String, Integer> map = newHashMapForNoResize(2);
        System.out.println("try put 1");
        map.put("test1", 1);
        System.out.println("try put 2");
        map.put("test1", 2);
        System.out.println("try put 3");
        map.put("test1", 3);
        System.out.println("try put 4");
        map.put("test1", 4);

        Pair<String, String> pair = new Pair<>("test", "test");
    }

    @Test
    public void test() {
        System.out.println("test");
    }

    private static <K, V> Map<K, V> newHashMapForNoResize(int size) {
        final float loadFactor = 0.99999f;
        int threshold = (int) Math.ceil((size + 1) * loadFactor);
        return new HashMap<K, V>(threshold, loadFactor);
    }
}

class HashMapClassVisitor extends ClassVisitor {

    public HashMapClassVisitor() {
        super(Opcodes.ASM5);
    }

    public HashMapClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        if ("resize".equals(name) && "()V".equals(desc)) {
            System.out.println("## Find target :: " + name);
            mv = new ProxyMethodVisitor(access, desc, mv);
        }

        return mv;
    }
}

class ProxyMethodVisitor extends LocalVariablesSorter implements org.objectweb.asm.Opcodes {

    protected ProxyMethodVisitor(int access, String desc, MethodVisitor mv) {
        super(ASM5, access, desc, mv);
    }

    @Override
    public void visitCode() {
        System.out.println("visit code..");
        mv.visitCode();
    }
}
