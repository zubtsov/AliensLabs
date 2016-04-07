import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void test() throws CloneNotSupportedException {
        Matrix a = new Matrix(4, 4);
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                a.set(i, j, (i - 1) * 4 + j - 1 + 1);
            }
        }
        //PERMUTATIONS TEST
        List<Integer> l = new ArrayList<>(4);
        l.add(3);
        l.add(2);
        l.add(1);
        l.add(0);
        Matrix b = a.clone();
        b.permuteColumns(l);
        b.permuteColumns(l);
        System.out.println("Permutation of columns: " + a.equals(b));

        //LU TEST
        Matrix sample = new Matrix(3, 3);
        sample.set(1, 1, 1.0).set(1, 3, 2.0).set(2, 1, -2.0).set(2, 2, 3.0).set(2, 3, 4.0).set(3, 1, 1.0).set(3, 3, 8.0);

        Matrix sampleLU = sample.LUdecomposition();
        Matrix L = sampleLU.lowerTriangular().add(Matrix.getOnes(3));
        Matrix U = sampleLU.upperTriangular().add(sampleLU.diagonal());
        System.out.println("LU: " + L.mult(U).equals(sample));

        //SMART ROW LU TEST
        List<Integer> l2 = new ArrayList<>();
        Matrix sampleSmartLU = sample.smartRowLUdecomposition(l2);
        L = sampleSmartLU.lowerTriangular().add(Matrix.getOnes(3));
        U = sampleSmartLU.upperTriangular().add(sampleSmartLU.diagonal());
        U.permuteColumns(l2);
        System.out.println("Smart LU: " + L.mult(U).equals(sample));
        System.out.println("Determinant: " + new Matrix(2,2).set(1,1,1).set(1,2,2).set(2,1,3).set(2,2,4).determinant());

        //INVERSE TEST
        Matrix sample2 = new Matrix(2, 2);
        sample2.set(1, 1, 7).set(1, 2, 4).set(2, 1, 5).set(2, 2, 3);
        System.out.println("INVERSE ERROR: " + sample2.mult(sample2.inverseMatrix()).add(Matrix.getOnes(2).mult(-1)).maxColumnNorm()); //3 -4 -5 7

        //JACOBI
        Matrix right = new Matrix(3, 1).set(1, 1, 11).set(2, 1, 10).set(3, 1, 10);
        Matrix left = new Matrix(3, 3).set(1, 1, 10).set(1, 2, 1).set(1, 3, -1).set(2, 1, 1).set(2, 2, 10).set(2, 3, -1).set(3, 1, -1).set(3, 2, 1).set(3, 3, 10);
