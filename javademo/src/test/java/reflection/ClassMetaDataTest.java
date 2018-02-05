package reflection;

import annotation.ReflectionAnnotataion;
import domain.ReflectionTestDomain;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import util.CustomPrinter;

/**
 * @author zacconding
 * @Date 2018-02-05
 * @GitHub : https://github.com/zacscoding
 */
public class ClassMetaDataTest {

    @Test
    public void display() {
        Class clazz = ReflectionTestDomain.class;
        Method[] methods = clazz.getDeclaredMethods();
        CustomPrinter.println("## Display Methods");
        for (Method method : methods) {
            String methodName = "## " + method.getName();
            Class[] params = method.getParameterTypes();
            Annotation[] annotations = method.getAnnotations();
            String annotationVals = " annotations : " + getAnnotationInfo(annotations);
            String paramVals = "(";
            for (Class param : params) {
                paramVals += (param.getName() + ", ");
            }
            if (params.length > 0) {
                paramVals = paramVals.substring(0, paramVals.length() - 2);
            }
            paramVals += ")";
            CustomPrinter.println(methodName + paramVals + annotationVals);
        }
        CustomPrinter.println("#######################################");

        CustomPrinter.println("## Display Fields");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            String annotationVals = getAnnotationInfo(annotations);
            CustomPrinter.println("## name : {}, class name : {}, annotation : {}", field.getName(), field.getType().getName(), annotationVals);
        }
        CustomPrinter.println("#######################################");
    }

    @Test
    public void createInstanceByUsingReflection() {
        try {
            Class clazz = ReflectionTestDomain.class;
            // create default instance
            ReflectionTestDomain inst = (ReflectionTestDomain) clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // check annotation!
                ReflectionAnnotataion annotation = field.getAnnotation(ReflectionAnnotataion.class);
                field.setAccessible(true);
                if (annotation != null) {
                    field.set(inst, annotation.value());
                } else {
                    // check generic type such as List<String>
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        if (field.getType().isAssignableFrom(java.util.List.class)) {
                            ParameterizedType types = (ParameterizedType) genericType;
                            Type type = types.getActualTypeArguments()[0];
                            Object value = getValue(Class.forName(type.getTypeName()));
                            List list = new ArrayList();
                            list.add(value);
                            field.set(inst, list);
                        }
                    } else {
                        field.set(inst, getValue(field.getType()));
                    }
                }
            }
            CustomPrinter.println("## Dynamic instance : " + inst.toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object getValue(Class clazz) {
        if (clazz == int.class) {
            return Integer.valueOf(10);
        } else if (clazz == String.class) {
            return "Default value!";
        }

        return null;
    }

    private String getAnnotationInfo(Annotation[] annotations) {
        String annotationVals = null;
        if (annotations.length == 0) {
            annotationVals = "empty";
        } else {
            annotationVals = "";
        }

        for (Annotation annotation : annotations) {
            annotationVals += annotation.annotationType().getName() + " ";
        }
        return annotationVals;
    }
}
