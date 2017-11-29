package com.isaac.modelos.enemigo;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.nivel.Sala;

import java.util.HashMap;

/**
 * Created by Pelayo on 27/10/2017.
 */

public class EnemigoMelee extends EnemigoBase {

    public final static int ESTADO_VIVO = 0;
    public final static int ESTADO_MUERTO = 1;

    private static final String CABEZA_DERECHA = "cabeza_derecha";
    private static final String CABEZA_IZQUIERDA = "cabeza_izquierda";
    private static final String CABEZA_ATRAS = "cabeza_atras";
    private static final String CABEZA_ADELANTE = "cabeza_adelante";
    private static final String MOVER_ADELANTE_ATRAS = "mover_adelante";
    private static final String MOVER_DERECHA = "mover_derecha";
    private static final String MOVER_IZQUIERDA = "mover_izquierda";
    private static final String PARADO_SPRITE = "parado";

    private final static int alturaCabeza = 25;
    private final static int anchoCabeza = 29;
    private final static int alturaCuerpo = 14;
    private final static int anchoCuerpo = 32;


    public EnemigoMelee(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, alturaCabeza+alturaCuerpo, Math.max(anchoCabeza,anchoCuerpo), Modelo.SOLIDO);

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        aceleracionX = 2;
        aceleracionY = 2;

        HP = 10;
        estado = ESTADO_VIVO;

        inicializar();
    }

    public void inicializar (){

        Sprite cabezaDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_cabeza_adelante),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_DERECHA, cabezaDerecha);

        Sprite cabezaIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_cabeza_adelante),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_IZQUIERDA, cabezaIzquierda);

        Sprite cabezaAtras = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_cabeza_atras),
                anchoCabeza, alturaCabeza,
                1, 1, true);
        sprites.put(CABEZA_ATRAS, cabezaAtras);

        Sprite cabezaAdelante = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_cabeza_adelante),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_ADELANTE, cabezaAdelante);

        Sprite cuerpoDerechaDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_caminar_derecha),
                anchoCuerpo, alturaCuerpo,
                8, 8, true);
        sprites.put(MOVER_DERECHA, cuerpoDerechaDerecha);

        Sprite cuerpoDerechaIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_caminar_izquierda),
                anchoCuerpo, alturaCuerpo,
                8, 8, true);
        sprites.put(MOVER_IZQUIERDA, cuerpoDerechaIzquierda);

        Sprite cuerpoAdelanteAtras = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_caminar_adelante),
                anchoCuerpo, alturaCuerpo,
                8, 8, true);
        sprites.put(MOVER_ADELANTE_ATRAS, cuerpoAdelanteAtras);

        Sprite cuerpoParado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_caminar_adelante),
                anchoCuerpo, alturaCuerpo,
                8, 8, true);
        sprites.put(PARADO_SPRITE, cuerpoParado);


        spriteCabeza = sprites.get(CABEZA_ADELANTE);
        spriteCuerpo = sprites.get(PARADO_SPRITE);

        movimiento = MOVIMIENTO_PARADO;

    }

    @Override
    public void actualizar (long tiempo) {
        if(movimiento == MOVIMIENTO_ABAJO){
            spriteCabeza = sprites.get(CABEZA_ADELANTE);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }

        else if(movimiento == MOVIMIENTO_ARRIBA){
            spriteCabeza = sprites.get(CABEZA_ATRAS);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }

        else if(movimiento == MOVIMIENTO_DERECHA){
            spriteCabeza = sprites.get(CABEZA_DERECHA);
            spriteCuerpo = sprites.get(MOVER_DERECHA);
        }

        else if(movimiento == MOVIMIENTO_IZQUIERDA){
            spriteCabeza = sprites.get(CABEZA_IZQUIERDA);
            spriteCuerpo = sprites.get(MOVER_IZQUIERDA);
        }

        else if(movimiento == MOVIMIENTO_PARADO){
            spriteCabeza = sprites.get(CABEZA_ADELANTE);
            spriteCuerpo = sprites.get(PARADO_SPRITE);
        }

        spriteCuerpo.actualizar(tiempo);
        spriteCabeza.actualizar(tiempo);
    }

    @Override
    public void dibujar(Canvas canvas){
        int xCabeza = (int)(x-(ancho/2)+(anchoCabeza/2));
        int yCabeza = (int)(y-(altura/2)+(alturaCabeza/2));

        int yCuerpo = (int)(yCabeza+(alturaCabeza/2)+(alturaCuerpo/2)-4);

        spriteCuerpo.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCuerpo - Sala.scrollEjeY);
        spriteCabeza.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCabeza - Sala.scrollEjeY);
    }

}
