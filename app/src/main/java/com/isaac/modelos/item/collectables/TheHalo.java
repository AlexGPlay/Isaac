package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 04/11/2017.
 */

public class TheHalo extends Item {

    public TheHalo(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.thehalo_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        if(jugador.actualMaxHP<jugador.maxHP){
            jugador.actualMaxHP = jugador.actualMaxHP+2;
            jugador.HP = jugador.actualMaxHP;
        }

        if(jugador.tearDelay>=50){
            jugador.tearDelay *= 0.90;

            if(jugador.tearDelay<50)
                jugador.tearDelay = 50;
        }

        if(jugador.speed<10){
            jugador.speed += 0.5;

            if(jugador.speed>10)
                jugador.speed = 10;
        }

        jugador.tearDamage *= 1.25;
        jugador.tearRange *= 1.25;
    }

}
