package com.mycompany.mahipus_studentlinkedlist;

import java.util.LinkedList;
import java.util.Scanner;

public class Mahipus_StudentLinkedList {
    private LinkedList<Mahipus_Student> students;
    private Scanner scanner;

    public Mahipus_StudentLinkedList() {
        students = new LinkedList<>();
        scanner = new Scanner(System.in);
        initializeStudents();
    }

    private void initializeStudents() {
        students.add(new Mahipus_Student("Nico Mahipus", 20, "Computer Science"));
        students.add(new Mahipus_Student("John James Tato", 22, "Mathematics"));
        students.add(new Mahipus_Student("Bryan Salcedo", 21, "Engineering"));
        students.add(new Mahipus_Student("Josh Martin Lacar", 20, "Biology"));
        students.add(new Mahipus_Student("Dasve Castillo", 23, "Physics"));
        students.add(new Mahipus_Student("Iyashmar Abdullah", 21, "Chemistry"));
        students.add(new Mahipus_Student("Benedict Layos", 22, "Economics"));
        students.add(new Mahipus_Student("Harvey Belgado", 20, "Psychology"));
        students.add(new Mahipus_Student("James Raleigh Rendon", 23, "History"));
        students.add(new Mahipus_Student("Yuhan Manubag", 21, "English"));
    }

    public void printStudents() {
        for (Mahipus_Student student : students) {
            System.out.println(student.toString());
        }
    }

    public void addStudent() {
        System.out.print("Enter student's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student's age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over
        System.out.print("Enter student's major: ");
        String major = scanner.nextLine();
        students.add(new Mahipus_Student(name, age, major));
        System.out.println("Student added successfully!");
    }

    public void removeStudent() {
        System.out.print("Enter student's name to remove: ");
        String name = scanner.nextLine();
        for (Mahipus_Student student : students) {
            if (student.getName().equals(name)) {
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
        Mahipus_StudentLinkedList studentLinkedList = new Mahipus_StudentLinkedList();
        studentLinkedList.run();
    }
}
