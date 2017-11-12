package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.item.Item;
import com.isaac.modelos.item.ShotModifier;

import java.util.ArrayList;

/**
 * Created by Alex on 05/11/2017.
 */

public class DoubleShot extends Item implements ShotModifier{

    public DoubleShot(Context context, double x, double y) {
        super(context, x, y, 32, 32, R.drawable.doubleshot_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.addShotModifier(this);
    }

    @Override
    public ArrayList<DisparoJugador> shot(Context context, ArrayList<DisparoJugador> disparos, double x, double y, long tearRange, double tearDamage, int orientacion) {
        DisparoJugador prueba = disparos.get(0);
        disparos.clear();

        DisparoJugador disparo1 = null;
        DisparoJugador disparo2 = null;

        if(orientacion == Jugador.DISPARO_DERECHA){
            disparo1 = prueba.clone();
            disparo1.setY( y-5 );

            disparo2 = prueba.clone();
            disparo2.setY(y+5);
        }

        else if(orientacion == Jugador.DISPARO_IZQUIERDA){
            disparo1 = prueba.clone();
            disparo1.setY(y-5);

            disparo2 = prueba.clone();
            disparo2.setY(y+5);
        }

        else if(orientacion == Jugador.DISPARO_ABAJO){
            disparo1 = prueba.clone();
            disparo1.setX(x-5);

            disparo2 = prueba.clone();
            disparo2.setX(x+5);
        }

        else if(orientacion == Jugador.DISPARO_ARRIBA){
            disparo1 = prueba.clone();
            disparo1.setX(x-5);

           disparo2 = prueba.clone();
           disparo2.setX(x+5);
        }

        disparos.add(disparo1);
        disparos.add(disparo2);
        return disparos;
    }
}
