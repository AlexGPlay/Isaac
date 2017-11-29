package com.isaac.modelos.item.pickups;

import android.content.Context;

import com.isaac.R;
import com.isaac.gestores.GestorAudio;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.Item;

/**
 * Created by Alex on 31/10/2017.
 */

public class Moneda extends Item {
    public Moneda(Context context, double x, double y) {
        super(context, x, y, 13, 18, R.drawable.pickup_moneda);
    }

    @Override
    public void doStuff(Jugador jugador) {
        GestorAudio.getInstancia().reproducirSonido(GestorAudio.PICK_MONEDA);
        jugador.setNumMonedas(jugador.getNumMonedas()+1);
    }
}
