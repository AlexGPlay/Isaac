package com.isaac.modelos.item;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.nivel.Sala;

/**
 * Created by alexgp1234 on 29/10/17.
 */

public abstract class Item extends Modelo {

    public Item(Context context, double x, double y, int altura, int ancho, int drawable){
        super(context,x,y,altura,ancho);

        imagen = CargadorGraficos.cargarDrawable(context, drawable);
    }

    @Override
    public void dibujar(Canvas canvas){
        int yArriba = (int)  (y - altura / 2) - Sala.scrollEjeY;
        int xIzquierda = (int) (x - ancho / 2) - Sala.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

    public abstract void doStuff(Jugador jugador);

}
