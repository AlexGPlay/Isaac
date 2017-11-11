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
        jugador.setTearDelay( (long)(jugador.getTearDelay()*0.75) );
        jugador.setTearRange( (int)(jugador.getTearRange()*0.75) );

    }

}
