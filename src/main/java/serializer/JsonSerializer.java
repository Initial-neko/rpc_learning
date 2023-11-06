package serializer;

import java.lang.reflect.Method;

public class JsonSerializer {


    public static String parseToJson(Object obj){

        final Class<?> aClass = obj.getClass();
        final Method[] methods = aClass.getDeclaredMethods();
        for(Method method: methods){
            if(method.getName().startsWith("set")){

            }
        }
        return null;
    }

    public static String parseToObject(String json){
        return "";
    }
}
