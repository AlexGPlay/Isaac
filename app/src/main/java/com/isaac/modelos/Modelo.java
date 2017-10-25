package com.isaac.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Modelo {

    public Context context;
    public double x;
    public double y;
    public int altura;
    public int ancho;
    protected Drawable imagen;

    public Modelo(Context context, double x, double y, int altura, int ancho){;
        this.context = context;
        this.x = x;
        this.y = y;
        this.altura = altura;
        this.ancho = ancho;
    }

    public void dibujar(Canvas canvas){
        int yArriba = (int)  y - altura / 2;
        int xIzquierda = (int) x - ancho / 2;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

    // No Actualiza
    public void actualizar (long tiempo){

    }

}


