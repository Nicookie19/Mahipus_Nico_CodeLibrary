/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mahipus_sortingalgorithm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author Nikki Mahipus
 */
public class Quck_MergeSort {
        public static void mergeSort(String[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    private static void merge(String[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        String[] L = new String[n1];
        String[] R = new String[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[mid + 1 + j];
        }

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (L[i].compareTo(R[j]) <= 0) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void quickSort(String[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(String[] arr, int low, int high) {
        String pivot = arr[high];
        int i = low - 1; 
        
        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                String temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        String temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
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
        
        // Convert back to array
        String[] shuffledNames = nameList.toArray(new String[0]);

        System.out.println("Shuffled names array:");
        printArray(shuffledNames);

        String[] mergeSortArr = shuffledNames.clone();
        String[] quickSortArr = shuffledNames.clone();

        // Apply merge sort
        mergeSort(mergeSortArr, 0, mergeSortArr.length - 1);
        System.out.println("Sorted names using Merge Sort:");
        printArray(mergeSortArr);

        // Apply quick sort
        quickSort(quickSortArr, 0, quickSortArr.length - 1);
        System.out.println("Sorted names using Quick Sort:");
        printArray(quickSortArr);
    }
}
