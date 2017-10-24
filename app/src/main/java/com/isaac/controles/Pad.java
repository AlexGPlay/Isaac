package com.isaac.controles;

/**
 * Created by Pelayo on 24/10/2017.
 */

import android.content.Context;
import android.util.Log;
import com.isaac.R;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;

/**
 * Created by Pelayo on 25/09/2017.
 */

public class Pad extends Modelo {

    public Pad(Context context, double x, double y) {
        super(context, x, y,100,100);
        altura = 100;
        ancho = 100;
        imagen = context.getResources().getDrawable(R.drawable.pad);
    }
    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) &&
                clickX >= (x - ancho / 2) &&
                clickY <= (y + altura / 2) &&
                clickY >= (y - altura / 2)) {

            estaPulsado = true;
        }

        return estaPulsado;
    }


    public int getOrientacion(float clickX, float clickY){
        Log.d("CliclX",String.valueOf(clickX));
        Log.d("CliclY",String.valueOf(clickY));
        double arribaX=x-ancho/2;
        double arribaY = y-altura/2;
        double fromLeft=clickX-arribaX;
        double fromTop=clickY-arribaY;

        double angulo=(Math.toDegrees( Math.atan2(fromLeft - 50.0, 50.0 - fromTop) ) + 360.0) % 360.0;
        Log.d("angulo",String.valueOf(angulo));
        if(angulo>=45 && angulo<135)
            return Jugador.MOVIMIENTO_DERECHA;

        if(angulo>=135 && angulo<225)
            return Jugador.MOVIMIENTO_ABAJO;

        if(angulo>=225 && angulo<315)
            return Jugador.MOVIMIENTO_IZQUIERDA;

        else
            return Jugador.MOVIMIENTO_ARRIBA;
    }


}
