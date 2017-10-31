package com.isaac.modelos.item;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.item.collectables.Desayuno;
import com.isaac.modelos.nivel.Sala;

/**
 * Created by alexgp1234 on 29/10/17.
 */

public class Altar extends Modelo {

    private Item item;
    private boolean picked;

    public Altar(Context context, double x, double y) {
        super(context, x, y, 23, 27);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.altar_objeto);

        picked = false;
        item = new Desayuno(context,this.x,this.y-20);
    }

    @Override
    public void dibujar(Canvas canvas){
        int yArriba = (int)  (y - altura / 2) - Sala.scrollEjeY;
        int xIzquierda = (int) (x - ancho / 2) - Sala.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);

        if(!picked)
            item.dibujar(canvas);
    }

    public Item pickItem(){
        if(!picked) {
            picked = true;
            return item;
        }

        return null;
    }

}
