package com.isaac.modelos.item.Collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by alexgp1234 on 29/10/17.
 */

public class Desayuno extends Item{

    public Desayuno(Context context, double x, double y) {
        super(context, x, y, 25, 25, R.drawable.breakfast_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        if(jugador.actualMaxHP<jugador.maxHP){
            jugador.actualMaxHP = jugador.actualMaxHP+2;
            jugador.HP = jugador.actualMaxHP;
        }
    }
}
