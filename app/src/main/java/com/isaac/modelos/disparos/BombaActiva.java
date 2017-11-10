package com.isaac.modelos.disparos;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.nivel.Sala;

import java.util.HashMap;

/**
 * Created by Alejandro García Parrondo on 09/11/2017.
 */

public class BombaActiva extends Modelo {

    private int ticksToExplode = 1500;
    private int actualTicks = 0;

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    public String estado;

    public int radio = 20;

    public final static String CUENTA_ATRAS = "cuenta_Atras";
    public final static String EXPLOTANDO = "explotando";
    public final static String EXPLOTADA = "explotada";

    public BombaActiva(Context context, double x, double y) {
        super(context, x, y, 21, 22, Modelo.MOVIBLE);

        inicializar();
        estado = CUENTA_ATRAS;
    }

    private void inicializar(){
        Sprite cuenta_atras = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.bomba_sprite),
                22, 21,
                4, 4, true);

        sprites.put(CUENTA_ATRAS, cuenta_atras);

        Sprite explosion = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.explosion_sprite),
                96, 96,
                30, 12, false);

        sprites.put(EXPLOTANDO, explosion);

        sprite = cuenta_atras;
    }

    public void actualizar(long tiempo){
        boolean finSprite = sprite.actualizar(tiempo);

        actualTicks += tiempo;

        if(actualTicks >= ticksToExplode && estado == CUENTA_ATRAS){
            estado = EXPLOTANDO;
            sprite = sprites.get(EXPLOTANDO);

            this.altura = 96;
            this.ancho = 96;

            sprite.setFrameActual(0);
        }

        if(estado == EXPLOTANDO && finSprite){
            estado = EXPLOTADA;
        }

    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int)x - Sala.scrollEjeX,(int) y - Sala.scrollEjeY);
    }

}