package com.isaac.modelos.hud;

import android.content.Context;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;

/**
 * Created by alexgp1234 on 29/10/17.
 */

public class IconoVida extends Modelo {

    public final static int FULL_HP = 1;
    public final static int HALF_HP = 2;
    public final static int EMPTY_HP = 3;

    public IconoVida(Context context, double x, double y, int estado) {
        super(context, x, y, 25,25);

        if(estado == FULL_HP)
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.full_hp);

        else if(estado == HALF_HP)
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.half_hp);

        else if(estado == EMPTY_HP)
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.empty_hp);
    }

}
