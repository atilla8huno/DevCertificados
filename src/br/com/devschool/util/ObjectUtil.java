package br.com.devschool.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ObjectUtil {

	@SuppressWarnings("all")
	public static Object clone(Object object) throws NegocioException {

		try {
			Class clazz = object.getClass();

			Object newObject = clazz.newInstance();

			for (Method method : clazz.getMethods()) {
				if (method.getName().startsWith("get") && !method.getName().equals("getId") && !method.getName().equals("getClass") && !Modifier.isStatic(method.getModifiers())) {
					String propertieName = method.getName().substring(3);
					Object value = method.invoke(object);
					clazz.getMethod("set" + propertieName, method.getReturnType()).invoke(newObject, value);
				}
			}

			return newObject;
		} catch (Exception e) {
			throw new NegocioException("erro.clonagemObjeto");
		}
	}
}
