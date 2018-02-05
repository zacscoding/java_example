## Java Test!

### Index

- <a href="#reflection">Reflection</a>


<div id="reflection"></div>

### Reflection

#### Display Class Inform (Methods, Field, Annotation)

> ReflectionTestDoamin.java

```aidl
package domain;

import annotation.ReflectionAnnotataion;
import java.util.List;

public class ReflectionTestDomain {

    @ReflectionAnnotataion
    private String name;
    private int age;
    private List<String> hobbies;
    private String job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {

        this.job = job;
    }

    @Override
    public String toString() {
        String hobbiesVals = "";
        if (hobbies == null) {
            hobbiesVals = "null";
        } else {
            for (String hobby : hobbies) {
                hobbiesVals += hobby + " ";
            }
        }
        return "ReflectionTestDomain{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", hobbies=" + hobbiesVals +
            ", job='" + job + '\'' +
            '}';
    }
}
```

> ReflectionAnnotataion.java

```aidl
package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReflectionAnnotataion {

    String value() default "default-value";
}
```

> Display  

```aidl
package reflection;

import domain.ReflectionTestDomain;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.junit.Test;
import util.CustomPrinter;

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
```

> Result

```aidl
## Display Methods
## toString() annotations : empty
## getName() annotations : empty
## setName(java.lang.String) annotations : empty
## getAge() annotations : empty
## getHobbies() annotations : empty
## setJob(java.lang.String) annotations : empty
## getJob() annotations : empty
## setHobbies(java.util.List) annotations : empty
## setAge(int) annotations : empty
#######################################
## Display Fields
## name : name, class name : java.lang.String, annotation : org.springframework.lang.Nullable
## name : age, class name : int, annotation : empty
## name : hobbies, class name : java.util.List, annotation : empty
## name : job, class name : java.lang.String, annotation : empty
#######################################
```

> Dynamic Instance by using reflection

```aidl
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
```

> Result

```aidl
## Dynamic instance : ReflectionTestDomain{name='default-value', age=10, hobbies=Default value! , job='Default value!'}
```
