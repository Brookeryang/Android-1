package com.maqiang.doctor.bean;

import java.io.Serializable;

/**
 * Created by maqiang on 2017/3/9.
 */

public class InfoDetailResult implements Serializable {
    private int count;
    private String description;
    private int fcount;
    private int id;
    private String img;
    private String keywords;
    private int loreclass;
    private String message;
    private int rcount;
    private boolean status;
    private long time;
    private String title;

    public InfoDetailResult() {
    }

    public InfoDetailResult(int count, String description, int fcount, int id, String img,
                            String keywords, int loreclass, String message, int rcount, boolean status, long time, String title) {
        this.count = count;
        this.description = description;
        this.fcount = fcount;
        this.id = id;
        this.img = img;
        this.keywords = keywords;
        this.loreclass = loreclass;
        this.message = message;
        this.rcount = rcount;
        this.status = status;
        this.time = time;
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getLoreclass() {
        return loreclass;
    }

    public void setLoreclass(int loreclass) {
        this.loreclass = loreclass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "InfoDetailResult{" +
                "count=" + count +
                ", description='" + description + '\'' +
                ", fcount=" + fcount +
                ", id=" + id +
                ", img='" + img + '\'' +
                ", keywords='" + keywords + '\'' +
                ", loreclass=" + loreclass +
                ", message='" + message + '\'' +
                ", rcount=" + rcount +
                ", status=" + status +
                ", time=" + time +
                ", title='" + title + '\'' +
                '}';
    }
}
