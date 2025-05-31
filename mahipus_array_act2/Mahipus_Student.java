/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mahipus_array_act2;

/**
 *
 * @author Nikki Mahipus
 */
  
public class Mahipus_Student {
    private String name;
    private int age;
    private String major;

    public Mahipus_Student(String name, int allowance, String major) {
        this.name = name;
        this.age = allowance;
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

