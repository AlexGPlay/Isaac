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

public class TheLamb extends EnemigoBase{

    //VARIABLES GRAFICAS
    protected static final String MOVIMIENTO_NORMAL = "movimiento";
    protected static final String APARICION = "aparicion";
    protected static final String DISPARAR = "disparar";

    private final static int altura = 75;
    private final static int ancho = 112;

    //ESTADOS PROPIOS DE THE LAMB
    public final static int ESTADO_APARECIENDO = 2;
    public final static int ESTADO_NORMAL = 3;
    public final static int ESTADO_DISPARANDO = 4;
    public final static int ESTADO_DISPARADO = 5;
    public final static int ESTADO_SEPARANDO = 6;

    //VARIABLES DISPARO
    private long tearDelay;
    private long tearRange;
    private int tearDamage;
    private int actualDelay;

    private boolean hasShot;
    private boolean preparingShot;
    private boolean summoned;

    public TheLamb(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, altura, ancho, Modelo.SOLIDO);

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        aceleracionX = 1;
        aceleracionY = 1;

        tearDelay = 2000;
        tearRange = 5000;
        actualDelay = 0;
        tearDamage = 1;

        HP = 300;

        estado = ESTADO_APARECIENDO;
        hasShot = false;
        preparingShot = false;
        summoned = false;

        inicializar();
    }

    public void inicializar(){
        Sprite aparicion = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.the_lamb_aparicion),
                ancho, altura,
                4, 4, false);
        sprites.put(APARICION, aparicion);

        Sprite movimiento = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.the_lamb_movimiento),
                ancho, altura,
                4, 4, true);
        sprites.put(MOVIMIENTO_NORMAL, movimiento);

        Sprite disparando = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.the_lamb_disparar),
                ancho, altura,
                5, 5, false);
        sprites.put(DISPARAR, disparando);

        spriteCuerpo = sprites.get(APARICION);
    }

    @Override
    public void actualizar(long tiempo) {
        boolean finalizado = spriteCuerpo.actualizar(tiempo);
        boolean tShot = timeToShot();
        actualDelay += tiempo;

        //Ya aparecio
        if(finalizado && estado==ESTADO_APARECIENDO){
            estado = ESTADO_NORMAL;
            spriteCuerpo.setFrameActual(0);
            spriteCuerpo = sprites.get(MOVIMIENTO_NORMAL);
        }

        //Salen los disparos
        else if(finalizado && estado==ESTADO_DISPARANDO){
            estado = ESTADO_DISPARADO;
            hasShot = true;
        }

        else if(tShot && !preparingShot && estado==ESTADO_NORMAL){
            estado = ESTADO_DISPARANDO;
        }

        else if(estado==ESTADO_DISPARANDO && !preparingShot){
            spriteCuerpo.setFrameActual(0);
            spriteCuerpo = sprites.get(DISPARAR);
            preparingShot = true;
        }

        //Vuelve a la normalidad despues de disparar
        else if(estado==ESTADO_DISPARADO){
            estado = ESTADO_NORMAL;
            spriteCuerpo.setFrameActual(0);
            spriteCuerpo = sprites.get(MOVIMIENTO_NORMAL);
            preparingShot = false;
        }

        if(getHP()<=0 && !summoned)
            estado = ESTADO_SEPARANDO;

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

    public boolean timeToShot(){
        if (actualDelay> tearDelay + Math.random()* tearDelay)
            return true;

        return false;
    }

    @Override
    public void dibujar(Canvas canvas) {
        spriteCuerpo.dibujarSprite(canvas,(int)x- Sala.scrollEjeX,(int)y - Sala.scrollEjeY);
    }

    @Override
    public ArrayList<EnemigoBase> summon(){
        ArrayList<EnemigoBase> toAdd = new ArrayList<>();

        if(estado==ESTADO_SEPARANDO) {
            toAdd.add(new TheLambBody(context, x, y));
            toAdd.add(new TheLambHead(context, x, y));
            summoned = true;

            estado = ESTADO_MUERTO;
        }

        return toAdd;
    }

}
