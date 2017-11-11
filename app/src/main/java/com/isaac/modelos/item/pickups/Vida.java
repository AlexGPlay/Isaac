package com.isaac.modelos.item.pickups;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 31/10/2017.
 */

public class Vida extends Item {
    public Vida(Context context, double x, double y) {
        super(context, x, y, 13, 16, R.drawable.pickup_vida);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.setHP(jugador.HP+2);
    }
}
