package com.maqiang.doctor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maqiang on 2017/3/9.
 */

public class InfoResult implements Serializable {
    private int total;
    private List<InfoItem> data;

    public InfoResult() {
    }

    public InfoResult(int total, List<InfoItem> data) {
        this.total = total;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<InfoItem> getData() {
        return data;
    }

    public void setData(List<InfoItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "InfoResult{" +
                "total=" + total +
                ", data=" + data +
                '}';
    }
}
