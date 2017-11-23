package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;

/**
 * Created by Alejandro García Parrondo on 08/11/2017.
 */

public class Roca extends Modelo {

    public Roca(Context context, double x, double y) {
        super(context, x, y, 30, 30, Modelo.SOLIDO);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.rock_1);
    }

    @Override
    public void dibujar(Canvas canvas){
        int yArriba = (int) (y - altura / 2) - Sala.scrollEjeY;
        int xIzquierda = (int) (x - ancho / 2) - Sala.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

    public int getTipoModelo(){
        return Modelo.ROCA;
    }

}
