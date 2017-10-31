package com.isaac.modelos.hud;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.modelos.Modelo;

/**
 * Created by Alex on 31/10/2017.
 */

public class IconoMoneda extends Modelo {

    private int monedas;

    public IconoMoneda(Context context, double x, double y) {
        super(context, x, y, 20,15);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.hud_monedas);
    }

    public void actualizar(int numMonedas){
        this.monedas = numMonedas;
    }

    @Override
    public void dibujar(Canvas canvas){
        super.dibujar(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        canvas.drawText(String.valueOf(monedas), (int)x+10, (int)y+altura/2, paint);
    }

}
