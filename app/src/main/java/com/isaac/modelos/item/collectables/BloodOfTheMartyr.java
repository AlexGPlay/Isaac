package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 04/11/2017.
 */

public class BloodOfTheMartyr extends Item {

    public BloodOfTheMartyr(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.bloodofthemartyr_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.tearDamage *= 1.75;
    }

}
