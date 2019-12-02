package client;

import compute.Task;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Fabian Traxler {@literal <ftraxler@student.tgm.ac.at>}
 * @version 12.03.19
 * @project syt4-gk833-rmi-taskloadbalancer-ftraxler-tgm
 */
public class Fibonacci implements Task<BigInteger>, Serializable{
    
    private static final Long serialVersionUID = 223L;


    /** digits of precision after the decimal point */
    private final int digits;

    /**
     * Construct a task to calculate Fibonacci to the specified
     * precision.
     */
    public Fibonacci(int digits) {
        this.digits = digits;
    }

    /**
     * Calculate Fibonacci.
     */
    public BigInteger execute() {
        return computeFibonacci(digits);
    }

    /**
     * Compute the value of Fibonacci to the specified number of
     * digits
     */
    public static BigInteger computeFibonacci(int digits) {



            BigInteger i = BigInteger.valueOf(0), j = BigInteger.valueOf(1),t;
            int k;
            if (digits == 0)
                return BigInteger.valueOf(0);
            for (k = 1; k < digits; ++k)
            {
                t = i.add(j);
                i = j;
                j = t;
            }
            return j;


    }

}
