package com.isaac.modelos.enemigo.bosses;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.disparos.DisparoEnemigo;
import com.isaac.modelos.enemigo.EnemigoBase;
import com.isaac.modelos.nivel.Sala;

import java.util.ArrayList;

/**
 * Created by Alejandro García Parrondo on 06/12/2017.
 */

public class TheLambBody extends EnemigoBase {

    private final static int altura = 25;
    private final static int ancho = 44;

    private long tearDelay;
    private long tearRange;
    private int tearDamage;
    private int actualDelay;

    public TheLambBody(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, altura, ancho, Modelo.SOLIDO);

        this.x = xInicial;
        this.y = yInicial;

        aceleracionX=0;
        aceleracionY=0;

        tearDelay = 2000;
        tearRange = 5000;
        actualDelay = 0;
        tearDamage = 1;

        HP = 300;

        estado = ESTADO_VIVO;

        inicializar();
    }

    public void inicializar(){
        spriteCuerpo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.the_lamb_cuerpo),
                ancho, altura,
                1, 1, true);
    }

    @Override
    public void actualizar(long tiempo) {
        actualDelay += tiempo;

        if(getHP()<=0)
            estado = ESTADO_MUERTO;

        spriteCuerpo.actualizar(tiempo);
    }


    @Override
    public ArrayList<DisparoEnemigo> disparar(Jugador jugador){
        ArrayList<DisparoEnemigo> disparos = new ArrayList<>();

        if(actualDelay > tearDelay + Math.random()* tearDelay) {
            actualDelay = 0;

            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ABAJO) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ARRIBA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_DERECHA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_IZQUIERDA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ABAJO_DERECHA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ABAJO_IZQUIERDA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ARRIBA_DERECHA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ARRIBA_IZQUIERDA) );

        }

        return disparos;
    }


    @Override
    public void dibujar(Canvas canvas) {
        spriteCuerpo.dibujarSprite(canvas,(int)x- Sala.scrollEjeX,(int)y - Sala.scrollEjeY);
    }

}
