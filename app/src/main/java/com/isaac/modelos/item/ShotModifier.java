package com.isaac.modelos.item;

import android.content.Context;

import com.isaac.modelos.disparos.DisparoJugador;

import java.util.ArrayList;

/**
 * Created by Alex on 04/11/2017.
 */

public interface ShotModifier {

    ArrayList<DisparoJugador> shot(Context context, ArrayList<DisparoJugador> disparos, double x, double y, long tearRange, double tearDamage, int orientacion);

}
