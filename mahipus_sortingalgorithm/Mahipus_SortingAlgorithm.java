/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mahipus_sortingalgorithm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author Nikki Mahipus
 */
public class Mahipus_SortingAlgorithm {

            public static void bubbleSort(String[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j].compareToIgnoreCase(arr[j + 1]) > 0) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public static void selectionSort(String[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j].compareToIgnoreCase(arr[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            String temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }

    public static void insertionSort(String[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            String key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j].compareToIgnoreCase(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void printArray(String[] arr) {
        for (String str : arr) {
            System.out.println(str);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String[] names = {
            "aaron", "abigail", "alexander", "alice", "benjamin", "brianna", "caleb",
            "charlotte", "daniel", "diana", "elijah", "emma", "gabriel", "grace",
            "hannah", "issac", "isabella", "jacob", "jasmine", "joshua", "katherine",
            "kevin", "leah", "liam", "lucas", "madison", "matthew", "mia", "nathan",
            "noah", "oliva", "owen", "patrick", "penelope", "quentin", "rachel",
            "samuel", "sarah", "sofia", "steven", "thomas", "tristan", "uriel",
            "vannessa", "victor", "william", "xander", "yasmine", "zachary", "zoe"
        };

        // Convert array to List for shuffling
        List<String> nameList = new ArrayList<>();
        Collections.addAll(nameList, names);
        
        // Shuffle the list
        Collections.shuffle(nameList);
        
        // Convert back to array if needed
        String[] shuffledNames = nameList.toArray(new String[0]);

        System.out.println("Shuffled names array:");
        printArray(shuffledNames);

        String[] bubbleSorted = shuffledNames.clone();
        bubbleSort(bubbleSorted);
        System.out.println("\nSorted with Bubble Sort:");
        printArray(bubbleSorted);

        String[] selectionSorted = shuffledNames.clone();
        selectionSort(selectionSorted);
        System.out.println("\nSorted with Selection Sort:");
        printArray(selectionSorted);

        String[] insertionSorted = shuffledNames.clone();
        insertionSort(insertionSorted);
        System.out.println("\nSorted with Insertion Sort:");
        printArray(insertionSorted);
    }
}
