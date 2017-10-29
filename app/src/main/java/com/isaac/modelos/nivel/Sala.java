package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.GameView;
import com.isaac.gestores.CargadorSalas;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.enemigo.EnemigoMelee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alex on 24/10/2017.
 */

public class Sala{

    public static final String SALA_CUADRADA_1 = "Sala_cuadrada_1";
    public static final String SALA_CUADRADA_2 = "Sala_cuadrada_2";
    public static final String SALA_CUADRADA_3 = "Sala_cuadrada_3";
    public static final String SALA_CUADRADA_4 = "Sala_cuadrada_4";
    public static final String SALA_CUADRADA_5 = "Sala_cuadrada_5";
    public static final String SALA_CUADRADA_6 = "Sala_cuadrada_6";
    public static final String SALA_CUADRADA_7 = "Sala_cuadrada_7";
    public static final String SALA_CUADRADA_8 = "Sala_cuadrada_8";
    public static final String SALA_CUADRADA_9 = "Sala_cuadrada_9";

    public static final String SALA_DORADA_1 = "Sala_dorada_test";

    public static final String PUERTA_ARRIBA = "arriba";
    public static final String PUERTA_ABAJO = "abajo";
    public static final String PUERTA_DERECHA = "derecha";
    public static final String PUERTA_IZQUIERDA = "izquierda";

    public static final int SALA_NORMAL = 1;
    public static final int SALA_BOSS = 2;
    public static final int SALA_TESORO = 3;

    public int tipoSala;

    protected Tile[][] mapaTiles;

    public static int orientacionPad;
    public static int orientacionDisparo;

    protected Jugador jugador;
    protected List <EnemigoMelee> enemigos;
    protected List<DisparoJugador> disparosJugador;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    protected Nivel nivel;
    protected Context context;

    protected HashMap<String,Puerta> puertas;

    public Sala(Context context, String tipoSala, Jugador jugador, Nivel nivel) throws Exception {
        puertas = new HashMap<>();
        mapaTiles = CargadorSalas.inicializarMapaTiles(tipoSala,this);

        this.disparosJugador = new ArrayList<>();
        this.jugador = jugador;
        this.context = context;

        enemigos= new ArrayList<EnemigoMelee>();
        enemigos.add(new EnemigoMelee(context,200,200));
        enemigos.add(new EnemigoMelee(context,150,150));

        this.nivel = nivel;
        this.orientacionPad = Jugador.PARADO;
        this.orientacionDisparo = Jugador.NO_DISPARO;
    }

    public void moveToRoom(String puerta){
        String contraria = getOppositeDoor(puerta);
        Puerta entrada = puertas.get(contraria);

        if(entrada!=null) {
            if(contraria == PUERTA_ABAJO){
                jugador.x = entrada.getXSalida() * Tile.ancho;
                jugador.y = entrada.getYSalida() * Tile.altura - Tile.altura;
            }

            else if(contraria == PUERTA_ARRIBA){
                jugador.x = entrada.getXSalida() * Tile.ancho;
                jugador.y = entrada.getYSalida() * Tile.altura + Tile.altura;
            }

            else if(contraria == PUERTA_DERECHA){
                jugador.x = entrada.getXSalida() * Tile.ancho - Tile.ancho;
                jugador.y = entrada.getYSalida() * Tile.altura;
            }

            else if(contraria == PUERTA_IZQUIERDA){
                jugador.x = entrada.getXSalida() * Tile.ancho + Tile.ancho;
                jugador.y = entrada.getYSalida() * Tile.altura;
            }

            scrollEjeX = 0;
            scrollEjeY = 0;
        }

        else {
            jugador.x = 100;
            jugador.y = 100;
        }

    }

    private String getOppositeDoor(String puerta){
        if(puerta == PUERTA_ARRIBA)
            return PUERTA_ABAJO;

        else if(puerta == PUERTA_ABAJO)
            return PUERTA_ARRIBA;

        else if(puerta == PUERTA_DERECHA)
            return PUERTA_IZQUIERDA;

        else if(puerta == PUERTA_IZQUIERDA)
            return PUERTA_DERECHA;

        return null;
    }

    public void addPuerta(String zona, Puerta puerta){
        puertas.put(zona,puerta);
    }


    public void actualizar(long time) throws Exception {
        jugador.procesarOrdenes(orientacionPad);
        disparosJugador.addAll( jugador.procesarDisparos(orientacionDisparo) );
        jugador.actualizar(time);

        for(EnemigoMelee enemigo : enemigos)
            enemigo.actualizar(time);

        for(DisparoJugador disparo : disparosJugador)
            disparo.actualizar(time);

        aplicarReglasMovimiento();
    }

    public void dibujar(Canvas canvas){
        dibujarTiles(canvas);

        for( Puerta puerta : puertas.values() )
            puerta.dibujar(canvas);

        jugador.dibujar(canvas);

        for(EnemigoMelee enemigo : enemigos)
            enemigo.dibujar(canvas);

        for(DisparoJugador disparo : disparosJugador)
            disparo.dibujar(canvas);

    }

    protected void aplicarReglasMovimiento() throws Exception {
        reglasMovimientoJugador();
        reglasMovimientoColisionPuerta();
        reglasDeMoVimientoEnemigos();
        reglasDeMovimientoDisparosJugador();
    }

