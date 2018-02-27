package cn.localhost01;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class ReflectUtil {

    public static void setFieldValue(Object target, String fname, Object fvalue) throws SqlAdderException {
        // 如果类型不匹配，直接退出
        if (target == null || fname == null || "".equals(fname))
            return;

        Class<?> clazz = target.getClass();

        try { // 通过set方法设置类属性值
            String methodname = "set" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
            Method method = clazz.getDeclaredMethod(methodname, fvalue.getClass()); // 获取定义的方法

            if (!Modifier.isPublic(method.getModifiers())) // 设置非共有方法权限
                method.setAccessible(true);

            // 执行方法回调
            method.invoke(target, fvalue);
        } catch (Exception e) {// 如果set方法不存在，则直接设置类属性值
            try {
                Field field = clazz.getDeclaredField(fname); // 获取定义的类属性

                if (!Modifier.isPublic(field.getModifiers())) // 设置非共有类属性权限
                    field.setAccessible(true);

                field.set(target, fvalue); // 设置类属性值
            } catch (Exception ex) {
                throw new SqlAdderException("设置属性值时错误", ex);
            }

        }
    }

    public static Object getFieldValue(Object target, String fname) throws SqlAdderException {

        if (target == null || fname == null || "".equals(fname))
            return null;

        Class<?> clazz = target.getClass();

        try { // 通过get方法获取类属性值
            String methodname = "get" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
            Method method = clazz.getDeclaredMethod(methodname); // 获取定义的方法

            if (!Modifier.isPublic(method.getModifiers())) // 设置非共有方法权限
                method.setAccessible(true);

            return method.invoke(target); // 方法回调，返回值
        } catch (Exception e) {// 如果get方法不存在，则直接获取类属性值
            try {
                Field field = clazz.getDeclaredField(fname); // 获取定义的类属性

                if (!Modifier.isPublic(field.getModifiers())) // 设置非共有类属性权限
                    field.setAccessible(true);

                return field.get(target);// 返回类属性值
            } catch (Exception ex) {
                throw new SqlAdderException("获取属性值时错误", ex);
            }

        }
    }
}
