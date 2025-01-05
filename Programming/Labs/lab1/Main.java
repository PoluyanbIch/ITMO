import java.lang.Math;
import java.util.Set;

public class Main {
    private static int[] task1() {
        int[] w = new int[12];
        for (int i = 2 ; i < 25 ; i+=2) {
            w[i / 2 - 1] = i;
        }
        return w;
    }
    
    private static double[] task2() {
        double[] x = new double[15];
        for (int i = 0 ; i != 15 ; ++i) {
            x[i] = 12 - (Math.random() * 26);
        }
        return x;
    }

    private static double calculateValue(int z, double x) {
        double result;
        Set<Integer> supportSet = Set.of(2, 4, 10, 12, 22, 24); 
        if (z == 6) {
            result = Math.pow(Math.cos(Math.pow(x + 4, 2)), 4 * (Math.sin(Math.tan(x))));
        }
        else if (supportSet.contains(z)) {
            result = Math.pow(Math.pow(Math.asin((x - 1) / 26) / 6, Math.cbrt(x)) * (1 - Math.cbrt(Math.PI * (x + 0.25))), 3);
        }
        else {
            double power_base = Math.pow((0.5 - Math.pow(0.25 / (x - 1), x)) / (Math.pow((2 - x), 3)), 3);
            double power_exponent1 = 3 * Math.pow((Math.atan((x - 1) / 26) + 1) / (Math.cos(x)), 3);
            double power_exponent2 = Math.tan(Math.pow(Math.E, Math.pow(3 * (0.25 + x), 3)));
            result = Math.pow((2 / 3) * (1 - Math.pow(power_base, power_exponent1)), power_exponent2);
        }
        return result;
    }

    private static double[][] task3() {
        int[] wArray = task1();
        double[] xArray = task2();
        double[][] z = new double[12][15];
        for (int i = 0 ; i != 12 ; ++i) {
            for (int j = 0 ; j != 15 ; ++j) {
                z[i][j] = calculateValue(wArray[i], xArray[j]);
            }
        }
        return z;
    }

    private static void prettyPrint() {
        double[][] resultArray = task3();
        for (int i = 0 ; i != resultArray.length ; ++i) {
            for (int j = 0 ; j != resultArray[0].length ; ++j) {
                System.out.print(String.format("%15.5f", resultArray[i][j]));
            }
            System.out.println();
        }
    }

    

    public static void main(String[] args) {
        
    }
}

