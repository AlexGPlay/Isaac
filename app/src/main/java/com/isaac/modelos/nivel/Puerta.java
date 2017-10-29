package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;

/**
 * Created by Alex on 25/10/2017.
 */

public class Puerta extends Modelo {

    private boolean abierta;
    private int xSalida, ySalida;

    public Puerta(Context context, double x, double y, int alto, int ancho, int drawable) {
        super(context, x, y, alto, ancho);

        imagen = CargadorGraficos.cargarDrawable(context, drawable);
        abierta = true;
    }

    public void setTile(int x, int y){
        this.xSalida = x;
        this.ySalida = y;
    }

    public int getXSalida(){
        return xSalida;
    }

    public int getYSalida(){
        return ySalida;
    }

    @Override
    public void dibujar(Canvas canvas){
        int yArriba = (int)  y - altura / 2 - Sala.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Sala.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

}
