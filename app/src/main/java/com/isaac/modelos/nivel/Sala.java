package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;

import com.isaac.GameView;
import com.isaac.gestores.CargadorSalas;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.enemigo.EnemigoMelee;
import com.isaac.modelos.item.Item;
import com.isaac.modelos.item.pickups.Bomba;
import com.isaac.modelos.item.pickups.Cofre;
import com.isaac.modelos.item.pickups.Llave;
import com.isaac.modelos.item.pickups.Moneda;
import com.isaac.modelos.item.pickups.PickupID;
import com.isaac.modelos.item.pickups.Vida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alex on 24/10/2017.
 */

public class Sala{

    public static final String SALA_CUADRADA_1 = "Sala_cuadrada_1";

    public static final String SALA_DORADA_1 = "Sala_dorada_test";
    public static final String SALA_BOSS_1 = "Sala_boss_test";

    public static final String PUERTA_ARRIBA = "arriba";
    public static final String PUERTA_ABAJO = "abajo";
    public static final String PUERTA_DERECHA = "derecha";
    public static final String PUERTA_IZQUIERDA = "izquierda";

    public static final int SALA_NORMAL = 1;
    public static final int SALA_DORADA = 2;
    public static final int SALA_BOSS = 3;

    protected boolean itemsDropped;

    protected Tile[][] mapaTiles;

    public static int orientacionPad;
    public static int orientacionDisparo;

    protected Jugador jugador;
    protected List <EnemigoMelee> enemigos;
    protected List<DisparoJugador> disparosJugador;
    protected List<Item> items;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    public Nivel nivel;
    protected Context context;

    protected HashMap<String,Puerta> puertas;

    public Sala(Context context, String tipoSala, Jugador jugador, Nivel nivel) throws Exception {
        this.nivel = nivel;

        puertas = new HashMap<>();
        mapaTiles = CargadorSalas.inicializarMapaTiles(tipoSala,this);

        this.disparosJugador = new ArrayList<>();
        this.items = new ArrayList<>();
        this.itemsDropped = false;
        this.jugador = jugador;
        this.context = context;

        enemigos = new ArrayList<>();
        enemigos.add(new EnemigoMelee(context,200,200));
        enemigos.add(new EnemigoMelee(context,150,150));

        this.orientacionPad = Jugador.PARADO;
        this.orientacionDisparo = Jugador.NO_DISPARO;
    }

    public void checkEstadoSala(){
        if(enemigos.size()>0){
            for(Puerta puerta : puertas.values()) {
                puerta.abierta = false;
                int x = puerta.getXEntrada();
                int y = puerta.getYEntrada();

                mapaTiles[x][y].tipoDeColision = Tile.SOLIDO;
            }
        }

        else if(enemigos.size()<=0){
            for(Puerta puerta : puertas.values()) {
                puerta.abierta = true;
                int x = puerta.getXEntrada();
                int y = puerta.getYEntrada();

                mapaTiles[x][y].tipoDeColision = Tile.PASABLE;
            }

            if(!itemsDropped)
                generatePickUps();
        }
    }

    public void generatePickUps(){
        int selectedPickUp = (int)(Math.random()* (PickupID.MAX_NUM+1));

        switch (selectedPickUp){
            case PickupID.BOMBA:
                items.add(new Bomba(context, (anchoMapaTiles()*Tile.ancho)/2, (altoMapaTiles()*Tile.altura)/2));
                break;

            case PickupID.LLAVE:
                items.add(new Llave(context, (anchoMapaTiles()*Tile.ancho)/2, (altoMapaTiles()*Tile.altura)/2));
                break;

            case PickupID.MONEDA:
                items.add(new Moneda(context, (anchoMapaTiles()*Tile.ancho)/2, (altoMapaTiles()*Tile.altura)/2));
                break;

            case PickupID.VIDA:
                items.add(new Vida(context, (anchoMapaTiles()*Tile.ancho)/2, (altoMapaTiles()*Tile.altura)/2));
                break;

            case PickupID.COFRE:
                items.add(new Cofre(context, (anchoMapaTiles()*Tile.ancho)/2, (altoMapaTiles()*Tile.altura)/2));
                break;
        }

        itemsDropped = true;
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
        checkEstadoSala();

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

        for(Item item : items)
            item.dibujar(canvas);

    }

