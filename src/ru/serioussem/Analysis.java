package ru.serioussem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Analysis {
    private static ArrayList<Integer> fillArrayFromFile(String pathFileInput) throws IOException {
        try (Scanner scanner = new Scanner(new FileInputStream(pathFileInput))) {
            ArrayList<Integer> list = new ArrayList<>();
            while (scanner.hasNextInt()) {
                list.add(scanner.nextInt());
            }
            return list;
        }
    }

    public static Integer findMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        for (Integer i : list) {
            if (min > i && i > 0) {
                min = i;
            }
        }
        return min;
    }

    public static int countOfLoops(List<Integer> list) {
        int loops = 0;
        for (Integer i : list) {
            if (i == 0) {
                loops++;
            }
        }
        return loops;
    }

    private static int countOfErr(ArrayList<Integer> list) {
        int err = 0;
        for (Integer i : list) {
            if (i == -1) {
                err++;
            }
        }
        return err;
    }

    private static double getAvarageWay(ArrayList<Integer> list) {
        int k = 0;
        int sum = 0;
        for (Integer i : list) {
            if (i > 0) {
                sum += i;
                k++;
            }
        }
        return (double) sum / k;
    }

    public static void main(String[] args) throws IOException {
        String pathFile = "src/result/testOut.txt";

        ArrayList<Integer> list = fillArrayFromFile(pathFile);
        System.out.println(list.size());
        System.out.println("Maximum : " + Collections.max(list));
        System.out.println("Minimum : " + findMin(list));
        int countOfLoops = countOfLoops(list);
        System.out.println("Count of loops  : " + countOfLoops);
        System.out.printf("Loops  %.0f%%\n", (double) countOfLoops / list.size() * 100);
        int countOfErrors = countOfErr(list);
        System.out.println("Count of errors  : " + countOfErrors);
        System.out.printf("Errors  %.0f%%\n", (double) countOfErrors / list.size() * 100);
        System.out.printf("Average way to philosophy :  %.0f", getAvarageWay(list));
    }
}