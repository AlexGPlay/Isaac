package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;

/**
 * Created by Alex on 05/11/2017.
 */

public class Trampilla extends Modelo {

    public boolean activa;
    private Nivel nivel;

    public Trampilla(Context context, double x, double y, Nivel nivel) {
        super(context, x, y, 23, 27);

        this.nivel = nivel;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.trampilla_abierta);
        activa = false;
    }

    @Override
    public void dibujar(Canvas canvas){
        if(activa) {
            int yArriba = (int) (y - altura / 2) - Sala.scrollEjeY;
            int xIzquierda = (int) (x - ancho / 2) - Sala.scrollEjeX;

            imagen.setBounds(xIzquierda, yArriba, xIzquierda
                    + ancho, yArriba + altura);
            imagen.draw(canvas);
        }
    }

    public void levelChange() throws Exception {
        if(activa)
            nivel.changeLevel();
    }

    public int getTipoModelo(){
        return Modelo.TRAMPILLA;
    }

}
