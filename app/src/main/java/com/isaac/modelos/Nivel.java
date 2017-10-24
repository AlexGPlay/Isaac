package com.isaac.modelos;

import android.content.Context;
import android.graphics.Canvas;

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
    public int orientacionPad = 0;


    private int salaActualX;
    private int salaActualY;

    public boolean inicializado;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar()throws Exception {

        salas = new Sala[3][3];

        for(int i=0;i<salas.length;i++)
            for(int j=0;j<salas[i].length;j++)
                salas[i][j] = new Sala(Sala.SALA_CUADRADA_1);


        salaActualX = (int)(Math.random()*salas.length);
        salaActualY = (int)(Math.random()*salas[0].length);

        jugador = new Jugador(context,100,100);

        salas[salaActualX][salaActualY].moveToRoom(jugador);
    }

    private void aplicarReglasMovimiento() throws Exception {
        jugador.procesarOrdenes(orientacionPad);
        if (jugador.aceleracionX > 0) {
            jugador.x+=jugador.aceleracionX;
        }
        if(jugador.aceleracionX<0){
            jugador.x+=jugador.aceleracionX;
        }
        if(jugador.aceleracionY>0){
            jugador.y+=jugador.aceleracionY;
        }
        if(jugador.aceleracionY<0){
            jugador.y+=jugador.aceleracionY;
        }
    }


    public void actualizar (long tiempo) throws Exception {
        if (inicializado) {
            salas[salaActualX][salaActualY].actualizar(tiempo);
            aplicarReglasMovimiento();
        }
    }


    public void dibujar (Canvas canvas) {
        if(inicializado) {
            salas[salaActualX][salaActualY].dibujar(canvas);
        }
    }

}

