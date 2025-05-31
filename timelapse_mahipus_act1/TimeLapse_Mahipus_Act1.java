/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.timelapse_mahipus_act1;
import java.util.Scanner;
import java.util.Random;
import java.util.Stack;

public class TimeLapse_Mahipus_Act1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int playerHealth = 100;
        int cpuHealth = 100;

        Stack<Integer> playerHpHistory = new Stack<>();

        System.out.println("Welcome to the Turn-Based Battle!");
        System.out.println("You are fighting against a CPU opponent. Survive and win!");

        int turnCount = 0;

        while (playerHealth > 0 && cpuHealth > 0) {
            playerHpHistory.push(playerHealth);
            if (playerHpHistory.size() > 5) {
                
                Stack<Integer> temp = new Stack<>();
                while (playerHpHistory.size() > 1) {
                    temp.push(playerHpHistory.pop());
                }
                playerHpHistory.pop();
                while (!temp.isEmpty()) {
                    playerHpHistory.push(temp.pop());
                }
            }

            System.out.println("\nYour Health: " + playerHealth + " | CPU Health: " + cpuHealth);
            System.out.println("Choose your action: \n1. Attack \n2. Defend \n3. Time Lapse");
            System.out.print("Enter the number of your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    int playerDamage = random.nextInt(15) + 10;
                    cpuHealth -= playerDamage;
                    System.out.println("You attack the CPU and deal " + playerDamage + " damage!");
                    break;
                case 2: 
                    System.out.println("You defend, reducing incoming damage!");
                    break;
                case 3: 
                    playerHealth = timeLapse(playerHpHistory, playerHealth);
                    break;
                default:
                    System.out.println("Invalid choice! You missed your turn.");
            }
            if (cpuHealth > 0) {
                int cpuDamage = random.nextInt(15) + 10;
                if (choice == 2) {
                    cpuDamage /= 2; // Reduce damage if player defended
                }
                playerHealth -= cpuDamage;
                    System.out.println("CPU attacks and deals " + cpuDamage + " damage!");
            }

            turnCount++;
        }
        if (playerHealth <= 0) {
                System.out.println("\nGame Over! The CPU wins.");
        } else {
                System.out.println("\nCongratulations! You defeated the CPU.");
        }

        scanner.close();
    }
    
    public static int timeLapse(Stack<Integer> hpHistory, int currentHp) {
        if (hpHistory.size() < 5) {
                System.out.println("Not enough history! Time Lapse failed.");
                    return currentHp;
        } else {
            Stack<Integer> temp = new Stack<>();
                for (int i = 0; i < 4; i++) {
                temp.push(hpHistory.pop());
            }
            int revertedHp = hpHistory.peek(); 
            System.out.println("Weaver's Spell activated! Your HP reverts to " + revertedHp + " (from 5 turns ago).");
                while (!temp.isEmpty()) {
                hpHistory.push(temp.pop());
            }
            return revertedHp;
        }
    }
}