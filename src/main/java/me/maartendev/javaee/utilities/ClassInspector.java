package me.maartendev.javaee.utilities;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ClassInspector {
    public static ArrayList<Method> getSetters(Class<?> c) {
        ArrayList<Method> list = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();

        for (Method method : methods) {
            if (isSetter(method)) {
                list.add(method);
            }
        }

        return list;
    }

    public static ArrayList<Method> getGetters(Class<?> c) {
        ArrayList<Method> list = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();

        for (Method method : methods) {
            if (isGetter(method)) {
                list.add(method);
            }
        }

        return list;
    }

    private static boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*") && !method.getReturnType().equals(void.class)) {
                return true;
            }

            return method.getName().matches("^is[A-Z].*") && method.getReturnType().equals(boolean.class);
        }
        return false;
    }

    private static boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*");
    }
}
