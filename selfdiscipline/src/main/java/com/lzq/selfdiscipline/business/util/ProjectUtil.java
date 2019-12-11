package com.lzq.selfdiscipline.business.util;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 项目工具类
 * 说明：用于项目开发中一下常用判断、转换等
 */
public class ProjectUtil {
    private ProjectUtil() {
    }

    /**
     * 判断对象是否为空
     *
     * @param obj
     * @return 如果为空，返回true，否则为false
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return StringUtils.isEmpty(obj);
        } else if (obj instanceof Collection) {
            return ((Collection) obj).size() == 0;
        } else if (obj instanceof Map) {
            return ((Map) obj).size() == 0;
        } else if (obj instanceof String[]) {
            return ((String[]) obj).length == 0;
        } else if (obj instanceof Integer[]) {
            return ((Integer[]) obj).length == 0;
        } else if (obj instanceof Double[]) {
            return ((Double[]) obj).length == 0;
        } else if (obj instanceof Float[]) {
            return ((Float[]) obj).length == 0;
        } else if (obj instanceof Character[]) {
            return ((Character[]) obj).length == 0;
        } else if (obj instanceof Boolean[]) {
            return ((Boolean[]) obj).length == 0;
        } else if (obj instanceof Short[]) {
            return ((Short[]) obj).length == 0;
        } else if (obj instanceof Long[]) {
            return ((Long[]) obj).length == 0;
        } else if (obj instanceof Byte[]) {
            return ((Byte[]) obj).length == 0;
        }
        return false;
    }

    public static boolean isEmpty(String obj) {
        return StringUtils.isEmpty(obj);
    }

    public static boolean isEmpty(Collection<?> obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(Map<?, ?> obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(Integer[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isEmpty(Double[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isEmpty(Short[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isEmpty(Long[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isEmpty(Float[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isEmpty(Byte[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isEmpty(Boolean[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isEmpty(Character[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }


    public static boolean isNotEmpty(String obj) {
        return !StringUtils.isEmpty(obj);
    }

    public static boolean isNotEmpty(Collection<?> obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Map<?, ?> obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Integer[] obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Double[] obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Short[] obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Long[] obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Float[] obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Byte[] obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Boolean[] obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Character[] obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断 两个对象是否相等：equals
     * @param obj 对象1
     * @param obj2 对象2
     * @return 相等
     */
    public static boolean areEqual(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        } else {
            return obj.equals(obj2);
        }
    }

    /**
     * 判断 两个字符串不考虑字母大小写是否相等：equals
     * @param obj 对象1
     * @param obj2 对象2
     * @return 相等
     */
    public static boolean areEqualIgnoreCase(String obj, String obj2) {
        if (obj == null) {
            return obj2 == null;
        } else {
            return obj.equalsIgnoreCase(obj2);
        }
    }

}
