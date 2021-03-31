package processor;

import java.util.InputMismatchException;
import java.util.Scanner;

enum TranspositionType {
    MAIN_DIAGONAL, SIDE_DIAGONAL, VERTICAL_LINE, HORIZONTAL_LINE
}

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            switch (chooseMenuOption(scanner)) {
                case 1:
                    String prompt1 = "Enter size of first matrix: ";
                    String prompt2 = "Enter first matrix:";
                    double[][] a = getMatrix(scanner, prompt1, prompt2);
                    prompt1 = "Enter size of second matrix: ";
                    prompt2 = "Enter second matrix:";
                    double[][] b = getMatrix(scanner, prompt1, prompt2);
                    double[][] sum = add(a, b);
                    if (sum != null) {
                        System.out.println("The result is:");
                        print(sum);
                    } else {
                        System.out.println("The operation cannot be performed.");
                    }
                    System.out.println();
                    break;
                case 2:
                    prompt1 = "Enter size of matrix: ";
                    prompt2 = "Enter matrix:";
                    a = getMatrix(scanner, prompt1, prompt2);
                    System.out.print("Enter constant: ");
                    double cons = scanner.nextDouble();
                    double[][] prod = multiplyByNum(a, cons);
                    System.out.println("The result is:");
                    print(prod);
                    System.out.println();
                    break;
                case 3:
                    prompt1 = "Enter size of first matrix: ";
                    prompt2 = "Enter first matrix:";
                    a = getMatrix(scanner, prompt1, prompt2);
                    prompt1 = "Enter size of second matrix: ";
                    prompt2 = "Enter second matrix:";
                    b = getMatrix(scanner, prompt1, prompt2);
                    double[][] product = multiply(a, b);
                    if (product != null) {
                        System.out.println("The result is:");
                        print(product);
                    } else {
                        System.out.println("The operation cannot be performed.");
                    }
                    System.out.println();
                    break;
                case 4:
                    a = generalizedTranspose();
                    System.out.println("The result is:");
                    print(a);
                    System.out.println();
                    break;
                case 5:
                    double det;
                    prompt1 = "Enter size of matrix: ";
                    prompt2 = "Enter matrix:";
                    a = getMatrix(scanner, prompt1, prompt2);

                    try {
                        det = determinant(a);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: determinant does not exist");
                        continue;
                    }
                    System.out.println("The result is:");
                    System.out.println(det);
                    System.out.println();
                    break;
                case 6:
                    prompt1 = "Enter size of matrix: ";
                    prompt2 = "Enter matrix:";
                    a = getMatrix(scanner, prompt1, prompt2);
                    a = inverse(a);
                    if (a == null) {
                        System.out.println("This matrix doesn't have an inverse.");
                        continue;
                    }
                    System.out.println("The result is:");
                    print(a);
                    System.out.println();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }
    }

    private static double[][] cofactorMatrix(double[][] a) {
        int size = a.length;
        double[][] cofactors = new double[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                cofactors[r][c] = determinant(minor(a, r, c));
                if ((r + c) % 2 == 1) {
                    cofactors[r][c] *= -1;
                }
            }
        }
        return cofactors;
    }

    private static double[][] inverse(double[][] a) {
        double det;
        try {
            det = determinant(a);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (det == 0) {
            return null;
        }
        a = transpose(cofactorMatrix(a));
        a = multiplyByNum(a, 1 / det);
        return  a;
    }

    private static double determinant(double[][] a) {
        int rows = a.length;
        int cols = a[0].length;
        if (rows != cols) throw new IllegalArgumentException("The matrix is not square");
        return determinantAux(a);
    }

    private static double determinantAux(double[][] a) {
        int rows = a.length;
        if (rows == 1) return a[0][0];
        if (rows == 2) {
            return a[0][0] * a[1][1] - a[0][1] * a[1][0];
        }
        double det = 0;
        int factor = 1;
        for (int i = 0; i< rows; i++) {
            assert minor(a, 0, i) != null;
            det += a[0][i] * factor * determinantAux(minor(a, 0, i));
            factor *= -1;
        }
        return det;
    }

    private static double[][] minor(double[][] a, int row, int col) {
        int size = a.length;
        if (size == 1) return null;
        double[][] b = new double[size - 1][size - 1];

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                b[r][c] = a[r][c];
            }
            for (int c = col + 1; c < size; c++) {
                b[r][c - 1] = a[r][c];
            }
        }

        for (int r = row + 1; r < size; r++) {
            for (int c = 0; c < col; c++) {
                b[r - 1][c] = a[r][c];
            }
            for (int c = col + 1; c < size; c++) {
                b[r - 1][c - 1] = a[r][c];
            }
        }
        return b;
    }
    private static double[][] generalizedTranspose() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println("1. Main diagonal");
            System.out.println("2. Side diagonal");
            System.out.println("3. Vertical line");
            System.out.println("4. Horizontal line");
            System.out.print("Your choice: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.  Try again.");
                continue;
            }

            if (choice  < 0 || choice > 4) {
                System.out.println("Invalid input.  Try again.");
                continue;
            }
            break;
        }
        double[][] a = getMatrix(scanner, "Enter matrix size: ", "Enter matrix:");
        double[][] result;
        switch (choice) {
            case 1:
                return transpose(a);
            case 2:
                return sideDiagonalTranspose(a);
            case 3:
                return verticalTranspose(a);
            case 4:
                return horizontalTranspose(a);
            default:
                return null;
        }
    }

    private static double[][] horizontalTranspose(double[][] a) {
        int rows = a.length;
        int cols = a[0].length;
        int mid = rows / 2;

        double[][] b = new double[rows][cols];
        for (int r = 0; r < mid; r++) {
            for (int c = 0; c < cols; c++) {
                b[r][c] = a[rows - 1 - r][c];
                b[rows - 1 - r][c] = a[r][c];
            }
        }
        return b;
    }

    private static double[][] verticalTranspose(double[][] a) {
        int rows = a.length;
        int cols = a[0].length;
        int mid = cols / 2;

        double[][] b = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < mid; c++) {
                b[r][c] = a[r][cols - 1 - c];
                b[r][cols - 1 - c] = a[r][c];
            }
        }
        return b;
    }

    private static double[][] sideDiagonalTranspose(double[][] a) {
        int rows = a.length;
        int cols = a[0].length;

        double[][] b = new double[cols][rows];
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                b[c][r] = a[rows - r - 1][cols - c - 1];
            }
        }
        return b;
    }

    private static double[][] transpose(double[][] a) {
        int rows = a.length;
        int cols = a[0].length;

        double[][] b = new double[cols][rows];
        for (int r = 0; r < cols; r++) {
            for (int c = 0; c < cols; c++) {
                b[r][c] = a[c][r];
            }
        }
        return b;
    }

    private static double[][] multiply(double[][] a, double[][] b) {
        int rows1 = a.length;
        int col1 = a[0].length;
        int rows2 = b.length;
        int col2 = b[0].length;

        if (col1 != rows2) return null;
        double[][] prod = new double[rows1][col2];
        for (int r = 0; r < rows1; r++) {
            for (int c = 0; c < col2; c++) {
                prod[r][c] = 0;
                for (int i = 0; i < col1; i++) {
                    prod[r][c] += a[r][i] * b[i][c];
                }

            }
        }
        return prod;
    }

    private static int chooseMenuOption(Scanner scanner) {

        while (true) {
            System.out.println("1. Add matrices");
            System.out.println("2. Multiply matrix by a constant");
            System.out.println("3. Multiply matrices");
            System.out.println("4. Transpose matrix");
            System.out.println("5. Calculate a determinant");
            System.out.println("6. Inverse matrix");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.  Try again.");
                continue;
            }

            if (choice  < 0 || choice > 6) {
                System.out.println("Invalid input.  Try again.");
                continue;
            }

            return choice;
        }
    }

    private static double[][] multiplyByNum(double[][] a, double d) {
        int rows = a.length;
        int cols = a[0].length;
        double[][] m = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                m[r][c] = a[r][c] * d;
            }
        }
        return m;
    }

    private static double[][] getMatrix(Scanner scanner, String prompt1, String prompt2) {
        System.out.print(prompt1);
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();

        System.out.println(prompt2);
        double[][] m = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                m[r][c] = scanner.nextDouble();
            }
        }
        return m;
    }

    private static double[][] add(double[][] a, double[][] b) {
        int r1 = a.length;
        int c1 = a[0].length;
        int r2 = b.length;
        int c2 = b[0].length;

        if (r1 != r2 || c1 != c2) return null;

        double[][] sum = new double[r1][c1];
        for (int r = 0; r < r1; r++) {
            for (int c = 0; c < c1; c++) {
                sum[r][c] = a[r][c] + b[r][c];
            }
        }
        return sum;
    }

    private static void  print(double[][] m) {
        for(double[] r : m) {
            for (double x : r) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }
}
