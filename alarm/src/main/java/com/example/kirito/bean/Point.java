package com.example.kirito.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kirito on 2016/4/12.
 */
public class Point implements Parcelable
{
    private double x,y;
    private String address;

    public Point(){

    }
    public Point(double x,double y){
        this.x=x;
        this.y=y;
    }
    public Point(double x,double y,String address){
        this.x=x;
        this.y=y;
        this.address=address;
    }
    protected Point(Parcel in) {
        x = in.readDouble();
        y = in.readDouble();
        address=in.readString();
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in.readDouble(),in.readDouble(),in.readString());
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         dest.writeDouble(x);
         dest.writeDouble(y);
         dest.writeString(address);
    }
}
