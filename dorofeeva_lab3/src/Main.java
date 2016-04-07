import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Matrix test = Matrix.getRandomMatrix(8,8,1,10);
        Matrix[] QR = test.QRDecomposition();
        //Matrix
//        Scanner s = new Scanner(System.in);
//        System.out.print("Введите размер матрицы: ");
//        int n = s.nextInt();
//        System.out.print("Введите константу c: ");
//        int c = s.nextInt();
//        //System.out.print("Введите начальный вектор для методя Зейделя: ");
//        Matrix x0 = new Matrix(n,1);
//        for (int i = 1; i <= n; i++) {
//            x0.set(i, 1, 1);
//        }
//        System.out.print("Задайте точность итерационного метода: ");
//        double eps = s.nextDouble();
//        Matrix A = Matrix.getEMatrix(n, c);
//        Matrix x = new Matrix(n,1);
//        for (int i = 1; i <= n; x.set(i,1, i++));
//        Matrix b = A.clone().mult(x);
//        //System.out.println("Матрица: " + A);
//        System.out.println("Портрет матрицы: ");
//        System.out.print(A.getPortrait());
//        System.out.println("Коэффициент заполнения: " + A.getFillingFactor());
//        Matrix gz = A.gaussZeidelSolver(b, x0, eps);
//        System.out.println("Итерационный метод: " + gz.sub(x).octaNorm() / x.octaNorm());
//        System.out.println("Итерационный метод: " + gz.sub(x).octaNorm() / x.octaNorm());
//        Matrix L = A.holetskyDecomposition();
//        System.out.println("Матрица L:\n" + L.getPortrait());
//        Matrix sm = L.clone().transpose().upperTriangleSolver(L.lowerTriangleSolver(b));
//        System.out.println("Прямой метод: " + sm.sub(x).octaNorm() / x.octaNorm());
//        System.out.println("Коэффициент заполнения прямого метода: " + (A.compare(L) + A.compare(L.clone().transpose()) - n));
//        System.out.println("Обусловленность кубическая: " + A.maxColumnNorm()*A.inverseMatrix().maxColumnNorm());
//        System.out.println("Обусловленность евклидова: " + A.euclideanNorm()*A.inverseMatrix().euclideanNorm());

        //LinkedListMatrix
        Scanner s = new Scanner(System.in);
        System.out.print("Введите размер матрицы: ");
        int n = s.nextInt();
        System.out.print("Введите константу c: ");
        int c = s.nextInt();
        //System.out.print("Введите начальный вектор для методя Зейделя: ");
        LinkedListMatrix x0 = new LinkedListMatrix(n,1);
        for (int i = 1; i <= n; i++) {
            x0.set(i, 1, 1);
        }
        System.out.print("Задайте точность итерационного метода: ");
        double eps = s.nextDouble();
        LinkedListMatrix A = LinkedListMatrix.getEMatrix(n, c);
        LinkedListMatrix x = new LinkedListMatrix(n,1);
        for (int i = 1; i <= n; x.set(i,1, i++));
        LinkedListMatrix b = A.clone().mult(x);
        //System.out.println("Матрица: " + A);
        System.out.println("Портрет матрицы: ");
        System.out.print(A.getPortrait());
        System.out.println("Коэффициент заполнения: " + A.getFillingFactor());
        LinkedListMatrix gz = A.gaussZeidelSolver(b, x0, eps);
        System.out.println("Итерационный метод: " + gz.sub(x).octaNorm() / x.octaNorm());
        LinkedListMatrix L = A.holetskyDecomposition();
        //System.out.println("Матрица L:\n" + L.getPortrait());
        LinkedListMatrix sm = L.clone().transpose().upperTriangleSolver(L.lowerTriangleSolver(b));
        System.out.println("Прямой метод: " + sm.sub(x).octaNorm() / x.octaNorm());
        System.out.println("Коэффициент заполнения прямого метода: " + (A.compare(L) + A.compare(L.clone().transpose()) - n));
        System.out.println("Обусловленность кубическая: " + A.maxColumnNorm()*A.inverseMatrix().maxColumnNorm());
        System.out.println("Обусловленность евклидова: " + A.euclideanNorm()*A.inverseMatrix().euclideanNorm());
    }
}
