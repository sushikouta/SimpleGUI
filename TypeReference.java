package sushikouta;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

public class TypeReference<T> {
    TypeReference() {
        try {
            Type type = getClass().getGenericSuperclass();
            System.out.println(type);
            ParameterizedType ptype = (ParameterizedType) type;
            System.out.println(ptype);
        } catch (java.lang.ClassCastException e) {
            System.out.println(e);
        }
    }
}