    private void reglasMovimientoColisionPuerta(){
        for(String key : puertas.keySet()) {
            if (jugador.colisiona(puertas.get(key))) {
                disparosJugador.clear();
                nivel.moverSala(key);
            }
        }
    }

    private void reglasMovimientoJugador(){

        int virtualX = (int) (jugador.x + jugador.aceleracionX);
        int virtualY = (int) (jugador.y + jugador.aceleracionY);

        int tileXJugador = (int) (virtualX / Tile.ancho);
        int tileYJugador = (int) (virtualY / Tile.altura);

        if(jugador.aceleracionX<0){
            tileXJugador = (int) ( (virtualX-jugador.ancho/2) / Tile.ancho);
        }

        if(jugador.aceleracionX>0){
            tileXJugador = (int) ( (virtualX+jugador.ancho/2) / Tile.ancho);
        }

        if(jugador.aceleracionY>0){
            tileYJugador = (int) ( (virtualY+jugador.altura/2) / Tile.altura);
        }

        if( mapaTiles[tileXJugador][tileYJugador].tipoDeColision == Tile.PASABLE ){
            jugador.x = virtualX;
            jugador.y = virtualY;
        }

    }

    private void reglasDeMoVimientoEnemigos(){
        for(EnemigoMelee enemigo: enemigos){
            if((jugador.x - jugador.ancho / 2 <= (enemigo.x + enemigo.ancho / 2)
                    && (jugador.x + jugador.ancho / 2) >= (enemigo.x - enemigo.ancho / 2))){
                enemigo.aceleracionX=0;
            }
            else if(jugador.x>enemigo.x) {
                enemigo.aceleracionX=2;
                enemigo.x+=enemigo.aceleracionX;
            }
            else if(enemigo.x<jugador.x){
                enemigo.aceleracionX=-2;
                enemigo.x+=enemigo.aceleracionX;
            }

        }

    }

    private void reglasDeMovimientoDisparosJugador(){
        for(Iterator<DisparoJugador> iterator = disparosJugador.iterator(); iterator.hasNext();){
            DisparoJugador disparo = iterator.next();

            int virtualX = (int) (disparo.x + disparo.aceleracionX);
            int virtualY = (int) (disparo.y + disparo.aceleracionY);

            int tileXDisparo = (int) (virtualX / Tile.ancho);
            int tileYDisparo = (int) (virtualY / Tile.altura);

            if(disparo.isOutOfRange()){
                iterator.remove();
                continue;
            }

            if(mapaTiles[tileXDisparo][tileYDisparo].tipoDeColision==Tile.SOLIDO) {
                iterator.remove();
                continue;
            }

            for(Puerta puerta : puertas.values()) {
                if (puerta.colisiona(disparo)) {
                    iterator.remove();
                    continue;
                }
            }

            disparo.x = virtualX;
            disparo.y = virtualY;
        }
    }

    private void dibujarTiles(Canvas canvas){
        // Calcular que tiles serán visibles en la pantalla
        // La matriz de tiles es más grande que la pantalla
        int tileXJugador = (int) jugador.x / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.x - scrollEjeX));
        izquierda = Math.max(0,izquierda); // Que nunca sea < 0, ej -1

        if ( jugador .x  < anchoMapaTiles()* Tile.ancho - GameView.pantallaAncho*0.3 )
            if( jugador .x - scrollEjeX > GameView.pantallaAncho * 0.7 ){
                scrollEjeX = (int) ((jugador .x ) - GameView.pantallaAncho* 0.7);
            }


        if ( jugador .x  > GameView.pantallaAncho*0.3 )
            if( jugador .x - scrollEjeX < GameView.pantallaAncho *0.3 ){
                scrollEjeX = (int)(jugador .x - GameView.pantallaAncho*0.3);
            }


        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        // el ultimo tile visible, no puede superar el tamaño del mapa
        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        int tileYJugador = (int) jugador.y / Tile.altura;
        int alto = (int) (tileYJugador - tilesEnDistanciaY(jugador.y - scrollEjeY));
        alto = Math.max(0,alto);

        if(jugador.y < altoMapaTiles()*Tile.altura - GameView.pantallaAlto*0.3){
            if(jugador.y -scrollEjeY > GameView.pantallaAlto*0.7){
                scrollEjeY = (int) (jugador.y - GameView.pantallaAlto*0.7);
            }
        }

        if(jugador.y > GameView.pantallaAlto*0.3){
            if(jugador.y-scrollEjeY < GameView.pantallaAlto*0.3){
                scrollEjeY = (int)(jugador.y - GameView.pantallaAlto*0.3);
            }
        }

        int abajo = alto + GameView.pantallaAlto / Tile.altura + 1;
        abajo = Math.min(abajo, altoMapaTiles()-1);

        for (int y = 0; y < altoMapaTiles() ; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo
                    mapaTiles[x][y].imagen.setBounds(
                            (x  * Tile.ancho) - scrollEjeX,
                            y * Tile.altura - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            y * Tile.altura + Tile.altura - scrollEjeY);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }

    private float tilesEnDistanciaX(double distanciaX){
        return (float) distanciaX/Tile.ancho;
    }

    private float tilesEnDistanciaY(double distanciaY) { return (float) distanciaY/Tile.altura; }

    public int anchoMapaTiles(){
        return mapaTiles.length;
    }

    public int altoMapaTiles(){
        return mapaTiles[0].length;
    }

}
