package com.isaac.modelos.item;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.item.collectables.BloodOfTheMartyr;
import com.isaac.modelos.item.collectables.Breakfast;
import com.isaac.modelos.item.collectables.ItemID;
import com.isaac.modelos.item.collectables.MomsHeels;
import com.isaac.modelos.item.collectables.NumberOne;
import com.isaac.modelos.item.collectables.TheHalo;
import com.isaac.modelos.item.collectables.TheSadOnion;
import com.isaac.modelos.nivel.Nivel;
import com.isaac.modelos.nivel.Sala;

/**
 * Created by alexgp1234 on 29/10/17.
 */

public class Altar extends Modelo {

    private Item item;
    private boolean picked;

    public Altar(Context context, double x, double y, Nivel nivel) {
        super(context, x, y, 23, 27);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.altar_objeto);

        picked = false;

        int itemID = (int)(Math.random()*nivel.itemPool.size());
        itemID = nivel.itemPool.get(itemID);

        item = generateItem(context, itemID);
    }

    private Item generateItem(Context context, int id){

        switch(id){
            case ItemID.BLOOD_OF_THE_MARTYR:
                return new BloodOfTheMartyr(context, this.x, this.y-20);

            case ItemID.BREAKFAST:
                return new Breakfast(context, this.x, this.y-20);

            case ItemID.MOMS_HEELS:
                return new MomsHeels(context, this.x, this.y-20);

            case ItemID.NUMBER_ONE:
                return new NumberOne(context, this.x, this.y-20);

            case ItemID.THE_HALO:
                return new TheHalo(context, this.x, this.y-20);

            case ItemID.THE_SAD_ONION:
                return new TheSadOnion(context, this.x, this.y-20);

        }

        return null;
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
