package com.isaac.modelos.enemigo.monsters;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.disparos.BombaActiva;
import com.isaac.modelos.disparos.DisparoEnemigo;
import com.isaac.modelos.disparos.DisparoExplosivo;
import com.isaac.modelos.enemigo.EnemigoBase;
import com.isaac.modelos.nivel.Sala;

import java.util.ArrayList;

/**
 * Created by Pelayo on 08/12/2017.
 */

public class BoomFly extends EnemigoBase {
    private final static int altura = 32;
    private final static int ancho = 32;

    public final static String MOVIMIENTO = "moviendose";

    public static final int ESTADO_EXPLOTAR = 4;

    private boolean explotado = false;

    public BoomFly(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, altura, ancho, Modelo.SOLIDO);

        comportamiento = ALEATORIO;

        this.x = xInicial;
        this.y = yInicial;

        aceleracionX=2;
        aceleracionY=2;

        HP = 15;

        estado = ESTADO_VIVO;
        fly = true;
        explotado = false;

        inicializar();
    }

    public void inicializar(){
        Sprite movimiento = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.boomfly_movimiento),
                ancho, altura,
                2, 2, true);
        spriteCuerpo = movimiento;

        sprites.put(MOVIMIENTO, movimiento);


    }

    @Override
    public void actualizar(long tiempo) {
        boolean finalizado = spriteCuerpo.actualizar(tiempo);

        if(getHP()<=0 && !explotado) {
            estado = ESTADO_EXPLOTAR;
            explotado = true;
        }
    }

    @Override
    public ArrayList<DisparoEnemigo> disparar(Jugador jugador){
        ArrayList<DisparoEnemigo> disparos = new ArrayList<>();

        if(estado == ESTADO_EXPLOTAR){
            disparos.add( new DisparoExplosivo(context, x, y, 0, 0, MOVIMIENTO_ABAJO, R.drawable.disparo_explosivo, 1) );
            estado = ESTADO_MUERTO;
        }

        return disparos;
    }

    @Override
    public void dibujar(Canvas canvas) {
        spriteCuerpo.dibujarSprite(canvas,(int)x- Sala.scrollEjeX,(int)y - Sala.scrollEjeY);
    }


}
