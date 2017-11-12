package com.isaac.modelos.nivel;

import android.content.Context;

/**
 * Created by Alejandro García Parrondo on 12/11/2017.
 */

public class PuertaLlave extends Puerta{

    private int llavesNecesarias;

    public PuertaLlave(Context context, double x, double y, int alto, int ancho, int puertaAbierta, int puertaCerrada) {
        super(context, x, y, alto, ancho, puertaAbierta, puertaCerrada);

        llavesNecesarias = 1;
    }

    public void insertKey(){
        if(llavesNecesarias>0)
            llavesNecesarias--;

        if(llavesNecesarias==0)
            setAbierta(true);
    }

    @Override
    public void setForzada(boolean forzada){}

}