    protected void aplicarReglasMovimiento() throws Exception {
        reglasMovimientoJugador();
        reglasMovimientoColisionPuerta();
        reglasDeMoVimientoEnemigos();
        reglasDeMovimientoDisparosJugador();
        reglasDeMovimientoColisionPickUps();
    }

    protected void reglasMovimientoColisionPuerta(){
        for(String key : puertas.keySet()) {
            if (jugador.colisiona(puertas.get(key)) && puertas.get(key).abierta) {
                disparosJugador.clear();
                nivel.moverSala(key);
            }
        }
    }

    protected void reglasMovimientoJugador(){

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

    protected void reglasDeMoVimientoEnemigos(){
        for(Iterator<EnemigoMelee> iterator = enemigos.iterator(); iterator.hasNext();){
            EnemigoMelee enemigo = iterator.next();

            if(enemigo.estado == EnemigoMelee.ESTADO_MUERTO) {
                iterator.remove();
                continue;
            }

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

    protected void reglasDeMovimientoDisparosJugador(){
        for(Iterator<DisparoJugador> iterator = disparosJugador.iterator(); iterator.hasNext();){
            DisparoJugador disparo = iterator.next();

            int virtualX = (int) (disparo.x + disparo.aceleracionX);
            int virtualY = (int) (disparo.y + disparo.aceleracionY);

            int tileXDisparo = (int) (virtualX / Tile.ancho);
            int tileYDisparo = (int) (virtualY / Tile.altura);

            if(disparo.estado == DisparoJugador.FINALIZADO){
                iterator.remove();
                continue;
            }

            if(disparo.isOutOfRange()){
                disparo.estado = DisparoJugador.FINALIZANDO;
                continue;
            }

            if(mapaTiles[tileXDisparo][tileYDisparo].tipoDeColision==Tile.SOLIDO) {
                disparo.estado = DisparoJugador.FINALIZANDO;
                continue;
            }

            for(EnemigoMelee enemigo : enemigos) {
                if (disparo.colisiona(enemigo) && disparo.estado == DisparoJugador.DISPARANDO) {
                    enemigo.HP -= disparo.damage;

                    if(enemigo.HP <= 0 )
                        enemigo.estado = EnemigoMelee.ESTADO_MUERTO;

                    disparo.estado = DisparoJugador.FINALIZANDO;
                    continue;
                }
            }

            for(Puerta puerta : puertas.values()) {
                if (puerta.colisiona(disparo)) {
                    disparo.estado = DisparoJugador.FINALIZANDO;
                    continue;
                }
            }

            if(disparo.estado == DisparoJugador.DISPARANDO) {
                disparo.x = virtualX;
                disparo.y = virtualY;
            }
        }
    }

    protected void reglasDeMovimientoColisionPickUps(){
        List<Item> newItems = new ArrayList<>();

        for(Iterator<Item> iterator = items.iterator(); iterator.hasNext();){
            Item item = iterator.next();

            if(jugador.colisiona(item)) {
                item.doStuff(jugador);

                if(item instanceof Cofre)
                    newItems.addAll(((Cofre)item).openChest(orientacionPad));

                iterator.remove();
                continue;
            }
        }

        items.addAll(newItems);
    }

    protected void dibujarTiles(Canvas canvas){
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

    protected float tilesEnDistanciaX(double distanciaX){
        return (float) distanciaX/Tile.ancho;
    }

    protected float tilesEnDistanciaY(double distanciaY) { return (float) distanciaY/Tile.altura; }

    public int anchoMapaTiles(){
        return mapaTiles.length;
    }

    public int altoMapaTiles(){
        return mapaTiles[0].length;
    }

    public int getTipoSala(){
        return Sala.SALA_NORMAL;
    }

}
