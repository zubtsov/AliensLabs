import java.util.*;

public class Matrix implements Cloneable {
    public static int operations;

    private double[] data; //матрица хранится построчно
    private int rows; //нумерация строк с единицы
    private int cols; //нумерация столбцов с единицы

    public static Matrix getOnes(int n) {
        Matrix result = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            result.data[i*n+i]=1;
        }
        return result;
    }

    public static Matrix getEMatrix(int n, int c) {
        Matrix m = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            m.data[i*n+i]=4;
        }
        for (int i = 1; i < n; i++) {
            m.data[i*n+i-1]=-1;
            m.data[(i-1)*n+i]=-1;
        }
        for (int i = c; i < n; i++) {
            m.data[i*n+i-c]=-1;
            m.data[(i-c)*n+i]=-1;
        }
        return m;
    }

    public static Matrix getRandomMatrix(int m, int n, double low, double high) {
        Random r = new Random();
        Matrix result = new Matrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result.data[i*n+j] = r.nextDouble()*(high-low)+low;
            }
        }
        return result;
    }

    public static Matrix getBadMatrix(double alpha, int n) {
        Matrix result = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            result.data[i*n+i] = Math.pow(alpha, Math.abs((n-2*(i+1))/2.0));
        }
        for (int i = 1; i < n; i++) {
            result.data[0*n+i] = result.data[i*n+0] = result.data[0]/Math.pow(alpha, i+1);
        }
        for (int i = 1; i < n-1; i++) {
            result.data[(n - 1) * n + i] = result.data[i * n + n - 1] = result.data[n * n - 1] / Math.pow(alpha, i + 1);
        }
        return result;
    }

    public static Matrix getGivensMatrix(int i, double vi, int j, double vj, int n) {
        Matrix result = getOnes(n);
        double tau = Math.sqrt(vi * vi + vj * vj);
        double c = vj / tau;
        double s = vi / tau;
        result.data[i*n+i]=result.data[j*n+j]=c;
        result.data[i*n+j]=-s;
        result.data[j*n+i]=s;
        return result;
    }

    public static Matrix getBasisVector(int n, int i) {
        Matrix result = new Matrix(n,1);
        result.data[i-1]=1.0;
        return result;
    }

    public Matrix(int rows, int cols) {
        data = new double[rows*cols];
        Arrays.fill(data, 0.0);
        this.rows = rows;
        this.cols = cols;
    }

    public Matrix getSubmatrix(int rows, int cols, int initRow, int initCol)   {
        Matrix result = new Matrix(rows, cols);
        if (rows + initRow -1 > this.rows || cols + initCol - 1 > this.cols)
            throw new IndexOutOfBoundsException("Invalid row or column indexes");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i*cols+j] = this.data[(i+initRow-1)*this.cols+j+initCol-1];
            }
        }
        result.rows = rows;
        result.cols = cols;
        return result;
    }

    public double get(int row, int col) {
        return data[(row - 1) * cols + col - 1];
    }

    public Matrix set(int row, int col, double d) {
        data[(row-1)*cols + col - 1]=d;
        return this;
    }

    public Matrix setSubmatrix(Matrix other, int row, int col) {
        for (int i = 0; i < other.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                data[(i+row-1)*rows+j+col-1]=other.data[i*other.rows+j];
            }
        }
        return this;
    }

    public Matrix swapColumns(int i, int j) {
        if (i == j) {
            return this;
        }
        double tmp;
        for (int k = 0; k < rows; k++) {
            tmp = data[k * cols + i];
            data[k * cols + i] = data[k * cols + j];
            data[k * cols + j] = tmp;
        }
        return this;
    }

    public Matrix permuteColumns(List<Integer> permutations) {
        List<Integer> p = new ArrayList<>(permutations);
        double[] newData = new double[data.length];
        int col;
        for (int i = 0; i < cols; i++) {
            col = Utils.indexOfMinElem(p);
            for (int j = 0; j < rows; j++) {
                newData[j*cols + i]=data[j*cols+col];
            }
            p.set(col, Integer.MAX_VALUE);
        }
        data = newData;
        return this;
    }

    public Matrix diagonalDominant() {
        int min = Math.min(rows, cols);
        for (int i = 0, sum = 0; i < min; i++, sum = 0) {
            for (int j = 0; j < min; j++) {
                if (j == i) {
                    continue;
                }
                sum += Math.abs(data[i*cols+j]);
            }
            data[i * cols + i] =  Math.abs(data[i * cols + i]) + sum; //!!!
        }
        return this;
    }

    public Matrix mult2(Matrix other) {
        for (int i = 0; i < data.length; i++) {
            data[i]*=other.data[i];
        }
        return this;
    }

    public Matrix mult(Matrix other) {
        if (cols != other.rows) {
            throw new IllegalArgumentException("Number of columns in first matrix must be equals to number of rows in second matrix");
        }
        double[] oldData = Arrays.copyOf(data, data.length);
        double[] newData = new double[rows*other.cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                newData[i * other.cols + j]=0;
                for (int k = 0; k < cols; k++) {
                    newData[i * other.cols + j] += oldData[i * cols + k] * other.data[k * other.cols + j];
                    operations+=2;
                }
            }
        }
        cols = other.cols;
        data = newData;
        return this;
    }

    public Matrix mult(double d) {
        for (int i = 0; i < data.length; i++) {
            data[i]*=d;
            operations++;
        }
        return this;
    }

    public Matrix add(Matrix other) {
        if (cols != other.cols || rows != other.rows) {
            throw new IllegalArgumentException("Two matrices must have an equal number of rows and columns to be added");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i*cols + j]+=other.data[i*cols + j];
                operations++;
            }
        }
        return this;
    }

    public Matrix sub(Matrix other) {
        if (cols != other.cols || rows != other.rows) {
            throw new IllegalArgumentException("Two matrices must have an equal number of rows and columns to be subtracted");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i*cols + j]-=other.data[i*cols + j];
                operations++;
            }
        }
        return this;
    }

    public Matrix diagonal() {
        Matrix result = new Matrix(rows, cols);
        int min = Math.min(rows, cols);
        for (int i = 0; i < min; i++) {
            result.data[i*cols+i]=data[i*cols+i];
        }
        return result;
    }

    public Matrix upperTriangular() {
        checkSquared();
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = i + 1; j < cols; j++) {
                result.data[i*cols + j]=data[i*cols + j];
            }
        }
        return result;
    }

    public Matrix lowerTriangular() {
        checkSquared();
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < i; j++) {
                result.data[i*cols + j]=data[i*cols + j];
            }
        }
        return result;
    }

    public Matrix upperHessenberg() {
        checkSquared();
        int n = rows;
        Matrix result = clone();
        Matrix v;
        Matrix p;
        Matrix Hi;
        double s;
        for (int i = 1; i < n - 1; i++) {
            v = result.getSubmatrix(n - i, 1, i + 1, i);
            s = Math.signum(v.get(1,1));
            if (s == 0) {
                s=1;
            }
            p = v.clone().add(getBasisVector(n - i, 1).mult(v.euclideanNorm() * s));
            Hi=getOnes(n - i).sub(p.clone().mult(p.clone().transpose()).mult(2 / p.clone().transpose().mult(p).get(1, 1)));
            Hi = getOnes(n).setSubmatrix(Hi, i+1, i+1);
            result=Hi.clone().transpose().mult(result).mult(Hi);
        }
        return result;
    }

    public Matrix transpose() {
        double[] newData = new double[rows*cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData[j*rows+i]=data[i*cols+j];
            }
        }
        data = newData;
        int tmp = rows;
        rows = cols;
        cols = tmp;
        return this;
    }

    public Matrix LUdecomposition() {
        checkSquared();
        int n=rows;
        Matrix result = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) { //вправо
                //U calculation
                double sum = data[i * n + j];
                for (int k = 0; k < i; k++) {
                    sum -= result.data[i * n + k] * result.data[k * n + j];
                    operations+=2;
                }
                result.data[i * n + j] = sum;
            }
            for (int j = i+1; j < n; j++) { //вниз
                //L calculation
                double sum = data[j*n + i];
                for (int k = 0; k < i; k++) {
                    sum-=result.data[j*n+k]*result.data[k*n + i];
                    operations+=2;
                }
                result.data[j*n + i]=sum/result.data[i*n+i];
                operations++;
            }
        }
        return result;
    }

    public Matrix smartRowLUdecomposition(List<Integer> l) {
        checkSquared();
        List<Integer> permutations = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            permutations.add(i);
        }
        int n=rows;
        Matrix result = new Matrix(n,n);
        Matrix tmp = clone();
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) { //вправо
                //U calculation
                double sum = tmp.data[i * n + j];
                for (int k = 0; k < i; k++) {
                    sum -= result.data[i * n + k] * result.data[k * n + j];
                    operations+=2;
                }
                result.data[i * n + j] = sum;
            }

            //L calculation
            double elem=result.data[i*n+i];
            int ind=i;
            for (int j = i+1; j < n; j++) {
                if (result.data[i * n + j] > elem) {
                    elem=result.data[i * n + j];
                    ind = j;
                }
            }
            tmp.swapColumns(i, ind);
            result.swapColumns(i, ind);
            operations+=3*rows;
            Collections.swap(permutations, i, ind);

            for (int j = i+1; j < n; j++) { //вниз
                double sum = tmp.data[j*n + i];
                for (int k = 0; k < i; k++) {
                    sum-=result.data[j*n+k]*result.data[k*n + i];
                    operations+=2;
                }
                result.data[j*n + i]=sum/elem;
                operations++;
            }
        }
        l.addAll(permutations);
        return result;
    }

    public Matrix holetskyDecomposition() {
        int n = rows;
        Matrix L = new Matrix(n,n);
        for (int i = 0; i<n; i++) {
            //j<i
            for (int j = 0; j < i; j++) {
                L.data[i*n+j]=data[i*n+j];
                for (int k = 0; k < j; k++) {
                    L.data[i*n+j]-=L.data[i*n+k]*L.data[j*n+k];
                }
                L.data[i*n+j]/=L.data[j*n+j];
            }
            //i==j
            double sum = data[i*n+i];
            for (int k = 0; k < i; k++) {
                sum-=L.data[i*n+k]*L.data[i*n+k];
            }
            L.data[i*n+i]=Math.sqrt(sum);
        }
        return L;
    }

    public Matrix jacobiSolver(Matrix b, Matrix x0, double eps) {
        checkSquared();
        Matrix L = lowerTriangular();
        Matrix D = diagonal();
        Matrix R = upperTriangular();

        Matrix invD = D.inverseMatrix();
        Matrix invD2 = invD.clone();

        Matrix B = invD.mult(L.clone().add(R).mult(-1));
        Matrix C = invD2.mult(b);

        Matrix x1 = x0;
        Matrix tmp;
        double q = B.maxColumnNorm();
        if (q >= 1) {
//            throw new UnsupportedOperationException("Matrix norm must be less than 1");
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

    public Matrix gaussZeidelSolver(Matrix right, Matrix x0, double eps) {
        Matrix x1 = x0;
        do {
            x0 = x1.clone();
            for (int i = 0; i < rows; i++) {
                double sum=0;
                for (int j = 0; j < i; j++) {
                    sum-=data[i*cols+j]/data[i*cols+i]*x1.data[j];
                }
                for (int j = i+1; j < rows; j++) {
                    sum-=data[i*cols+j]/data[i*cols+i]*x0.data[j];
                }
                x1.data[i] = sum + right.data[i]/data[i*cols+i];
            }
        } while(x1.clone().sub(x0).octaNorm() > eps);
        return x1;
    }

    public Matrix upperTriangleSolver(Matrix right) {
        checkSquared();
        int n = rows;
        Matrix result = new Matrix(n, 1);
        result.data[n - 1] = right.data[n - 1] / data[n * n - 1];
        operations++;
        for (int i = rows - 2; i >= 0; i--) {
            result.data[i] = right.data[i];
            for (int k = i + 1; k < n; k++) {
                result.data[i] -= data[i*n+k]*result.data[k];
                operations+=2;
            }
            result.data[i]/=data[i*n+i];
            operations++;
        }
        return result;
    }

    public Matrix lowerTriangleSolver(Matrix right) {
        checkSquared();
        int n = rows;
        Matrix result = new Matrix(n, 1);
        result.data[0] = right.data[0] / data[0];
        operations++;
        for (int i = 1; i < rows; i++) {
            result.data[i] = right.data[i];
            for (int k = i - 1; k >= 0; k--) {
                result.data[i] -= data[i*n+k]*result.data[k];
                operations+=2;
            }
            result.data[i]/=data[i*n+i];
            operations++;
        }
        return result;
    }

    public double determinant()  {
        List<Integer> l = new ArrayList<Integer>();
        Matrix LU = smartRowLUdecomposition(l); //smartRowLUdecomposition(new ArrayList<>()); - с точностью до знака
        double d = 1.0;
        for (int i = 0; i < rows; i++) {
            d*=LU.data[i*cols+i];
        }
        return d;
    }

    public Matrix[] QRDecomposition() {
        checkSquared();
        int n = rows;
        Matrix Q = getOnes(n);
        Matrix R = clone();
        Matrix v = getSubmatrix(n, 1, 1, 1);
        Matrix p = v.clone().add(getBasisVector(n, 1).mult(Math.signum(v.get(1,1))*v.euclideanNorm()));
        Matrix pt = p.clone().transpose();
        Matrix Hi = getOnes(n).sub(p.clone().mult(pt).mult(2 / pt.clone().mult(p).get(1, 1)));
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
            Hi=getOnes(n).setSubmatrix(Hi, n-i, n-i);
            R = Hi.clone().mult(R);
            Q = Hi.clone().mult(Q);
        }
        return new Matrix[] {Q.transpose(), R} ;
    }

    public Matrix QREigenvalues(double eps) {
        checkSquared();
        Matrix result = upperHessenberg();
        int n = rows;
        Matrix G, Q=null, R=null, v;
        double s;
        do {
            Q=getOnes(n);
            R=result.clone();
            for (int i = 1; i < n; i++) {
                //элемент i-й строки, i-1 столбца обнулить
                if (result.data[i * cols + i-1] == 0) {
                    continue;
                }
                v = R.getSubmatrix(n, 1, 1, i);
                G = getGivensMatrix(i, v.data[i], i-1, v.data[i-1], n); //для R
                R=G.clone().mult(R);
                Q=G.mult(Q);
            }
            result = R.clone().mult(Q.transpose());
            s=0;
            for (int i = 1; i < n; i++) {
                s+=Math.abs(result.data[i*n+i-1]);
            }
        } while(s > eps);
        return result;
    }

    public Matrix inverseMatrix() {
        Matrix LU = LUdecomposition();
        Matrix inverse = new Matrix(rows, cols);
        int n = rows;
        double sum;
        for (int i = n-1; i >= 0; i--) {
            //row=column
            sum = 1;
            for (int k = i + 1; k < n; k++) {
                sum-=LU.data[i*n+k]*inverse.data[k*n+i];
                operations+=2;
            }
            inverse.data[i*n+i]=sum/LU.data[i*n+i];
            operations++;
            //row<column, j - строка, i - столбец
            for (int j = i-1; j >= 0; j--) {
                sum=0;
                for (int k = j+1; k<n; k++) {
                    sum -= LU.data[j * cols + k] * inverse.data[k * cols + i];
                    operations+=2;
                }
                inverse.data[j*cols+i]=sum/LU.data[j*cols+j];
                operations++;
            }
            //row>column, j - столбец, i - строка
            for (int j = i-1; j >= 0; j--) {
                sum=0;
                for (int k = j+1; k<n; k++) {
                    sum -= LU.data[k * cols + j] * inverse.data[i * cols + k];
                    operations+=2;
                }
                inverse.data[i*cols+j]=sum;
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
            norm += Math.abs(data[i]);
            operations++;
        }
        return norm;
    }

    public double euclideanNorm() {
        double norm = 0;
        for (int i = 0; i < rows * cols; i++) {
            norm+=data[i]*data[i];
            operations+=2;
        }
        return Math.sqrt(norm);
    }

    public double maxColumnNorm() {
        double max = Double.MIN_VALUE;
        for (int j = 0; j < cols; j++) {
            double d = 0.0;
            for (int i = 0; i < rows; i++) {
                d+=Math.abs(data[i*cols + j]);
                operations++;
            }
            if (d > max)
                max = d;
        }
        return max;
    }

    public int getFillingFactor() {
        int counter = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0) {
                counter++;
            }
        }
        return counter;
    }

    public boolean isDiagDominant() {
        int min = Math.min(rows, cols);
        for (int i = 0, sum = 0; i < min; i++, sum = 0) {
            for (int j = 0; j < min; j++) {
                if (j == i) {
                    continue;
                }
                sum += Math.abs(data[i*cols+j]);
            }
            if (Math.abs(data[i * cols + i]) < sum) {
                return false;
            }
        }
        return true;
    }

    public boolean isUpperTriangle(double eps) {
        checkSquared();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < i; j++) {
                if (Math.abs(data[i * cols + j]) >= eps)    {
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
    protected Matrix clone() {
        Matrix result = new Matrix(rows, cols);
        result.data = Arrays.copyOf(data, data.length);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        Matrix other = (Matrix) o;
        if (this == o) {
            return true;
        }
        if (rows != other.rows || cols != other.cols) {
            return false;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] != other.data[i]) {
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
                sb.append(String.format(" %-5.2f", data[i*cols+j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getPortrait() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(data[i*cols+j] != 0)
                    sb.append(String.format(" %-2s", "✚"));
                else
                    sb.append(String.format(" %-2s", " "));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int compare(Matrix other) {
        int k = 0;
        for (int i = 0; i < data.length; i++) {
            if (other.data[i] != 0 && data[i] == 0) {
                k++;
            }
        }
        return k;
    }
}
