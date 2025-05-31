package com.mycompany.mahipus_studentlinkedlist;

public class Mahipus_Student {
    private String name;
    private int age;
    private String major;

    public Mahipus_Student(String name, int age, String major) {
        this.name = name;
        this.age = age;
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getMajor() {
        return major;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Age: " + age + ", Major: " + major;
    }
}
