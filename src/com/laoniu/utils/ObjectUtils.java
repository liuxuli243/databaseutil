package com.laoniu.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class ObjectUtils {

	/**
	 * 判断是否是基本数据类型
	*Title: isBaseType
	*author:刘旭利
	*Description: 
	　 * @param className
	　 * @param incString
	　 * @return
	 */
	public static boolean isBaseType(Class<?> className, boolean incString) {
	    if (incString && className.equals(String.class)) {
	        return true;
	    }
	    return className.equals(Integer.class) ||
	            className.equals(int.class) ||
	            className.equals(Byte.class) ||
	            className.equals(byte.class) ||
	            className.equals(Long.class) ||
	            className.equals(long.class) ||
	            className.equals(Double.class) ||
	            className.equals(double.class) ||
	            className.equals(Float.class) ||
	            className.equals(float.class) ||
	            className.equals(Character.class) ||
	            className.equals(char.class) ||
	            className.equals(Short.class) ||
	            className.equals(short.class) ||
	            className.equals(Boolean.class) ||
	            className.equals(boolean.class);
	}
	/**
	 * 转换为指定的数据类型
	*Title: convert
	*author:刘旭利
	*Description: 
	　 * @param <T>
	　 * @param obj
	　 * @param type
	　 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> T convert(Object obj, Class<T> type) {
        if (obj != null && StringUtils.isNotBlank(obj.toString())) {
            if (type.equals(Integer.class)||type.equals(int.class)) {
                return (T)new Integer(obj.toString());
            } else if (type.equals(Long.class)||type.equals(long.class)) {
                return (T)new Long(obj.toString());
            } else if (type.equals(Boolean.class)||type.equals(boolean.class)) {
                return (T) new Boolean(obj.toString());
            } else if (type.equals(Short.class)||type.equals(short.class)) {
                return (T) new Short(obj.toString());
            } else if (type.equals(Float.class)||type.equals(float.class)) {
                return (T) new Float(obj.toString());
            } else if (type.equals(Double.class)||type.equals(double.class)) {
                return (T) new Double(obj.toString());
            } else if (type.equals(Byte.class)||type.equals(byte.class)) {
                return (T) new Byte(obj.toString());
            } else if (type.equals(Character.class)||type.equals(char.class)) {
                return (T)new Character(obj.toString().charAt(0));
            } else if (type.equals(String.class)) {
                return (T) obj;
            } else if (type.equals(BigDecimal.class)) {
                return (T) new BigDecimal(obj.toString());
            } else if (type.equals(LocalDateTime.class)) {
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return (T) LocalDateTime.parse(obj.toString());
            } else if (type.equals(Date.class)) {
                try
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    return (T) formatter.parse(obj.toString());
                }
                catch (ParseException e)
                {
                   throw new RuntimeException(e.getMessage());
                }

            }else{
                return null;
            }
        } else {
            if (type.equals(int.class)) {
                return (T)new Integer(0);
            } else if (type.equals(long.class)) {
                return (T)new Long(0L);
            } else if (type.equals(boolean.class)) {
                return (T)new Boolean(false);
            } else if (type.equals(short.class)) {
                return (T)new Short("0");
            } else if (type.equals(float.class)) {
                return (T) new Float(0.0);
            } else if (type.equals(double.class)) {
                return (T) new Double(0.0);
            } else if (type.equals(byte.class)) {
                return (T) new Byte("0");
            } else if (type.equals(char.class)) {
                return (T) new Character('\u0000');
            }else {
                return null;
            }
        }
    }
}
