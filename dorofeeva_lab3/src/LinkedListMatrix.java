import java.util.*;

public class LinkedListMatrix {
    public static int operations;
    private LinkedListCells data = new LinkedListCells();
    private int rows; //нумерация строк с единицы
    private int cols; //нумерация столбцов с единицы

    public static LinkedListMatrix getOnes(int n) {
        LinkedListMatrix result = new LinkedListMatrix(n, n);
        for (int i = 0; i < n; i++) {
            result.data.set(i*n+i,1);
        }
        return result;
    }

    public static LinkedListMatrix getEMatrix(int n, int c) {
        LinkedListMatrix m = new LinkedListMatrix(n, n);
        for (int i = 0; i < n; i++) {
            m.data.set(i*n+i,4);
        }
        for (int i = 1; i < n; i++) {
            m.data.set(i*n+i-1,-1);
            m.data.set((i-1)*n+i,-1);
        }
        for (int i = c; i < n; i++) {
            m.data.set(i*n+i-c,-1);
            m.data.set((i-c)*n+i,-1);
        }
        return m;
    }

    public static LinkedListMatrix getRandomMatrix(int m, int n, double low, double high) {
        Random r = new Random();
        LinkedListMatrix result = new LinkedListMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result.data.set(i*n+j, r.nextDouble()*(high-low)+low);
            }
        }
        return result;
    }

    public static LinkedListMatrix getBadMatrix(double alpha, int n) {
        LinkedListMatrix result = new LinkedListMatrix(n, n);
        for (int i = 0; i < n; i++) {
            result.data.set(i * n + i, Math.pow(alpha, Math.abs((n - 2 * (i + 1)) / 2.0)));
        }
        for (int i = 1; i < n; i++) {
            result.data.set(0*n+i, result.data.set(i*n+0, result.data.get(0)/Math.pow(alpha, i+1)));
        }
        for (int i = 1; i < n-1; i++) {
            result.data.set((n - 1) * n + i, result.data.set(i * n + n - 1, result.data.get(n * n - 1) / Math.pow(alpha, i + 1)));
        }
        return result;
    }

    public static LinkedListMatrix getGivensMatrix(int i, double vi, int j, double vj, int n) {
        LinkedListMatrix result = getOnes(n);
        double tau = Math.sqrt(vi * vi + vj * vj);
        double c = vj / tau;
        double s = vi / tau;
        result.data.set(i*n+i,result.data.set(j*n+j,c));
        result.data.set(i*n+j,-s);
        result.data.set(j*n+i,s);
        return result;
    }

    public static LinkedListMatrix getBasisVector(int n, int i) {
        LinkedListMatrix result = new LinkedListMatrix(n,1);
        result.data.set(i-1,1.0);
        return result;
    }

    public LinkedListMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public LinkedListMatrix getSubmatrix(int rows, int cols, int initRow, int initCol)   {
        LinkedListMatrix result = new LinkedListMatrix(rows, cols);
        if (rows + initRow -1 > this.rows || cols + initCol - 1 > this.cols)
            throw new IndexOutOfBoundsException("Invalid row or column indexes");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data.set(i*cols+j, this.data.get((i+initRow-1)*this.cols+j+initCol-1));
            }
        }
        result.rows = rows;
        result.cols = cols;
        return result;
    }

    public double get(int row, int col) {
        return data.get((row - 1) * cols + col - 1);
    }

    public LinkedListMatrix set(int row, int col, double d) {
        data.set((row-1)*cols + col - 1,d);
        return this;
    }

    public LinkedListMatrix setSubmatrix(LinkedListMatrix other, int row, int col) {
        for (int i = 0; i < other.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                data.set((i+row-1)*rows+j+col-1,other.data.get(i*other.rows+j));
            }
        }
        return this;
    }

    public LinkedListMatrix swapColumns(int i, int j) {
        if (i == j) {
            return this;
        }
        double tmp;
        for (int k = 0; k < rows; k++) {
            tmp = data.get(k * cols + i);
            data.set(k * cols + i, data.get(k * cols + j));
            data.set(k * cols + j, tmp);
        }
        return this;
    }

    public LinkedListMatrix permuteColumns(List<Integer> permutations) {
        List<Integer> p = new ArrayList<>(permutations);
        LinkedListCells newData = new LinkedListCells();
        int col;
        for (int i = 0; i < cols; i++) {
            col = Utils.indexOfMinElem(p);
            for (int j = 0; j < rows; j++) {
                newData.set(j*cols + i,data.get(j*cols+col));
            }
            p.set(col, Integer.MAX_VALUE);
        }
        data = newData;
        return this;
    }

    public LinkedListMatrix diagonalDominant() {
        int min = Math.min(rows, cols);
        for (int i = 0, sum = 0; i < min; i++, sum = 0) {
            for (int j = 0; j < min; j++) {
                if (j == i) {
                    continue;
                }
                sum += Math.abs(data.get(i*cols+j));
            }
            data.set(i * cols + i,  Math.abs(data.get(i * cols + i)) + sum); //!!!
        }
        return this;
    }

    public LinkedListMatrix mult(LinkedListMatrix other) throws CloneNotSupportedException {
        if (cols != other.rows) {
            throw new IllegalArgumentException("Number of columns in first LinkedListMatrix must be equals to number of rows in second LinkedListMatrix");
        }
        LinkedListCells oldData = data.clone();
        LinkedListCells newData = new LinkedListCells();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                newData.set(i * other.cols + j,0);
                for (int k = 0; k < cols; k++) {
                    newData.set(i * other.cols + j, newData.get(i * other.cols + j) + oldData.get(i * cols + k) * other.data.get(k * other.cols + j));
                    operations+=2;
                }
            }
        }
        cols = other.cols;
        data = newData;
        return this;
    }

    public LinkedListMatrix mult(double d) {
        for (int i = 0; i < data.getSize(); i++) {
            data.setValue(data.getValue()*d);
            data.next();
            operations++;
        }
        return this;
    }

    public LinkedListMatrix add(LinkedListMatrix other) {
        if (cols != other.cols || rows != other.rows) {
            throw new IllegalArgumentException("Two matrices must have an equal number of rows and columns to be added");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data.set(i*cols + j,data.get(i*cols + j)+other.data.get(i*cols + j));
                operations++;
            }
        }
        return this;
    }

    public LinkedListMatrix sub(LinkedListMatrix other) {
        if (cols != other.cols || rows != other.rows) {
            throw new IllegalArgumentException("Two matrices must have an equal number of rows and columns to be subtracted");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data.set(i*cols + j,data.get(i*cols + j)-other.data.get(i*cols + j));
                operations++;
            }
        }
        return this;
    }

    public LinkedListMatrix diagonal() {
        LinkedListMatrix result = new LinkedListMatrix(rows, cols);
        int min = Math.min(rows, cols);
        for (int i = 0; i < min; i++) {
            result.data.set(i*cols+i,data.get(i*cols+i));
        }
        return result;
    }

    public LinkedListMatrix upperTriangular() {
        checkSquared();
        LinkedListMatrix result = new LinkedListMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = i + 1; j < cols; j++) {
                result.data.set(i*cols + j,data.get(i*cols + j));
            }
        }
        return result;
    }

    public LinkedListMatrix lowerTriangular() {
        checkSquared();
        LinkedListMatrix result = new LinkedListMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < i; j++) {
                result.data.set(i*cols + j,data.get(i * cols + j));
            }
        }
        return result;
    }

    public LinkedListMatrix upperHessenberg() throws CloneNotSupportedException {
        checkSquared();
        int n = rows;
        LinkedListMatrix result = clone();
        LinkedListMatrix v;
        LinkedListMatrix p;
        LinkedListMatrix Hi;
        double s;
        for (int i = 1; i < n - 1; i++) {
            v = result.getSubmatrix(n - i, 1, i + 1, i);
            s = Math.signum(v.get(1,1));
            if (s == 0) {
                s=1;
            }
            p = v.clone().add(getBasisVector(n - i, 1).mult(v.euclideanNorm() * s));
            Hi=getOnes(n - i).sub(p.clone().mult(p.clone().transpose()).mult(2 / p.clone().transpose().mult(p).get(1, 1)));
            Hi = getOnes(n).setSubmatrix(Hi, i + 1, i+1);
            result=Hi.clone().transpose().mult(result).mult(Hi);
        }
        return result;
    }

    public LinkedListMatrix transpose() {
        LinkedListCells newData = new LinkedListCells();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData.set(j*rows+i,data.get(i*cols+j));
            }
        }
        data = newData;
        int tmp = rows;
        rows = cols;
        cols = tmp;
        return this;
    }

    public LinkedListMatrix LUdecomposition() {
        checkSquared();
        int n=rows;
        LinkedListMatrix result = new LinkedListMatrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) { //вправо
                //U calculation
                double sum = data.get(i * n + j);
                for (int k = 0; k < i; k++) {
                    sum -= result.data.get(i * n + k) * result.data.get(k * n + j);
                    operations+=2;
                }
                result.data.set(i * n + j, sum);
            }
            for (int j = i+1; j < n; j++) { //вниз
                //L calculation
                double sum = data.get(j*n + i);
                for (int k = 0; k < i; k++) {
                    sum-=result.data.get(j*n+k)*result.data.get(k*n + i);
                    operations+=2;
                }
                result.data.set(j * n + i, sum / result.data.get(i * n + i));
                operations++;
            }
        }
        return result;
    }

    public LinkedListMatrix smartRowLUdecomposition(List<Integer> l) throws CloneNotSupportedException {
        checkSquared();
        List<Integer> permutations = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            permutations.add(i);
        }
        int n=rows;
        LinkedListMatrix result = new LinkedListMatrix(n,n);
        LinkedListMatrix tmp = clone();
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) { //вправо
                //U calculation
                double sum = tmp.data.get(i * n + j);
                for (int k = 0; k < i; k++) {
                    sum -= result.data.get(i * n + k) * result.data.get(k * n + j);
                    operations+=2;
                }
                result.data.set(i * n + j, sum);
            }

            //L calculation
            double elem=result.data.get(i*n+i);
            int ind=i;
            for (int j = i+1; j < n; j++) {
                if (result.data.get(i * n + j) > elem) {
                    elem=result.data.get(i * n + j);
                    ind = j;
                }
            }
            tmp.swapColumns(i, ind);
            result.swapColumns(i, ind);
            operations+=3*rows;
            Collections.swap(permutations, i, ind);

            for (int j = i+1; j < n; j++) { //вниз
                double sum = tmp.data.get(j*n + i);
                for (int k = 0; k < i; k++) {
                    sum-=result.data.get(j*n+k)*result.data.get(k*n + i);
                    operations+=2;
                }
                result.data.set(j*n + i,sum/elem);
                operations++;
            }
        }
        l.addAll(permutations);
        return result;
    }

    public LinkedListMatrix holetskyDecomposition() {
        int n = rows;
        LinkedListMatrix L = new LinkedListMatrix(n,n);
        for (int i = 0; i<n; i++) {
            //j<i
            for (int j = 0; j < i; j++) {
                L.data.set(i*n+j,data.get(i*n+j));
                for (int k = 0; k < j; k++) {
                    L.data.set(i*n+j,L.data.get(i*n+j)-L.data.get(i*n+k)*L.data.get(j*n+k));
                }
                L.data.set(i*n+j,L.data.get(i*n+j)/L.data.get(j*n+j));
            }
            //i==j
            double sum = data.get(i*n+i);
            for (int k = 0; k < i; k++) {
                sum-=L.data.get(i*n+k)*L.data.get(i*n+k);
            }
            L.data.set(i * n + i, Math.sqrt(sum));
        }
        return L;
    }

    public LinkedListMatrix jacobiSolver(LinkedListMatrix b, LinkedListMatrix x0, double eps) throws CloneNotSupportedException {
        checkSquared();
        LinkedListMatrix L = lowerTriangular();
        LinkedListMatrix D = diagonal();
        LinkedListMatrix R = upperTriangular();

        LinkedListMatrix invD = D.inverseMatrix();
        LinkedListMatrix invD2 = invD.clone();

        LinkedListMatrix B = invD.mult(L.clone().add(R).mult(-1));
        LinkedListMatrix C = invD2.mult(b);

        LinkedListMatrix x1 = x0;
        LinkedListMatrix tmp;
        double q = B.maxColumnNorm();
        if (q >= 1) {
//            throw new UnsupportedOperationException("LinkedListMatrix norm must be less than 1");
            System.out.println("Норма матрицы B: " + q);
        }
        do {
            x0 = x1.clone();
            x1 =  B.clone().mult(x0).add(C);
            tmp=x1.clone();
            operations+=3;
        } while(Math.abs(tmp.sub(x0).octaNorm()*q/(1-q)) > eps);

        return x1;
    }

    public LinkedListMatrix gaussZeidelSolver(LinkedListMatrix right, LinkedListMatrix x0, double eps) throws CloneNotSupportedException {
        LinkedListMatrix x1 = x0;
        do {
            x0 = x1.clone();
            for (int i = 0; i < rows; i++) {
                double sum=0;
                for (int j = 0; j < i; j++) {
                    sum-=data.get(i*cols+j)/data.get(i*cols+i)*x1.data.get(j);
                }
                for (int j = i+1; j < rows; j++) {
                    sum-=data.get(i*cols+j)/data.get(i*cols+i)*x0.data.get(j);
                }
                x1.data.set(i, sum + right.data.get(i)/data.get(i*cols+i));
            }
        } while(x1.clone().sub(x0).octaNorm() > eps);
        return x1;
    }

    public LinkedListMatrix upperTriangleSolver(LinkedListMatrix right) {
        checkSquared();
        int n = rows;
        LinkedListMatrix result = new LinkedListMatrix(n, 1);
        result.data.set(n - 1, right.data.get(n - 1) / data.get(n * n - 1));
        operations++;
        for (int i = rows - 2; i >= 0; i--) {
            result.data.set(i, right.data.get(i));
            for (int k = i + 1; k < n; k++) {
                result.data.set(i, result.data.get(i) - data.get(i*n+k)*result.data.get(k));
                operations+=2;
            }
            result.data.set(i,result.data.get(i)/data.get(i*n+i));
            operations++;
        }
        return result;
    }

    public LinkedListMatrix lowerTriangleSolver(LinkedListMatrix right) {
        checkSquared();
        int n = rows;
        LinkedListMatrix result = new LinkedListMatrix(n, 1);
        result.data.set(0, right.data.get(0) / data.get(0));
        operations++;
        for (int i = 1; i < rows; i++) {
            result.data.set(i, right.data.get(i));
            for (int k = i - 1; k >= 0; k--) {
                result.data.set(i, result.data.get(i)-data.get(i*n+k)*result.data.get(k));
                operations+=2;
            }
            result.data.set(i,result.data.get(i)/data.get(i*n+i));
            operations++;
        }
        return result;
    }

    public double determinant() throws CloneNotSupportedException {
        List<Integer> l = new ArrayList<Integer>();
        LinkedListMatrix LU = smartRowLUdecomposition(l); //smartRowLUdecomposition(new ArrayList<>()); - с точностью до знака
        double d = 1.0;
        for (int i = 0; i < rows; i++) {
            d*=LU.data.get(i*cols+i);
        }
        return d;
    }

    public LinkedListMatrix[] QRDecomposition() throws CloneNotSupportedException {
        checkSquared();
        int n = rows;
        LinkedListMatrix Q = getOnes(n);
        LinkedListMatrix R = clone();
        LinkedListMatrix v = getSubmatrix(n, 1, 1, 1);
        LinkedListMatrix p = v.clone().add(getBasisVector(n, 1).mult(Math.signum(v.get(1,1))*v.euclideanNorm()));
        LinkedListMatrix pt = p.clone().transpose();
        LinkedListMatrix Hi = getOnes(n).sub(p.clone().mult(pt).mult(2 / pt.clone().mult(p).get(1, 1)));
        R = Hi.clone().mult(R);
        Q = Hi.clone().mult(Q);
        for (int i = 1; i < n-1; i++) {
            Hi = getOnes(n-i);
            v = R.getSubmatrix(n - i, 1, i + 1, i + 1);
            double s = Math.signum(v.get(1, 1));
            if (s == 0) {
                s=1;
            }
            p = v.add(getBasisVector(n - i, 1).mult(s * v.euclideanNorm()));
            pt = p.clone().transpose();
            Hi=Hi.sub(p.clone().mult(pt).mult(2 / pt.clone().mult(p).get(1, 1)));
            Hi=getOnes(n).setSubmatrix(Hi, n - i, n-i);
            R = Hi.clone().mult(R);
            Q = Hi.clone().mult(Q);
        }
        return new LinkedListMatrix[] {Q.transpose(), R} ;
    }

    public LinkedListMatrix QREigenvalues(double eps) throws CloneNotSupportedException {
        checkSquared();
        LinkedListMatrix result = upperHessenberg();
        int n = rows;
        LinkedListMatrix G, Q=null, R=null, v;
        double s;
        do {
            Q=getOnes(n);
            R=result.clone();
            for (int i = 1; i < n; i++) {
                //элемент i-й строки, i-1 столбца обнулить
                if (result.data.get(i * cols + i-1) == 0.0) {
                    continue;
                }
                v = R.getSubmatrix(n, 1, 1, i);
                G = getGivensMatrix(i, v.data.get(i), i - 1, v.data.get(i - 1), n); //для R
                R=G.clone().mult(R);
                Q=G.mult(Q);
            }
            result = R.clone().mult(Q.transpose());
            s=0;
            for (int i = 1; i < n; i++) {
                s+=Math.abs(result.data.get(i*n+i-1));
            }
        } while(s > eps);
        return result;
    }

    public LinkedListMatrix inverseMatrix() {
        LinkedListMatrix LU = LUdecomposition();
        LinkedListMatrix inverse = new LinkedListMatrix(rows, cols);
        int n = rows;
        double sum;
        for (int i = n-1; i >= 0; i--) {
            //row=column
            sum = 1;
            for (int k = i + 1; k < n; k++) {
                sum-=LU.data.get(i*n+k)*inverse.data.get(k*n+i);
                operations+=2;
            }
            inverse.data.set(i*n+i,sum/LU.data.get(i*n+i));
            operations++;
            //row<column, j - строка, i - столбец
            for (int j = i-1; j >= 0; j--) {
                sum=0;
                for (int k = j+1; k<n; k++) {
                    sum -= LU.data.get(j * cols + k) * inverse.data.get(k * cols + i);
                    operations+=2;
                }
                inverse.data.set(j*cols+i,sum/LU.data.get(j*cols+j));
                operations++;
            }
            //row>column, j - столбец, i - строка
            for (int j = i-1; j >= 0; j--) {
                sum=0;
                for (int k = j+1; k<n; k++) {
                    sum -= LU.data.get(k * cols + j) * inverse.data.get(i * cols + k);
                    operations+=2;
                }
                inverse.data.set(i*cols+j,sum);
            }
        }
        return inverse;
    }

    public double octaNorm() {
        if (rows != 1 && cols != 1) {
            throw new UnsupportedOperationException("Octahedral norm defined for vectors only");
        }
        double norm = 0.0;
        int max = Math.max(rows, cols);
        for (int i = 0; i < max; i++) {
            norm += Math.abs(data.get(i));
            operations++;
        }
        return norm;
    }

    public double euclideanNorm() {
        double norm = 0;
        for (int i = 0; i < rows * cols; i++) {
            norm+=data.get(i)*data.get(i);
            operations+=2;
        }
        return Math.sqrt(norm);
    }

    public double maxColumnNorm() {
        double max = Double.MIN_VALUE;
        for (int j = 0; j < cols; j++) {
            double d = 0.0;
            for (int i = 0; i < rows; i++) {
                d+=Math.abs(data.get(i*cols + j));
                operations++;
            }
            if (d > max)
                max = d;
        }
        return max;
    }

    public int getFillingFactor() {
        return data.getSize();
    }

    public boolean isDiagDominant() {
        int min = Math.min(rows, cols);
        for (int i = 0, sum = 0; i < min; i++, sum = 0) {
            for (int j = 0; j < min; j++) {
                if (j == i) {
                    continue;
                }
                sum += Math.abs(data.get(i*cols+j));
            }
            if (Math.abs(data.get(i * cols + i)) < sum) {
                return false;
            }
        }
        return true;
    }

    public boolean isUpperTriangle(double eps) {
        checkSquared();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < i; j++) {
                if (Math.abs(data.get(i * cols + j)) >= eps)    {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkSquared() {
        if (rows != cols) {
            throw new UnsupportedOperationException("Rows number must be equal to columns number");
        }
    }

    @Override
    protected LinkedListMatrix clone() throws CloneNotSupportedException {
        LinkedListMatrix result = new LinkedListMatrix(rows, cols);
        result.data = data.clone();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        LinkedListMatrix other = (LinkedListMatrix) o;
        if (this == o) {
            return true;
        }
        if (rows != other.rows || cols != other.cols) {
            return false;
        }
        for (int i = 0; i < rows*cols; i++) { //можно оптимизировать с помощью next() и prev()
            if (data.get(i) != other.data.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(String.format(" %-5.2f", data.get(i*cols+j)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getPortrait() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(data.get(i*cols+j) != 0)
                    sb.append(String.format(" %-2s", "✚"));
                else
                    sb.append(String.format(" %-2s", " "));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int compare(LinkedListMatrix other) {
        int k = 0;
        for (int i = 0; i < rows*cols; i++) {
            if (other.data.get(i) != 0 && data.get(i) == 0) {
                k++;
            }
        }
        return k;
    }
}
