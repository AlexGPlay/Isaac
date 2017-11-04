package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 04/11/2017.
 */

public class NumberOne extends Item{

    public NumberOne(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.numberone_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        if(jugador.tearDelay>=50){
            jugador.tearDelay *= 0.70;

            if(jugador.tearDelay<50)
                jugador.tearDelay = 50;
        }

        if(jugador.tearRange>=150){
            jugador.tearRange *= 0.75;

            if(jugador.tearRange<150)
                jugador.tearRange = 150;
        }

    }

}
