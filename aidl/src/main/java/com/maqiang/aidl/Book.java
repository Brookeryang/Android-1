package com.maqiang.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maqiang on 16/8/13.
 */
public class Book implements Parcelable{
    private int id;
    private String name;

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * 反序列化
     */
    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel parcel) {
            return new Book(parcel);
        }

        @Override
        public Book[] newArray(int i) {
            return new Book[i];
        }
    };

    public Book(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }



    /**
     * 序列化过程
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
