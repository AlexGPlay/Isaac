package com.isaac.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.nivel.Sala;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 24/10/2017.
 */

public class Jugador extends Modelo{
    public static final int MOVIMIENTO_DERECHA = 0;
    public static final int MOVIMIENTO_IZQUIERDA = 1;
    public static final int MOVIMIENTO_ABAJO = 2;
    public static final int MOVIMIENTO_ARRIBA = 3;
    public static final int MOVIMIENTO_ARRIBA_DERECHA = 4;
    public static final int MOVIMIENTO_ARRIBA_IZQUIERDA = 5;
    public static final int MOVIMIENTO_ABAJO_DERECHA = 6;
    public static final int MOVIMIENTO_ABAJO_IZQUIERDA = 7;
    public static final int PARADO = 8;

    public static final int DISPARO_DERECHA = 0;
    public static final int DISPARO_IZQUIERDA = 1;
    public static final int DISPARO_ABAJO = 2;
    public static final int DISPARO_ARRIBA = 3;
    public static final int NO_DISPARO = 4;

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

    public double aceleracionX;
    public double aceleracionY;

    public long tearDelay;
    public long tearRange;
    public double tearDamage;
    public int HP;
    public int maxHP;
    public int actualMaxHP;

    public int actualDelay;

    private int numBombas;
    private int numLlaves;
    private int numMonedas;

    public Jugador(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, alturaCabeza+alturaCuerpo, Math.max(anchoCabeza,anchoCuerpo) );

        this.x =  xInicial;
        this.y =  yInicial - altura/2;

        aceleracionX = 0;
        aceleracionY = 0;

        tearDelay = 400;
        tearRange = 1000;
        actualDelay = 0;
        tearDamage = 3.5;
        HP = 6;
        actualMaxHP = 6;
        maxHP = 20;

        inicializar();
    }

    public void inicializar (){

        Sprite cabezaDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cabeza_derecha),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_DERECHA, cabezaDerecha);

        Sprite cabezaIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cabeza_izquierda),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_IZQUIERDA, cabezaIzquierda);

        Sprite cabezaAtras = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cabeza_atras),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_ATRAS, cabezaAtras);

        Sprite cabezaAdelante = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cabeza_defrente),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_ADELANTE, cabezaAdelante);

        Sprite cuerpoDerechaDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_andar_derecha),
                anchoCuerpo, alturaCuerpo,
                10, 10, true);
        sprites.put(MOVER_DERECHA, cuerpoDerechaDerecha);

        Sprite cuerpoDerechaIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_andar_izquierda),
                anchoCuerpo, alturaCuerpo,
                10, 10, true);
        sprites.put(MOVER_IZQUIERDA, cuerpoDerechaIzquierda);

        Sprite cuerpoAdelanteAtras = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_andar_adelante_atras),
                anchoCuerpo, alturaCuerpo,
                10, 10, true);
        sprites.put(MOVER_ADELANTE_ATRAS, cuerpoAdelanteAtras);

        Sprite cuerpoParado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cuerpo_parado),
                anchoCuerpo, alturaCuerpo,
                1, 1, true);
        sprites.put(PARADO_SPRITE, cuerpoParado);


        spriteCabeza = sprites.get(CABEZA_ADELANTE);
        spriteCuerpo = sprites.get(PARADO_SPRITE);

    }

    public void actualizar (long tiempo) {
        spriteCuerpo.actualizar(tiempo);
        spriteCabeza.actualizar(tiempo);

        this.actualDelay += tiempo;
    }

    public List<DisparoJugador> procesarDisparos (int orientacionPad){
        ArrayList<DisparoJugador> disparos = new ArrayList<>();

        if(orientacionPad!=Jugador.NO_DISPARO && actualDelay>=tearDelay) {
            disparos.add(new DisparoJugador(context, this.x, this.y, tearRange, tearDamage, orientacionPad));
            actualDelay = 0;
        }

        return disparos;
    }

    public void procesarOrdenes (int orientacionPad) {

        if (orientacionPad == MOVIMIENTO_DERECHA) {
            aceleracionY = 0;
            aceleracionX = 5;

            spriteCabeza = sprites.get(CABEZA_DERECHA);
            spriteCuerpo = sprites.get(MOVER_DERECHA);
        }
        else if (orientacionPad == MOVIMIENTO_ABAJO){
            aceleracionX = 0;
            aceleracionY = 5;

            spriteCabeza = sprites.get(CABEZA_ADELANTE);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == MOVIMIENTO_IZQUIERDA){
            aceleracionY  =0;
            aceleracionX= -5;

            spriteCabeza = sprites.get(CABEZA_IZQUIERDA);
            spriteCuerpo = sprites.get(MOVER_IZQUIERDA);
        }
        else if(orientacionPad == MOVIMIENTO_ARRIBA) {
            aceleracionX = 0;
            aceleracionY = -5;

            spriteCabeza = sprites.get(CABEZA_ATRAS);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == MOVIMIENTO_ARRIBA_DERECHA){
            aceleracionX = 5;
            aceleracionY = -5;

            spriteCabeza = sprites.get(CABEZA_ATRAS);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == MOVIMIENTO_ARRIBA_IZQUIERDA){
            aceleracionX = -5;
            aceleracionY = -5;

            spriteCabeza = sprites.get(CABEZA_ATRAS);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == MOVIMIENTO_ABAJO_DERECHA){
            aceleracionX = 5;
            aceleracionY = 5;

            spriteCabeza = sprites.get(CABEZA_ADELANTE);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == MOVIMIENTO_ABAJO_IZQUIERDA){
            aceleracionX = -5;
            aceleracionY = 5;

            spriteCabeza = sprites.get(CABEZA_ADELANTE);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == PARADO) {
            frenar();

            spriteCabeza = sprites.get(CABEZA_ADELANTE);
            spriteCuerpo = sprites.get(PARADO_SPRITE);
        }

    }

    public void dibujar(Canvas canvas){
        int xCabeza = (int)(x-(ancho/2)+(anchoCabeza/2));
        int yCabeza = (int)(y-(altura/2)+(alturaCabeza/2));

        int yCuerpo = (int)(yCabeza+(alturaCabeza/2)+(alturaCuerpo/2)-4);

        spriteCuerpo.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCuerpo - Sala.scrollEjeY);
        spriteCabeza.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCabeza - Sala.scrollEjeY);
    }

    private void frenar(){
        aceleracionX=0;
        aceleracionY=0;
    }

    public int getNumBombas() {
        return numBombas;
    }

    public void setNumBombas(int numBombas) {
        if(numBombas>=0 && numBombas<=99)
            this.numBombas = numBombas;
    }

    public int getNumLlaves() {
        return numLlaves;
    }

    public void setNumLlaves(int numLlaves) {
        if(numLlaves>=0 && numLlaves<=99)
            this.numLlaves = numLlaves;
    }

    public int getNumMonedas() {
        return numMonedas;
    }

    public void setNumMonedas(int numMonedas) {
        if(numMonedas>=0 && numMonedas<=99)
            this.numMonedas = numMonedas;
    }

}
