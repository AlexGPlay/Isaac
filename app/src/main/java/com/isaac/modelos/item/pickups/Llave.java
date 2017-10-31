package com.isaac.modelos.item.pickups;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 31/10/2017.
 */

public class Llave extends Item {
    public Llave(Context context, double x, double y) {
        super(context, x, y, 20, 14, R.drawable.pickup_llave);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.setNumLlaves(jugador.getNumLlaves()+1);
    }
}
