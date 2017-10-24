package com.isaac.modelos;

import android.graphics.Canvas;

import com.isaac.GameView;
import com.isaac.gestores.CargadorSalas;
import com.isaac.gestores.Utilidades;

/**
 * Created by Alex on 24/10/2017.
 */

public class Sala{

    public static final String SALA_CUADRADA_1 = "Sala_cuadrada_1";

    private Tile[][] mapaTiles;
    private Jugador jugador;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    public Sala(String tipoSala) throws Exception {
        mapaTiles = CargadorSalas.inicializarMapaTiles(tipoSala);
        scrollEjeX = 0;
        scrollEjeY = 0;
    }

    public void moveToRoom(Jugador jugador){
        this.jugador = jugador;
    }

    public void actualizar(long time) throws Exception {
        jugador.actualizar(time);
        aplicarReglasMovimiento();
    }

    public void dibujar(Canvas canvas){
        dibujarTiles(canvas);
        jugador.dibujar(canvas);
    }

    private void aplicarReglasMovimiento() throws Exception {
        reglasMovimientoJugador();
    }

    private void reglasMovimientoJugador(){
        int tileXJugadorIzquierda = (int) (jugador.x - (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha = (int) (jugador.x + (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileYJugadorInferior = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro = (int) jugador.y / Tile.altura;
        int tileYJugadorSuperior = (int) (jugador.y - (jugador.altura / 2 - 1)) / Tile.altura;

        if(jugador.aceleracionX>0) {
            Tile derecha_superior = mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior];
            Tile derecha_enfrente = mapaTiles[tileXJugadorDerecha][tileYJugadorCentro];
            Tile derecha_inferior = mapaTiles[tileXJugadorDerecha][tileYJugadorInferior];

            if (derecha_superior.tipoDeColision == Tile.PASABLE && derecha_enfrente.tipoDeColision == Tile.PASABLE && derecha_inferior.tipoDeColision == Tile.PASABLE)
                jugador.x += jugador.aceleracionX;

            else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    derecha_inferior.tipoDeColision == Tile.PASABLE &&
                    derecha_enfrente.tipoDeColision == Tile.PASABLE &&
                    derecha_superior.tipoDeColision == Tile.PASABLE) {

                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (jugador.x + jugador.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, jugador.aceleracionX);
                    jugador.x += velocidadNecesaria;
                }
                else {
                    jugador.x = TileJugadorBordeDerecho - jugador.ancho / 2;
                }

            }

        }

        else if(jugador.aceleracionX<0) {
            Tile izquierda_superior = mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior];
            Tile izquierda_enfrente = mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro];
            Tile izquierda_inferior = mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior];

            if (izquierda_superior.tipoDeColision == Tile.PASABLE && izquierda_enfrente.tipoDeColision == Tile.PASABLE && izquierda_inferior.tipoDeColision == Tile.PASABLE)
                jugador.x += jugador.aceleracionX;

            else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    izquierda_inferior.tipoDeColision == Tile.PASABLE &&
                    izquierda_enfrente.tipoDeColision == Tile.PASABLE &&
                    izquierda_superior.tipoDeColision == Tile.PASABLE) {

                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (jugador.x - jugador.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, jugador.aceleracionX);
                    jugador.x += velocidadNecesaria;
                }
                else {
                    jugador.x = TileJugadorBordeIzquierdo + jugador.ancho / 2;
                }


            }
        }

        if(jugador.aceleracionY<0){
            Tile superior_izquierda = mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior];
            Tile superior_centro = mapaTiles[tileXJugadorIzquierda+1][tileYJugadorSuperior];
            Tile superior_derehca = mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior];

            if (superior_izquierda.tipoDeColision == Tile.PASABLE && superior_centro.tipoDeColision == Tile.PASABLE && superior_derehca.tipoDeColision == Tile.PASABLE)
                jugador.y += jugador.aceleracionY;

            else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    superior_izquierda.tipoDeColision == Tile.PASABLE &&
                    superior_centro.tipoDeColision == Tile.PASABLE &&
                    superior_derehca.tipoDeColision == Tile.PASABLE) {

                int TileJugadorBordeIzquierdo = tileYJugadorSuperior * Tile.altura;
                double distanciaY = (jugador.y - jugador.altura / 2) - TileJugadorBordeIzquierdo;

                if (distanciaY > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaY, jugador.aceleracionX);
                    jugador.y += velocidadNecesaria;
                }
                else {
                    jugador.y = TileJugadorBordeIzquierdo + jugador.ancho / 2;
                }
            }
        }

        else if(jugador.aceleracionY>0) {
            Tile inferior_derecha = mapaTiles[tileXJugadorDerecha][tileYJugadorInferior];
            Tile inferior_centro = mapaTiles[tileXJugadorDerecha-1][tileYJugadorInferior];
            Tile inferior_izquierda = mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior];

            if (inferior_derecha.tipoDeColision == Tile.PASABLE && inferior_centro.tipoDeColision == Tile.PASABLE && inferior_izquierda.tipoDeColision == Tile.PASABLE)
                jugador.y += jugador.aceleracionY;

            else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    inferior_derecha.tipoDeColision == Tile.PASABLE &&
                    inferior_centro.tipoDeColision == Tile.PASABLE &&
                    inferior_izquierda.tipoDeColision == Tile.PASABLE) {

                int TileJugadorBordeDerecho = tileYJugadorInferior * Tile.altura + Tile.altura;
                double distanciaY = TileJugadorBordeDerecho - (jugador.x + jugador.altura / 2);

                if (distanciaY > 0) {
                    double velocidadNecesaria = Math.min(distanciaY, jugador.aceleracionX);
                    jugador.y += velocidadNecesaria;
                }
                else {
                    jugador.y = TileJugadorBordeDerecho - jugador.altura / 2;
                }

            }

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
