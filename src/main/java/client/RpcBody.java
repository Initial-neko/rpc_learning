package client;

import java.io.Serializable;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class RpcBody implements Serializable {

    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] arguments;

    public RpcBody(String methodName, Class<?>[] parameterTypes) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "RpcBody{" +
                "methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}
