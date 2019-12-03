package com.ebupt.txcy.yellowpagelibbak.utils;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityParamValidUtil {
    /**
     * 插入数据库时候验证
     * @param o
     * @return
     */

    public static List<String> validEntityInsertList(Object o){
        Class<?> aClass = o.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        List<String> collect = Arrays.stream(declaredFields).filter(field -> {
            field.setAccessible(true);
            Column annotation = field.getAnnotation(Column.class);
            Id annotation1 = field.getAnnotation(Id.class);
            if(annotation1!=null){
                GeneratedValue annotation2 = field.getAnnotation(GeneratedValue.class);
                if(annotation2==null){
                    try {
                        Object o1 = field.get(o);
                        return  isNullOrBlank(o1,annotation);
                    } catch (IllegalAccessException e) {

                    }
                }
            }
            //不能为空，进行验证
            if (annotation!=null&&!annotation.nullable()) {
                try {
                    Object o1 = field.get(o);
                    return  isNullOrBlank(o1,annotation);
                } catch (IllegalAccessException e) {

                }
            }
            //对于可空字符的长度验证
            if(annotation!=null){
                try {
                    Object o1 = field.get(o);
                    if(o1!=null&&o1 instanceof String){
                        if(annotation!=null&&annotation.length()<((String) o1).length()){
                            return true;
                        }
                    }
                } catch (IllegalAccessException e) {

                }
            }
            return false;

        }).map(Field::getName).collect(Collectors.toList());
        return collect;
    }
    /**
     * 验证实体主键是否存在
     */
    public static List<String> validEntityUpdateList(Object o){
        Class<?> aClass = o.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        List<String> collect = Arrays.stream(declaredFields).filter(field -> {
            field.setAccessible(true);
            Column annotation = field.getAnnotation(Column.class);
            Id annotation1 = field.getAnnotation(Id.class);
            if(annotation1!=null){
                GeneratedValue annotation2 = field.getAnnotation(GeneratedValue.class);
                if(annotation2==null){
                    try {
                        Object o1 = field.get(o);
                        return  isNullOrBlank(o1,annotation);
                    } catch (IllegalAccessException e) {

                    }
                }
            }
            return false;

        }).map(Field::getName).collect(Collectors.toList());
        return collect;
    }
    public static String [] validEntityInsertArray(Object o){
        return validEntityInsertList(o).toArray(new String[0]);
    }
    public static String [] validEntityUpdateArray(Object o){
        return validEntityUpdateList(o).toArray(new String[0]);
    }
    public static boolean isNullOrBlank(Object o,Column annotation){
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            if (((String) o).trim().length() == 0) {
                return true;
            }else{
                //验证字符串长度
                if(annotation!=null&&annotation.length()<((String) o).length()){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     *
     * @param obj 验证实体属性是否存在值
     * @param excludeFields 需要排除校验的属性名
     * @return
     */
    public static Boolean isPass(Object  obj,String[]excludeFields){
        String [] excludeFieldss = Optional.ofNullable(excludeFields).orElse(new String[]{});
        Class<?> clazz = obj.getClass();
        Field[] allFields = clazz.getDeclaredFields();
       return  Arrays.asList(allFields).stream().filter(field -> {
            field.setAccessible(true);
            for(int i=0;i<excludeFieldss.length;i++){
                if(field.getName().equals(excludeFieldss[i])){
                    return false;
                }
            }
            return true;
        }).allMatch(field -> {
            try {
                Object o = field.get(obj);
                if(o==null) {
                    return false;
                }
                if(o instanceof  String){
                    return  !((String)o).trim().equals("");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return  true;
        });
    }

    /**
     * 返回为空的实体属性名
     * @param obj
     * @param excludeFields 排除的属性
     * @return
     */
    public static String[] notPassParamByexcludeField(Object  obj,String[]excludeFields){
        String [] excludeFieldss = Optional.ofNullable(excludeFields).orElse(new String[]{});
        Class<?> clazz = obj.getClass();
        Field[] allFields = clazz.getDeclaredFields();
        return  Arrays.asList(allFields).stream().filter(field -> {
            field.setAccessible(true);
            for (int i = 0; i < excludeFieldss.length; i++) {
                if (field.getName().equals(excludeFieldss[i])) {
                    return false;
                }
            }
            Object o = null;
            try {
                o = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (o == null) {
                return true;
            }
            if (o instanceof String) {
                return ((String) o).trim().equals("");
            }else{
                return false;
            }

        }).map(Field::getName).collect(Collectors.toList()).toArray(new String[0]);
    }

    /**
     * 校验字符串中的属性
     * @param obj
     * @param excludeFields
     * @return
     */
    public static String[] notPassParamByincludeField(Object  obj,String[]excludeFields){
        String [] excludeFieldss = Optional.ofNullable(excludeFields).orElse(new String[]{});
        Class<?> clazz = obj.getClass();
        Field[] allFields = clazz.getDeclaredFields();
        return  Arrays.asList(allFields).stream().filter(field -> {
            field.setAccessible(true);
            for (int i = 0; i < excludeFieldss.length; i++) {
                if (field.getName().equals(excludeFieldss[i])) {
                    Object o = null;
                    try {
                        o = field.get(obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (o == null) {
                        return true;
                    }
                    if (o instanceof String) {
                        return ((String) o).trim().equals("");
                    }
                }
            }
            return false;
        }).map(Field::getName).collect(Collectors.toList()).toArray(new String[0]);
    }


}
