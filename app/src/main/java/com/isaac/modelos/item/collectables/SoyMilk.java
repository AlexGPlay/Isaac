package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 04/11/2017.
 */

public class SoyMilk extends Item {

    public SoyMilk(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.soymilk_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.tearDamage *= 0.2;
        jugador.tearDelay = (jugador.tearDelay/4)-50;

        if(jugador.tearDelay<25)
            jugador.tearDelay = 25;
    }

}
