package top.llk.pojo;

import java.io.Serializable;

public class OperationLog implements Serializable{
    private Integer id;

    private String operateClass;

    private String operateMethod;

    private String returnClass;

    private String operateUser;

    private String operateTime;

    private String paramAndValue;

    private Integer costTime;

    private String returnValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperateClass() {
        return operateClass;
    }

    public void setOperateClass(String operateClass) {
        this.operateClass = operateClass == null ? null : operateClass.trim();
    }

    public String getOperateMethod() {
        return operateMethod;
    }

    public void setOperateMethod(String operateMethod) {
        this.operateMethod = operateMethod == null ? null : operateMethod.trim();
    }

    public String getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass == null ? null : returnClass.trim();
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser == null ? null : operateUser.trim();
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime == null ? null : operateTime.trim();
    }

    public String getParamAndValue() {
        return paramAndValue;
    }

    public void setParamAndValue(String paramAndValue) {
        this.paramAndValue = paramAndValue == null ? null : paramAndValue.trim();
    }

    public Integer getCostTime() {
        return costTime;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue == null ? null : returnValue.trim();
    }
}