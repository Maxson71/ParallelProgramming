package main.java;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class Data {
    public static int N;
    public static int[][] MA;
    public static int[][] MB;
    public static int[][] MD;
    public static int[][] MF;
    public static int[][] MK;
    public static int[][] MP;
    public static int[][] MM;
    public static int[] O;
    public static int[] R;
    public static int[] S;

    public static void initializeMatricesAndVectors() {
        MA = new int[N][N];
        MB = new int[N][N];
        MD = new int[N][N];
        MF = new int[N][N];
        MK = new int[N][N];
        MP = new int[N][N];
        MM = new int[N][N];
        O = new int[N];
        R = new int[N];
        S = new int[N];

        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                MA[i][j] = random.nextInt(10) + 1;
                MB[i][j] = random.nextInt(10) + 1;
                MD[i][j] = random.nextInt(10) + 1;
                MF[i][j] = random.nextInt(10) + 1;
                MK[i][j] = random.nextInt(10) + 1;
                MP[i][j] = random.nextInt(10) + 1;
                MM[i][j] = random.nextInt(10) + 1;
            }
            O[i] = random.nextInt(10) + 1;
            R[i] = random.nextInt(10) + 1;
            S[i] = random.nextInt(10) + 1;
        }
    }
}

class F1 extends Thread {
    @Override
    public void run() {
        System.out.println("Потік T1: Обчислення main.java.F1 = ME = MB * (MA * MD)");
        int[][] temp = multiplyMatrices(Data.MA, Data.MD);
        int[][] ME = multiplyMatrices(Data.MB, temp);
        try (FileWriter writer = new FileWriter("F1_result.txt")) {
            writer.write("Результат main.java.F1 (ME): " + Arrays.deepToString(ME));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Потік T1 завершив роботу.");
    }

    private int[][] multiplyMatrices(int[][] A, int[][] B) {
        int[][] result = new int[Data.N][Data.N];
        for (int i = 0; i < Data.N; i++) {
            for (int j = 0; j < Data.N; j++) {
                for (int k = 0; k < Data.N; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }
}

class F2 extends Thread {
    @Override
    public void run() {
        System.out.println("Потік T2: Обчислення main.java.F2 = ML = SORT(TRANS(MF) * MK)");
        int[][] transMF = transposeMatrix(Data.MF);
        int[][] ML = multiplyMatrices(transMF, Data.MK);
        sortRows(ML);
        try (FileWriter writer = new FileWriter("F2_result.txt")) {
            writer.write("Результат main.java.F2 (ML): " + Arrays.deepToString(ML));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Потік T2 завершив роботу.");
    }

    private int[][] transposeMatrix(int[][] matrix) {
        int[][] transposed = new int[Data.N][Data.N];
        for (int i = 0; i < Data.N; i++) {
            for (int j = 0; j < Data.N; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    private int[][] multiplyMatrices(int[][] A, int[][] B) {
        int[][] result = new int[Data.N][Data.N];
        for (int i = 0; i < Data.N; i++) {
            for (int j = 0; j < Data.N; j++) {
                for (int k = 0; k < Data.N; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    private void sortRows(int[][] matrix) {
        for (int[] row : matrix) {
            Arrays.sort(row);
        }
    }
}

class F3 extends Thread {
    @Override
    public void run() {
        System.out.println("Потік T3: Обчислення main.java.F3 = s = MIN(O * TRANS(MP * MM)) + (R * SORT(S))");
        int[][] temp = multiplyMatrices(Data.MP, Data.MM);
        int[] transposed = multiplyMatrixAndVector(temp, Data.O);
        int min = findMin(transposed);
        Arrays.sort(Data.S);
        int scalarProduct = scalarMultiply(Data.R, Data.S);
        int result = min + scalarProduct;
        try (FileWriter writer = new FileWriter("F3_result.txt")) {
            writer.write("Результат main.java.F3 (s): " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Потік T3 завершив роботу з результатом: " + result);
    }

    private int[][] multiplyMatrices(int[][] A, int[][] B) {
        int[][] result = new int[Data.N][Data.N];
        for (int i = 0; i < Data.N; i++) {
            for (int j = 0; j < Data.N; j++) {
                for (int k = 0; k < Data.N; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    private int[] multiplyMatrixAndVector(int[][] matrix, int[] vector) {
        int[] result = new int[Data.N];
        for (int i = 0; i < Data.N; i++) {
            for (int j = 0; j < Data.N; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    private int findMin(int[] array) {
        return Arrays.stream(array).min().orElse(Integer.MAX_VALUE);
    }

    private int scalarMultiply(int[] A, int[] B) {
        int result = 0;
        for (int i = 0; i < Data.N; i++) {
            result += A[i] * B[i];
        }
        return result;
    }
}

public class Lab1 {
    public static void main(String[] args) throws InterruptedException {

        System.out.print("""
                _____________________________________________________
                Лабораторна робота №1.3
                Група ІО-22
                Шерстюк Максим Олександрович
                14.09.2024
                _____________________________________________________
                1.08 - main.java.F1  ME = MB * (MA * MD)
                2.16 - main.java.F2  ML = SORT(TRANS(MF) * MK)
                3.17 - main.java.F3  s = MIN(O * TRANS(MP * MM)) + (R * SORT(S))
                _____________________________________________________
                """);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть розмірність матриць (N): ");
        Data.N = scanner.nextInt();
        System.out.println("_____________________________________________________");
        Data.initializeMatricesAndVectors();

        long startTime = System.currentTimeMillis();

        F1 thread1 = new F1();
        F2 thread2 = new F2();
        F3 thread3 = new F3();

        thread1.setPriority(Thread.MAX_PRIORITY);  // Високий пріоритет для T1
        thread2.setPriority(Thread.NORM_PRIORITY); // Нормальний пріоритет для T2
        thread3.setPriority(Thread.MIN_PRIORITY);  // Низький пріоритет для T3

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
        System.out.println("_____________________________________________________");
        System.out.println("Усі потоки завершили роботу.");
        System.out.println("_____________________________________________________");

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime; // Час виконання програми

        System.out.println("Час виконання програми: " + elapsedTime + " мс");
        System.out.println("_____________________________________________________");
        System.out.println("Натисніть Enter для виходу...");
        scanner = new Scanner(System.in);
        scanner.nextLine();
        scanner.close();
    }
}
