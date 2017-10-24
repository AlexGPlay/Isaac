package com.isaac.gestores;

/**
 * Created by Jordan on 23/08/2015.
 */
public class Utilidades {

    public static double proximoACero(double a, double b) {
        if (Math.pow(a,2) <  Math.pow(b,2))
            return a;
        else
            return b;

    }
}
