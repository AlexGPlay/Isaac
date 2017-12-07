package com.isaac.modelos.disparos;

import android.content.Context;

/**
 * Created by Alejandro García Parrondo on 07/12/2017.
 */

public class DisparoExplosivo extends DisparoEnemigo{

    public DisparoExplosivo(Context context, double xInicial, double yInicial, long tearRange, int damage, int orientacion, int sprite, int fps) {
        super(context, xInicial, yInicial, tearRange, damage, orientacion, sprite, fps);

        TIPO_DISPARO = DISPARO_EXPLOSIVO;
    }

    public BombaActiva putBomb(){
        BombaActiva bomba = new BombaActiva(context,x,y);
        bomba.setTicksToExplode(0);

        return bomba;
    }

}
