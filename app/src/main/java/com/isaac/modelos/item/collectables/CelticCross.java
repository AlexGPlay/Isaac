package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.item.DamageModifier;
import com.isaac.modelos.item.Item;

/**
 * Created by alexgp1234 on 12/11/17.
 */

public class CelticCross extends Item implements DamageModifier {

    public CelticCross(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.celticcross_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.addDamageModifier(this);
    }

    @Override
    public int processDamage(Jugador jugador, int damage) {

        if(damage>0){
            jugador.setShielded(true);
            jugador.setActualShield(0);
            jugador.setMaxShield(1500);
        }

        return damage;
    }
}
