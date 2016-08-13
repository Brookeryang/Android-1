package com.maqiang.xml;

/**
 * Created by maqiang on 16/8/13.
 */
public class Person {
    private int id;
    private String name;
    private int age;

    public Person() {
    }

    public Person(int age, int id, String name) {
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
