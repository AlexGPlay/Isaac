package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by alexgp1234 on 11/11/17.
 */

public class Fate extends Item{

    public Fate(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.fate_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.setFlying(true);
    }

}
