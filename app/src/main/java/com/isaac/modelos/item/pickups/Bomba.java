package com.isaac.modelos.item.pickups;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 31/10/2017.
 */

public class Bomba extends Item {
    public Bomba(Context context, double x, double y) {
        super(context, x, y, 21, 20, R.drawable.pickup_bomba);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.setNumBombas(jugador.getNumBombas()+1);
    }
}
