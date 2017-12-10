package com.isaac.modelos.item.collectables;

import android.content.Context;

import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.item.Item;
import com.isaac.modelos.item.ShotModifier;

import java.util.ArrayList;

/**
 * Created by alexgp1234 on 10/12/17.
 */

public class SpiritOfTheNight extends Item implements ShotModifier {

    public SpiritOfTheNight(Context context, double x, double y) {
        super(context, x, y, 22, 23, R.drawable.spiritofthenight_icon);
    }

    @Override
    public void doStuff(Jugador jugador) {
        jugador.addShotModifier(this);
    }

    @Override
    public ArrayList<DisparoJugador> shot(Context context, ArrayList<DisparoJugador> disparos, double x, double y, long tearRange, double tearDamage, int orientacion) {
        for(DisparoJugador disparo : disparos)
            disparo.setEspectral(true);

        return disparos;
    }

}
