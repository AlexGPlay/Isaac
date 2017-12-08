package com.isaac.modelos.enemigo.monsters;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.enemigo.EnemigoBase;
import com.isaac.modelos.nivel.Sala;

/**
 * Created by Pelayo on 08/12/2017.
 */

public class Fly extends EnemigoBase {
    private final static int altura = 32;
    private final static int ancho = 32;

    public final static String MOVIMIENTO = "moviendose";


    public Fly(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, altura, ancho, Modelo.SOLIDO);

        comportamiento = PERSEGUIDOR;

        this.x = xInicial;
        this.y = yInicial;

        aceleracionX=2;
        aceleracionY=2;


        HP = 15;

        estado = ESTADO_VIVO;
        fly = true;

        inicializar();
    }

    public void inicializar(){
        Sprite movimiento = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.moscas_duke),
                ancho, altura,
                2, 2, true);
        spriteCuerpo = movimiento;

        sprites.put(MOVIMIENTO, movimiento);


    }

    @Override
    public void actualizar(long tiempo) {
        boolean finalizado = spriteCuerpo.actualizar(tiempo);

        if(getHP()<=0)
            estado = ESTADO_MUERTO;
    }



    @Override
    public void dibujar(Canvas canvas) {
        spriteCuerpo.dibujarSprite(canvas,(int)x- Sala.scrollEjeX,(int)y - Sala.scrollEjeY);
    }


}
