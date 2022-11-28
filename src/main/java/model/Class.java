package model;

import java.util.List;

public class Class {
    private String className;
    private List<String> methodName;
    private String packageName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getMethodName() {
        return methodName;
    }

    public void addMethodName(String method) {
        methodName.add(method);
    }

    public void setMethodName(List<String> methodName) {
        this.methodName = methodName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
