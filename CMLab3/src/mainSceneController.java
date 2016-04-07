import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class mainSceneController implements Initializable{
    @FXML
    private XYChart<Double, Double> chart;

    @FXML
    private Slider slider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slider.valueProperty().addListener(this::sliderHandler);
    }

    private void sliderHandler(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (oldValue.intValue() != newValue.intValue()) {
            int alpha = newValue.intValue();
            int N = 5;
            double l = 0.0, r = 1.0;
            Matrix Y = getSolution(alpha, l, r, N);
            Matrix X = new Matrix(N+1, 1);
            double h = (r-l)/N;
            for (int i = 0; i <= N; i++) {
                X.set(i+1, 1, l+h*i);
            }

            addDataToChart(X, Y, chart, N+1, Math.pow(0.1, newValue.intValue()));

        }
    }

    private void addDataToChart(Matrix X, Matrix Y, XYChart<Double, Double> chart, int N, Double alpha) {
        XYChart.Series<Double, Double> series = new XYChart.Series();
        DecimalFormat format = new DecimalFormat("0.##########");
        series.setName(format.format(alpha));
        for (int i = 1; i <= N; i++) {
            series.getData().add(new XYChart.Data<>(X.get(i, 1), Y.get(i, 1)));
        }
        chart.getData().remove(0, chart.getData().size());
        chart.getData().add(series);
    }

    public Matrix getSolution(double alpha, double l, double r, int N) {
        double h = (r-l)/N;
        Matrix A = new Matrix(N+1,N+1);
        Matrix b = new Matrix(N+1,1);

        for (int i = 0; i <= N; i++) {
            b.set(i+1,1,l+h*i);
            for (int j = 0; j <= N; j++) {
                if (j == 0 || j == N) {
                    A.set(i + 1, j + 1, Math.pow(l + h * i, l + h * j) * h/2);
                } else {
                    A.set(i + 1, j + 1, Math.pow(l + h * i, l + h * j) * h);
                }
            }
        }

        Matrix C = A.clone().transpose().mult(A).add(Matrix.getOnes(N+1).mult(alpha*alpha));
        Matrix d = A.clone().transpose().mult(b);
        Matrix LU = C.LUdecomposition();

        Matrix L = LU.lowerTriangular().add(Matrix.getOnes(N+1));
        Matrix U = LU.upperTriangular().add(LU.diagonal());
        Matrix x = U.upperTriangleSolver(L.lowerTriangleSolver(d));

        Matrix coeffs = new Matrix(N+1, N+1);

        //Проверка
//        System.out.println("Test: " + C.clone().mult(x).sub(d).maxColumnNorm());
        return x;
    }
}
