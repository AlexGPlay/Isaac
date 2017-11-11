package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 04/11/2017.
 */

public class MomsHeels extends Item {

    public MomsHeels(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.momsheels_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.setTearRange((long)(jugador.getTearRange()*1.5));
    }
}