//        left = Matrix.getRandomMatrix(3,3);
        Matrix x0 = new Matrix(3, 1).set(1, 1, 1.1).set(2, 1, 1).set(3, 1, 1);
        Matrix jacobiX = left.jacobiSolver(right, x0, 0.00001);
        System.out.println("JACOBI SOLVER ERROR: " + left.clone().mult(jacobiX).sub(right).octaNorm());

        //UPPER TRIANGLE SOLVER
        left.set(1, 1, 1).set(1, 2, 1).set(1, 3, 1).set(2, 1, 0).set(2, 2, 1).set(2, 3, 1).set(3, 1, 0).set(3, 2, 0).set(3, 3, 1);
        right.set(1, 1, 4).set(2, 1, 3).set(3, 1, 2);
        Matrix utriangleX = left.upperTriangleSolver(right);
        System.out.println("UPPER TRIANGLE ERROR: " + left.clone().mult(utriangleX).sub(right).octaNorm());

        //LOWER TRIANGLE SOLVER
        left.set(1, 1, 1).set(1, 2, 0).set(1, 3, 0).set(2, 1, 1).set(2, 2, 1).set(2, 3, 0).set(3, 1, 1).set(3, 2, 1).set(3, 3, 1);
        right.set(1, 1, 3).set(2, 1, 4).set(3, 1, 5);
        Matrix ltriangleX = left.lowerTriangleSolver(right);
        System.out.println("LOWER TRIANGLE ERROR: " + left.clone().mult(ltriangleX).sub(right).octaNorm());

        //QR DECOMPOSITION
        Matrix sample3 = new Matrix(3,3).set(1, 1, 3).set(1, 2, -1).set(1, 3, 5).set(2, 1, 0).set(2, 2, 0).set(2, 3, 5).set(3, 1, 4).set(3, 2, 7).set(3, 3, 5);
        Matrix[] QR = sample3.QRDecomposition();
        System.out.println("Q*Qt=E ERROR: " + QR[0].clone().mult(QR[0].clone().transpose()).sub(Matrix.getOnes(3)).maxColumnNorm());
        System.out.println("QR=A ERROR: " + QR[0].clone().mult(QR[1]).sub(sample3).maxColumnNorm());

        //UPPER HESSENBERG
        Matrix sample4 =  new Matrix(3,3).set(1, 1, 1).set(1, 2, -1).set(1, 3, 0).set(2, 1, -1).set(2, 2, 0).set(2, 3, 1).set(3, 1, 0).set(3, 2, 1).set(3, 3, 1);
        Matrix uHessenberg = sample4.upperHessenberg();
        System.out.println("DONE");

        //QR EIGENVALUES
        Matrix sample5 = new Matrix(3,3).set(1, 1, 4).set(1, 2, 2).set(1, 3, 1).set(2, 1, 2).set(2, 2, 5).set(2, 3, 3).set(3, 1, 1).set(3, 2, 3).set(3, 3, 6);//new Matrix(2,2).set(1,1,-1).set(1,2,-6).set(2,1,2).set(2,2,6);
        Matrix sample5eigen = sample5.QREigenvalues(10);
        System.out.println("DONE");
    }
    public static void main(String[] args) throws CloneNotSupportedException {
        //test();
        Scanner s = new Scanner(System.in);
        Matrix A;
        int n;

        System.out.print("Введите размер матрицы: ");
        n = s.nextInt();
        A = new Matrix(n, n);

        System.out.print("Ввести матрицу? ");
        String str = s.next();

        switch (str.toLowerCase()) {
            case "y":
                System.out.println("Вводите матрицу построчно");
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= n; j++) {
                        A.set(i,j,s.nextDouble());
                    }
                }
                break;
            case "b":
                System.out.print("Введите параметр альфа: ");
                double alpha = s.nextDouble();
                A = Matrix.getBadMatrix(alpha, n);
                break;
            case "r":
                System.out.print("Введите минимальное и максимальное случайные числа: ");
                double low, high;
                low = s.nextDouble();
                high = s.nextDouble();
                A = Matrix.getRandomMatrix(n, n, low, high).diagonalDominant();
                break;
        }

        System.out.println("Матрица A: " + A);

        Matrix x = new Matrix(n, 1);
        for (int i = 1; i <= n; i++) {
            x.set(i, 1, i);
        }
        Matrix b = A.clone().mult(x);

        System.out.println("Вектор правой части системы: " + b);

        //LU SOLVE
        Matrix.operations = 0;
        Matrix LU = A.LUdecomposition();
        Matrix L = LU.lowerTriangular().add(Matrix.getOnes(n));
        Matrix U = LU.upperTriangular().add(LU.diagonal());
        Matrix x1 = U.upperTriangleSolver(L.lowerTriangleSolver(b));
        int operations1 = Matrix.operations;

        //SMART LU
        Matrix.operations = 0;
        List<Integer> l = new ArrayList<>(n);
        LU = A.smartRowLUdecomposition(l);
        L = LU.lowerTriangular().add(Matrix.getOnes(n));
        U = LU.upperTriangular().add(LU.diagonal());
        Matrix x2 = U.upperTriangleSolver(L.lowerTriangleSolver(b));
        x2.transpose().permuteColumns(l).transpose();
        int operations2 = Matrix.operations;

        //JACOBI SOLVER
        Matrix.operations = 0;
        System.out.print("Введите начальный вектор для метода Якоби: ");
        Matrix x0 = new Matrix(n, 1);
        for (int i = 0; i < n; i++) {
            x0.set(i + 1, 1, s.nextDouble());
        }
        System.out.print("Введите погрешность: ");
        double eps = s.nextDouble();
        Matrix x3 = A.jacobiSolver(b, x0, eps);
        int operations3 = Matrix.operations;

        double det = A.determinant();

        double error1 = A.clone().mult(x1).sub(b).maxColumnNorm()/b.maxColumnNorm();
        double error2 = A.clone().mult(x2).sub(b).maxColumnNorm()/b.maxColumnNorm();
        double error3 = A.clone().mult(x3).sub(b).maxColumnNorm()/b.maxColumnNorm();

        System.out.print("Введите точность для QR метода: ");
        Matrix eigenMatrix = A.clone().mult(A.clone().transpose()).QREigenvalues(s.nextDouble());
        double maxEigN = Double.MIN_VALUE, minEigN = Double.MAX_VALUE;
        Matrix eigs = eigenMatrix.diagonal();
        for (int i = 1; i <= n; i++) {
            if (eigs.get(i, i) > maxEigN) {
                maxEigN = eigs.get(i, i);
            }
            if (eigs.get(i, i) < minEigN) {
                minEigN = eigs.get(i, i);
            }
        }

//        System.out.print("Введите параметр для проверки матрицы на \"треугольность\": ");
//        double tmp = s.nextDouble();
//        if (!eigenMatrix.isUpperTriangle(tmp)) {
//            System.out.println("Не все собственные числа матрицы вещественные!");
//        }

        double conditionNumber = Math.sqrt(Math.abs(maxEigN))/Math.sqrt(Math.abs(minEigN)); //A.maxColumnNorm()*A.clone().inverseMatrix().maxColumnNorm();

        System.out.println("Матрица собственных чисел: " + eigenMatrix);

        System.out.println("Название метода                               Погрешность                              Операций");
        System.out.println("LU разложение                               " + error1 + "                              " + operations1);
        System.out.println("LU разложение с выбором ведущего            " + error2 + "                              " + operations2);
        System.out.println("Якоби                                       " + error3 + "                              " + operations3);

        System.out.println("Определитель матрицы системы: " + det);
        System.out.println("Максимальное собственное число: " + maxEigN);
        System.out.println("Минимальное собственное число: " + minEigN);
        System.out.println("Число обусловленности: " + conditionNumber);
    }
}
