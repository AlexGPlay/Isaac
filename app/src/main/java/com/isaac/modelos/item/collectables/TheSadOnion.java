package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 04/11/2017.
 */

public class TheSadOnion extends Item {

    public TheSadOnion(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.thesadonion_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        if(jugador.tearDelay>=50){
            jugador.tearDelay *= jugador.tearDelay*0.75;

            if(jugador.tearDelay<50)
                jugador.tearDelay = 50;
        }
    }

}
