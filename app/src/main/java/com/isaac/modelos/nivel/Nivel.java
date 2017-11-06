package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.isaac.GameView;
import com.isaac.R;
import com.isaac.gestores.GestorXML;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.hud.IconoVida;
import com.isaac.modelos.item.Item;

import java.util.List;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Sala[][] salas;
    private Jugador jugador;

    private int salaActualX;
    private int salaActualY;

    private GameView gameView;
    private Sala salaActual;

    public List<Integer> itemPool;

    public boolean inicializado;

    public Nivel(Context context, int numeroNivel, GameView gameView) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        this.gameView = gameView;
        itemPool = GestorXML.getInstance().getItemPool(context);

        jugador = new Jugador(context,100,100);
        inicializar();

        inicializado = true;
    }

    public void inicializar()throws Exception {

        generateRooms();

        salaActual = salas[salaActualY][salaActualX];
        salaActual.enemigos.clear();
        salaActual.itemsDropped = true;
        salaActual.moveToRoom(null);
    }

    public IconoVida[] getActualHP(){
        IconoVida[] vida = new IconoVida[jugador.actualMaxHP/2];

        if(jugador.HP % 2 == 0){

            int i = 0;
            int j = 0;

            for(;i<jugador.HP;i=i+2) {
                vida[j] = new IconoVida(context, GameView.pantallaAncho * (0.04 * (j + 1)), GameView.pantallaAlto * 0.05, IconoVida.FULL_HP);
                j++;
            }

            for(;i<jugador.actualMaxHP;i=i+2) {
                vida[j] = new IconoVida(context, GameView.pantallaAncho * (0.04 * (j + 1)), GameView.pantallaAlto * 0.05, IconoVida.EMPTY_HP);
                j++;
            }
        }

        else{

            int objHP = jugador.HP-1;
            int i = 0;
            int j = 0;

            for(;i<objHP;i=i+2) {
                vida[j] = new IconoVida(context, GameView.pantallaAncho * (0.04 * (j + 1)), GameView.pantallaAlto * 0.05, IconoVida.FULL_HP);
                j++;
            }

            vida[j] = new IconoVida(context,GameView.pantallaAncho* (0.04*(j+1)),GameView.pantallaAlto*0.05, IconoVida.HALF_HP);
            i+=2;
            j++;

            for(;i<jugador.actualMaxHP;i=i+2) {
                vida[j] = new IconoVida(context, GameView.pantallaAncho * (0.04 * (j + 1)), GameView.pantallaAlto * 0.05, IconoVida.EMPTY_HP);
                j++;
            }
        }

        return vida;
    }

    public void actualizar (long tiempo) throws Exception {
        if (inicializado) {
            salaActual.actualizar(tiempo);
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

        salaActual = salas[salaActualY][salaActualX];
        salaActual.moveToRoom(puerta);
        gameView.forceUpdate();
    }

    public int getLlaves(){
        return jugador.getNumLlaves();
    }

    public int getBombas(){
        return jugador.getNumBombas();
    }

    public int getMonedas(){
        return jugador.getNumMonedas();
    }

    public void changeLevel() throws Exception {
        inicializar();
    }

    private void generateRooms() throws Exception {
        int largo = (int)(Math.random()*3)+3;
        int ancho = (int)(Math.random()*3)+3;

        int salaAmarillaI = (int)(Math.random()*largo);
        int salaAmarillaJ = (int)(Math.random()*ancho);

        int salaBossI;
        int salaBossJ;

        do{
            salaBossI = (int)(Math.random()*largo);
            salaBossJ = (int)(Math.random()*ancho);
        }while(salaBossI==salaAmarillaI && salaBossJ==salaAmarillaJ);

        salas = new Sala[largo][ancho];

        salas[salaAmarillaI][salaAmarillaJ] = new Sala_tesoro(context, Sala.SALA_DORADA_1, jugador, this);
        salas[salaBossI][salaBossJ] = new Sala_boss(context, Sala.SALA_BOSS_1, jugador, this);

        do {
            salaActualX = (int) (Math.random() * salas[0].length);
            salaActualY = (int) (Math.random() * salas.length);
        }while( (salaActualX==salaAmarillaI && salaActualY==salaAmarillaJ) || (salaActualX==salaBossI && salaActualY==salaBossJ));

        for(int i=0;i<salas.length;i++){
            for(int j=0;j<salas[i].length;j++){
                if(salas[i][j]==null)
                    salas[i][j] = new Sala(context, Sala.SALA_CUADRADA_1, jugador, this);

                if(i==0)
                    salas[i][j].puertas.remove(Sala.PUERTA_ARRIBA);

                if(j==0)
                    salas[i][j].puertas.remove(Sala.PUERTA_IZQUIERDA);

                if(i==largo-1)
                    salas[i][j].puertas.remove(Sala.PUERTA_ABAJO);

                if(j==ancho-1)
                    salas[i][j].puertas.remove(Sala.PUERTA_DERECHA);

            }
        }

        //Salas laterales de la amarilla
        if(salaAmarillaJ-1>=0){
            Puerta temp = salas[salaAmarillaI][salaAmarillaJ-1].puertas.get(Sala.PUERTA_DERECHA);
            temp.puertaAbierta = R.drawable.puerta_sala_dorada_derecha;
            temp.puertaCerrada = R.drawable.puerta_sala_dorada_derecha_cerrada;
        }

        if(salaAmarillaJ+1<=ancho-1){
            Puerta temp = salas[salaAmarillaI][salaAmarillaJ+1].puertas.get(Sala.PUERTA_IZQUIERDA);
            temp.puertaAbierta = R.drawable.puerta_sala_dorada_izquierda;
            temp.puertaCerrada = R.drawable.puerta_sala_dorada_izquierda_cerrada;
        }

        if(salaAmarillaI-1>=0){
            Puerta temp = salas[salaAmarillaI-1][salaAmarillaJ].puertas.get(Sala.PUERTA_ABAJO);
            temp.puertaAbierta = R.drawable.puerta_sala_dorada_abajo;
            temp.puertaCerrada = R.drawable.puerta_sala_dorada_abajo_cerrada;
        }

        if(salaAmarillaI+1<=largo-1){
            Puerta temp = salas[salaAmarillaI+1][salaAmarillaJ].puertas.get(Sala.PUERTA_ARRIBA);
            temp.puertaAbierta = R.drawable.puerta_sala_dorada_arriba;
            temp.puertaCerrada = R.drawable.puerta_sala_dorada_arriba_cerrada;
        }

        //Salas laterales boss
        if(salaBossJ-1>=0){
            Puerta temp = salas[salaBossI][salaBossJ-1].puertas.get(Sala.PUERTA_DERECHA);
            temp.puertaAbierta = R.drawable.puerta_sala_boss_derecha;
            temp.puertaCerrada = R.drawable.puerta_sala_boss_derecha_cerrada;
        }

        if(salaBossJ+1<=ancho-1){
            Puerta temp = salas[salaBossI][salaBossJ+1].puertas.get(Sala.PUERTA_IZQUIERDA);
            temp.puertaAbierta = R.drawable.puerta_sala_boss_izquierda;
            temp.puertaCerrada = R.drawable.puerta_sala_boss_izquierda_cerrada;
        }

        if(salaBossI-1>=0){
            Puerta temp = salas[salaBossI-1][salaBossJ].puertas.get(Sala.PUERTA_ABAJO);
            temp.puertaAbierta = R.drawable.puerta_sala_boss_abajo;
            temp.puertaCerrada = R.drawable.puerta_sala_boss_abajo_cerrada;
        }

        if(salaBossI+1<=largo-1){
            Puerta temp = salas[salaBossI+1][salaBossJ].puertas.get(Sala.PUERTA_ARRIBA);
            temp.puertaAbierta = R.drawable.puerta_sala_boss_arriba;
            temp.puertaCerrada = R.drawable.puerta_sala_boss_arriba_cerrada;
        }

    }

}

