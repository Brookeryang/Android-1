package com.maqiang.doctor.bean;

import java.io.Serializable;

/**
 * Created by maqiang on 2017/3/9.
 */

public class InfoDetail implements Serializable{
    private int error_code;
    private String reason;
    private InfoDetailResult result;

    public InfoDetail() {
    }

    public InfoDetail(int error_code, String reason, InfoDetailResult result) {
        this.error_code = error_code;
        this.reason = reason;
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InfoDetailResult getResult() {
        return result;
    }

    public void setResult(InfoDetailResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "InfoDetail{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
