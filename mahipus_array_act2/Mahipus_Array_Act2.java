/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.mahipus_array_act2;

import java.util.LinkedList;
import java.util.Scanner;

public class Mahipus_Array_Act2 {

    private LinkedList<Mahipus_Student> students; // Correct type to Mahipus_Student
    private Scanner scanner;

    public Mahipus_Array_Act2() {
        students = new LinkedList<>();
        scanner = new Scanner(System.in);
        initializeStudents();
    }

    private void initializeStudents() {
        students.add(new Mahipus_Student("Nico Mahipus", 200, "Computer Science"));
        students.add(new Mahipus_Student("Cris Manero", 345, "Information technology"));
        students.add(new Mahipus_Student("Kit Baldonado", 210, "Education"));
        students.add(new Mahipus_Student("Kyrous Masangya", 120, "Biology"));
        students.add(new Mahipus_Student("Ian Ceasar Benablo", 523, "Pharmacy"));
        students.add(new Mahipus_Student("Michel Bolo", 221, "Chemistry"));
        students.add(new Mahipus_Student("Hizon Caja", 252, "Accountancy"));
        students.add(new Mahipus_Student("Carlo Castro", 280, "Psychology"));
        students.add(new Mahipus_Student("Kevin Cardoca", 323, "Culinary"));
        
    }

    public void printStudents() {
        for (Mahipus_Student student : students) {
            System.out.println(student);
        }
    }

    public void addStudent() {
        System.out.print("Enter student's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student's allowance: ");
        int allowance = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over
        System.out.print("Enter student's major: ");
        String major = scanner.nextLine();
        students.add(new Mahipus_Student(name, allowance, major));
        System.out.println("Student added successfully!");
    }

    public void removeStudent() {
        System.out.print("Enter student's name to remove: ");
        String name = scanner.nextLine();
        for (Mahipus_Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                students.remove(student);
                System.out.println("Student removed successfully!");
                return;
            }
        }
        System.out.println("Student not found!");
    }

    public void run() {
        while (true) {
            System.out.println("1. Print students");
            System.out.println("2. Add student");
            System.out.println("3. Remove student");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
            switch (option) {
                case 1:
                    printStudents();
                    break;
                case 2:
                    addStudent();
                    break;
                case 3:
                    removeStudent();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        Mahipus_Array_Act2 studentLinkedList = new Mahipus_Array_Act2();
        studentLinkedList.run();
    }
}