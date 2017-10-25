package com.isaac.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;

/**
 * Created by Alex on 25/10/2017.
 */

public class Puerta extends Modelo{

    private boolean abierta;

    public Puerta(Context context, double x, double y) {
        super(context, x, y, 32, 40);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.door);
        abierta = true;
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
