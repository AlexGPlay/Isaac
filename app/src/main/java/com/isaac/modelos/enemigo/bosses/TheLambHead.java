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

public class TheLambHead extends EnemigoBase {

    private final static int altura = 56;
    private final static int ancho = 97;

    private long tearDelay;
    private long tearRange;
    private int tearDamage;
    private int actualDelay;

    private boolean hasShot = false;

    public final static String MOVIMIENTO = "moviendose";
    public final static String DISPARANDO = "disparando";

    public final static int ESTADO_DISPARANDO = 2;

    public TheLambHead(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, altura, ancho, Modelo.SOLIDO);

        this.x = xInicial;
        this.y = yInicial;

        aceleracionX=1;
        aceleracionY=1;

        tearDelay = 2000;
        tearRange = 2000;
        actualDelay = 0;
        tearDamage = 1;

        HP = 2000;

        estado = ESTADO_VIVO;
        fly = true;

        inicializar();
    }

    public void inicializar(){
        Sprite movimiento = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.the_lamb_cabeza),
                ancho, altura,
                1, 1, true);

        sprites.put(MOVIMIENTO, movimiento);

        Sprite disparo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.the_lamb_cabeza_disparar),
                ancho, altura,
                2, 2, false);

        sprites.put(DISPARANDO, disparo);

        spriteCuerpo = sprites.get(MOVIMIENTO);
    }

    @Override
    public void actualizar(long tiempo) {
        boolean finalizado = spriteCuerpo.actualizar(tiempo);

        if(timeToShot() && estado==ESTADO_VIVO){
            estado = ESTADO_DISPARANDO;
            spriteCuerpo.setFrameActual(0);
            spriteCuerpo = sprites.get(DISPARANDO);
            Log.d("Lamb head", "Disparo");
        }

        if(finalizado && estado==ESTADO_DISPARANDO){
            estado = ESTADO_VIVO;
            hasShot = true;
            spriteCuerpo.setFrameActual(0);
            spriteCuerpo = sprites.get(MOVIMIENTO);
            Log.d("Lamb head", "Normalidad");
        }

        actualDelay += tiempo;

        if(getHP()<=0)
            estado = ESTADO_MUERTO;
    }

    public boolean timeToShot(){
        if (actualDelay> tearDelay + Math.random()* tearDelay)
            return true;

        return false;
    }

    @Override
    public ArrayList<DisparoEnemigo> disparar(Jugador jugador){
        ArrayList<DisparoEnemigo> disparos = new ArrayList<>();

        if(hasShot) {
            actualDelay = 0;

            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ABAJO) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ARRIBA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_DERECHA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_IZQUIERDA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ABAJO_DERECHA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ABAJO_IZQUIERDA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ARRIBA_DERECHA) );
            disparos.add( new DisparoEnemigo(context, x, y, tearRange, tearDamage, MOVIMIENTO_ARRIBA_IZQUIERDA) );

            hasShot = false;
        }

        return disparos;
    }


    @Override
    public void dibujar(Canvas canvas) {
        spriteCuerpo.dibujarSprite(canvas,(int)x- Sala.scrollEjeX,(int)y - Sala.scrollEjeY);
    }

}
