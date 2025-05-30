/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tla5_mahipus;

/**
 *
 * @author Nikki Mahipus
 */
import java.util.Scanner;

public class TLA5_Mahipus {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int totalEmployees = 5;
        double totalGrossPay = 0;
        int totalHoursWorked = 0;

        for (int i = 1; i <= totalEmployees; i++) {
            System.out.println("#Employee #" + i);
            System.out.print("Enter hours worked: ");
                int hoursWorked = scanner.nextInt();
                System.out.print("Enter hourly rate: ");
                double hourlyRate = scanner.nextDouble();

            double grossPay = calculateGrossPay(hoursWorked, hourlyRate);
                totalGrossPay += grossPay;
                totalHoursWorked += hoursWorked;

            System.out.printf("Gross pay is Php%.2f\n", grossPay);
        }

        System.out.println("Total hours worked: " + totalHoursWorked);
        System.out.printf("Total gross pay disbursed: Php%.2f\n", totalGrossPay);

        scanner.close();
    }

    private static double calculateGrossPay(int hoursWorked, double hourlyRate) {
        if (hoursWorked < 20) {
            return (hoursWorked * hourlyRate) - 50.00;
        } else if (hoursWorked > 40) {
            return (40 * hourlyRate) + ((hoursWorked - 40) * hourlyRate * 1.5);
        } else {
            return hoursWorked * hourlyRate;
        }
    }
}