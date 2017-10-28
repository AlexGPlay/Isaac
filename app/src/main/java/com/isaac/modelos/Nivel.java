package com.isaac.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.isaac.GameView;
import com.isaac.R;
import com.isaac.controles.Pad;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.gestores.CargadorSalas;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Sala[][] salas;
    private Jugador jugador;
    public int orientacionPad;

    private int salaActualX;
    private int salaActualY;

    private GameView gameView;
    private Sala salaActual;

    public boolean inicializado;

    public Nivel(Context context, int numeroNivel, GameView gameView) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        this.gameView = gameView;
        inicializar();

        inicializado = true;
    }

    public void inicializar()throws Exception {

        salas = new Sala[3][3];
        jugador = new Jugador(context,100,100);

        salas[0][0] = new Sala(Sala.SALA_CUADRADA_6, jugador, this);
        salas[0][1] = new Sala(Sala.SALA_CUADRADA_4, jugador, this);
        salas[0][2] = new Sala(Sala.SALA_CUADRADA_7, jugador,this);
        salas[1][0] = new Sala(Sala.SALA_CUADRADA_3, jugador,this);
        salas[1][1] = new Sala(Sala.SALA_CUADRADA_1, jugador,this);
        salas[1][2] = new Sala(Sala.SALA_CUADRADA_2, jugador,this);
        salas[2][0] = new Sala(Sala.SALA_CUADRADA_8, jugador,this);
        salas[2][1] = new Sala(Sala.SALA_CUADRADA_5, jugador,this);
        salas[2][2] = new Sala(Sala.SALA_CUADRADA_9, jugador,this);

        salaActualX = (int)(Math.random()*salas[0].length);
        salaActualY = (int)(Math.random()*salas.length);

        orientacionPad = Jugador.PARADO;

        salaActual = salas[salaActualY][salaActualX];
        salaActual.moveToRoom(null);
    }

    public void actualizar (long tiempo) throws Exception {
        if (inicializado) {

            salaActual.actualizar(tiempo);
            jugador.procesarOrdenes(orientacionPad);
            jugador.actualizar(tiempo);

        }
    }


    public void dibujar (Canvas canvas) {
        if(inicializado) {
            salaActual.dibujar(canvas);
        }
    }

    public void moverSala(String puerta){
        if(puerta==Sala.PUERTA_ARRIBA)
            salaActualY--;

        else if(puerta==Sala.PUERTA_ABAJO)
            salaActualY++;

        else if(puerta==Sala.PUERTA_DERECHA)
            salaActualX++;

        else if(puerta==Sala.PUERTA_IZQUIERDA)
            salaActualX--;

        Log.v("Movimiento sala", salaActualY + "||" + salaActualX);
        salaActual = salas[salaActualY][salaActualX];
        salaActual.moveToRoom(puerta);
        gameView.forceUpdate();
    }

}

