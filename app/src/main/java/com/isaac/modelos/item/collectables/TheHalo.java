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
        jugador.setMaxHP(jugador.actualMaxHP+2);
        jugador.setHP(jugador.actualMaxHP);
        jugador.setTearDelay((int)(jugador.tearDelay*0.9));

        jugador.setMovementSpeed( (int)(jugador.speed + 0.5));

        jugador.tearDamage *= 1.25;
        jugador.setTearRange( (int)(jugador.tearRange*1.25) );
    }

}
