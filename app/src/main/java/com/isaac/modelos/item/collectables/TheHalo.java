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
        jugador.setMaxHP(jugador.getMaxHP()+2);
        jugador.setHP(jugador.getMaxHP());
        jugador.setTearDelay((int)(jugador.getTearDelay()*0.9));

        jugador.setMovementSpeed( (int)(jugador.getMovementSpeed() + 0.5));

        jugador.setTearDamage( jugador.getTearDamage()*1.25 );
        jugador.setTearRange( (long)(jugador.getTearRange()*1.25) );
    }

}
