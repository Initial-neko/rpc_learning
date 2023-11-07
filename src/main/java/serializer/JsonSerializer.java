package serializer;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import server.RemoteService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JsonParser中需要使用类型，通过类型来进行赋值才行
 */
public class JsonSerializer {

    private static String getField(String Field){
        return (char)(Field.charAt(0) - ('A' - 'a')) + Field.substring(1);
    }

    private static String addMarks(String str){
        return "\"" + str + "\"";
    }

    private static String deleteMarks(String str){
        if(str.charAt(0) == '{' || str.charAt(0) == '"') return str.substring(1, str.length() - 1);
        else return str;
    }

    private static String typeConvertString(Object obj) throws IllegalAccessException {
        if(obj instanceof String){
            return addMarks(obj.toString());
        }else if(obj instanceof Integer){
            return obj.toString();
        }else{
            //复杂类型等
            return parseToJson(obj);
        }
    }

    private static Object convertType(String str, Class<?> clazz){
        final String name = clazz.getSimpleName();
        if(name.equals("String")){
            return str;
        }else if(name.equals("int") || name.equals("Integer")){
            return Integer.parseInt(str);
        }else {
            return parseToObject(str, clazz);
        }
    }


    public static String parseToJson(Object obj) {

        StringBuilder jsonBuilder = new StringBuilder();

        try {
            final Class<?> clazz = obj.getClass();
            final Method[] methods = clazz.getDeclaredMethods();

            jsonBuilder.append("{");
            for (Method method : methods) {
                if (method.getName().startsWith("get")) {
                    String field = getField(method.getName().substring(3));
                    jsonBuilder
                            .append(addMarks(field)).append(":")
                            .append(typeConvertString(method.invoke(obj))).append(",");
                }
            }
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
            jsonBuilder.append("}");
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonBuilder.toString();
    }



    public static Map<String, Object> jsonToMap(String json, Class<?> clazz){
        System.out.println("jsonToMap:" + json + " class:" + clazz);

        Map<String, Class<?>> typeMap = new HashMap<>();
        for(Field field: clazz.getDeclaredFields()){
            typeMap.put(field.getName(), field.getType());
        }

        System.out.println("typeMap:" + typeMap);

        Map<String, Object> map = new HashMap<>();
        json = deleteMarks(json);
        //尝试使用正则表达式提取键值对
        String regex = "";
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        return map;
    }

    public static <T> T parseToObject(String json, Class<T> clazz){
        Object obj = null;
        try {
            final Map<String, Object> jsonMap = jsonToMap(json, clazz);
            final Method[] methods = clazz.getDeclaredMethods();
            final Constructor<?> constructor = clazz.getConstructor();
            obj = constructor.newInstance();
            for (Method method : methods) {
                if (method.getName().startsWith("set")) {
                    String field = getField(method.getName().substring(3));
                    method.invoke(obj, jsonMap.get(field));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return (T) obj;
    }

    @Data
    static class testClass{
        RemoteService remoteService;
        String name;
        int port;

        public testClass() {
        }

        public testClass(RemoteService remoteService, String name, int port) {
            this.remoteService = remoteService;
            this.name = name;
            this.port = port;
        }
    }

    public static void main(String[] args) throws Exception {
        final RemoteService service = new RemoteService("neko", "12.12.12.12", 9098, "serial", "compress");
        final String json = parseToJson(service);
        System.out.println("json:" + json);
        final RemoteService service0 = parseToObject(json, RemoteService.class);
        System.out.println(service0);

        System.out.println("------------------------------------");
        final testClass testService = new testClass(service, "testService", 90999);
        String json2 = parseToJson(testService);
        System.out.println(JSONObject.parse(json2));
        JSONObject.parse(json2);
//        System.out.println("testService json:" + json2);
//        System.out.println("testService object:" + parseToObject(json2, testClass.class));

    }
}
