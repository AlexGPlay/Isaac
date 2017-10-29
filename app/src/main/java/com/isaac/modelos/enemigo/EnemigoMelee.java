package com.isaac.modelos.enemigo;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.Sala;

import java.util.HashMap;

/**
 * Created by Pelayo on 27/10/2017.
 */

public class EnemigoMelee extends Modelo {

    public static final int MOVIMIENTO_DERECHA = 0;
    public static final int MOVIMIENTO_IZQUIERDA = 1;
    public static final int MOVIMIENTO_ABAJO = 2;
    public static final int MOVIMIENTO_ARRIBA = 3;
    public static final int PARADO = 4;

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

    private Sprite spriteCabeza;
    private Sprite spriteCuerpo;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    private double xInicial;
    private double yInicial;

    public double aceleracionX;
    public double aceleracionY;

    public EnemigoMelee(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, alturaCabeza+alturaCuerpo, Math.max(anchoCabeza,anchoCuerpo) );

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        aceleracionX = 0;
        aceleracionY = 0;

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

    }

    public void actualizar (long tiempo) {
        spriteCuerpo.actualizar(tiempo);
        spriteCabeza.actualizar(tiempo);
    }


    public void dibujar(Canvas canvas){
        int xCabeza = (int)(x-(ancho/2)+(anchoCabeza/2));
        int yCabeza = (int)(y-(altura/2)+(alturaCabeza/2));

        int yCuerpo = (int)(yCabeza+(alturaCabeza/2)+(alturaCuerpo/2)-4);

        spriteCuerpo.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCuerpo - Sala.scrollEjeY);
        spriteCabeza.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCabeza - Sala.scrollEjeY);
    }

    public void aplicarReglasDeMovimientoMelee(){

    }



}
