package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.isaac.GameView;
import com.isaac.gestores.CargadorSalas;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.disparos.BombaActiva;
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
import java.util.Random;

/**
 * Created by Alex on 24/10/2017.
 */

public class Sala{

    public static final String SALA_CUADRADA_1 = "Sala_cuadrada_1";
    public static final String SALA_CUADRADA_2 = "Sala_cuadrada_2";
    public static final String SALA_CUADRADA_3 = "Sala_cuadrada_3";
    public static final String SALA_CUADRADA_4 = "Sala_cuadrada_4";

    private static final int NUMBER_OF_LAYOUTS = 4;

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
    public static boolean bombaActiva;

    protected Jugador jugador;
    protected List <EnemigoMelee> enemigos;
    protected List<DisparoJugador> disparosJugador;
    protected List<BombaActiva> bombas;
    protected List<Item> items;
    protected List<Roca> rocas;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    public Nivel nivel;
    protected Context context;

    protected HashMap<String,Puerta> puertas;

    public Sala(Context context, String tipoSala, Jugador jugador, Nivel nivel) throws Exception {
        this.nivel = nivel;

        puertas = new HashMap<>();
        mapaTiles = CargadorSalas.inicializarMapaTiles(tipoSala,this, context);

        this.disparosJugador = new ArrayList<>();
        this.items = new ArrayList<>();
        this.itemsDropped = false;
        this.jugador = jugador;
        this.context = context;

        enemigos = new ArrayList<>();
        enemigos.add(new EnemigoMelee(context,200,200));
        enemigos.add(new EnemigoMelee(context,150,150));

        bombas = new ArrayList<>();

        if(rocas == null)
            rocas = new ArrayList<>();

        this.orientacionPad = Jugador.PARADO;
        this.orientacionDisparo = Jugador.NO_DISPARO;
        this.bombaActiva = false;
    }

    public void checkEstadoSala(){
        if(enemigos.size()>0){
            for(Puerta puerta : puertas.values()) {
                puerta.abierta = false;
                int x = puerta.getXEntrada();
                int y = puerta.getYEntrada();

                mapaTiles[x][y].tipoDeColision = Tile.SOLIDO;

                if(puerta.forzada){
                    puerta.abierta = true;
                    mapaTiles[x][y].tipoDeColision = Tile.PASABLE;
                }

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
                jugador.x = entrada.getXSalida() * Tile.ancho + Tile.ancho/2;
                jugador.y = entrada.getYSalida() * Tile.altura - Tile.altura/2;
            }

            else if(contraria == PUERTA_ARRIBA){
                jugador.x = entrada.getXSalida() * Tile.ancho + Tile.ancho/2;
                jugador.y = entrada.getYSalida() * Tile.altura + Tile.altura;
            }

            else if(contraria == PUERTA_DERECHA){
                jugador.x = entrada.getXSalida() * Tile.ancho + Tile.ancho/2;
                jugador.y = entrada.getYSalida() * Tile.altura + Tile.altura/2;
            }

            else if(contraria == PUERTA_IZQUIERDA){
                jugador.x = entrada.getXSalida() * Tile.ancho + Tile.ancho/2;
                jugador.y = entrada.getYSalida() * Tile.altura + Tile.altura/2;
            }

            scrollEjeX = 0;
            scrollEjeY = 0;

            for(Puerta temp : puertas.values()) {
                temp.abierta = false;
                temp.forzada = false;
            }

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

    public void addRock(Roca roca){
        if(rocas == null){
            rocas = new ArrayList<>();
        }

        rocas.add(roca);
    }

    public void actualizar(long time) throws Exception {
        checkEstadoSala();

        jugador.procesarOrdenes(orientacionPad);
        disparosJugador.addAll( jugador.procesarDisparos(orientacionDisparo) );

        if(bombaActiva) {
            bombas.addAll(jugador.fireBomb());
            bombaActiva = false;
        }

        jugador.actualizar(time);

        for(EnemigoMelee enemigo : enemigos)
            enemigo.actualizar(time);

        for(DisparoJugador disparo : disparosJugador)
            disparo.actualizar(time);

        for(BombaActiva bomba : bombas)
            bomba.actualizar(time);

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

        for(Roca roca : rocas)
            roca.dibujar(canvas);

        for(BombaActiva bomba : bombas)
            bomba.dibujar(canvas);

    }

    protected List<Modelo> modelIn(Modelo modelo, int x, int y){
        List<Modelo> modelos = new ArrayList<>();

        if(jugador.colisionanCoordenadas(modelo,x,y) && jugador!=modelo)
            modelos.add(jugador);

        for(EnemigoMelee enemigo : enemigos)
            if(enemigo.colisionanCoordenadas(modelo,x,y) && enemigo!=modelo)
                modelos.add(enemigo);

        for(DisparoJugador disparo : disparosJugador)
            if(disparo.colisionanCoordenadas(modelo,x,y) && disparo!=modelo && modelo!=jugador)
                modelos.add(disparo);

        for(Item item : items)
            if(item.colisionanCoordenadas(modelo,x,y) && item!=modelo)
                modelos.add(item);

        for(Roca roca : rocas)
            if(roca.colisionanCoordenadas(modelo,x,y) && roca!=modelo)
                modelos.add(roca);

        for(Puerta puerta : puertas.values())
            if(puerta.colisionanCoordenadas(modelo,x,y) && puerta!=modelo)
                modelos.add(puerta);

        return modelos;
    }

    protected void modelsInExplosion(BombaActiva bomba){

        if( checkExplosion(bomba, jugador) )
            jugador.setHP( jugador.HP-1 );

        for(EnemigoMelee enemigo : enemigos)
            if( checkExplosion(bomba, enemigo) )
                enemigo.HP = enemigo.HP - bomba.daño;

        for(Iterator<Roca> iterator = rocas.iterator(); iterator.hasNext();) {
            Roca roca = iterator.next();
            if (checkExplosion(bomba, roca)) {
                iterator.remove();
                continue;
            }
        }

        for(Puerta puerta : puertas.values()){
            if( checkExplosion(bomba, puerta) )
                puerta.forzada = true;
        }

    }

    protected boolean checkExplosion(BombaActiva bomba, Modelo modelo){
        double fC = Math.pow(modelo.x - bomba.x,2);
        double sC = Math.pow(modelo.y - bomba.y,2);
        double res = Math.sqrt(fC + sC);

        return res <= bomba.radio;
    }

    protected boolean colisiona(Modelo modelo, int x, int y){
        int tileX = (int) (x / Tile.ancho);
        int tileY = (int) (y / Tile.altura);

        return colisiona(modelo,x,y,tileX,tileY);
    }

    protected boolean colisiona(Modelo modelo, int x, int y, int tileX, int tileY){
        boolean colision = (mapaTiles[tileX][tileY].tipoDeColision != Tile.PASABLE);

        if(colision)
            return true;

        List<Modelo> modelos = modelIn(modelo,x,y);

        if(modelos.size()==0)
            return false;

        for(Modelo model : modelos) {
            if (model.colision != Modelo.PASABLE) {
                return true;
            }
        }

        return false;
    }

    protected void aplicarReglasMovimiento() throws Exception {
        reglasMovimientoJugador();
        reglasMovimientoColisionPuerta();
        reglasDeMovimientoEnemigos();
        reglasDeMovimientoDisparosJugador();
        reglasDeMovimientoColisionPickUps();
        reglasDeMovimientoBombas();
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

        if(!colisiona(jugador,virtualX,virtualY,tileXJugador,tileYJugador)){
            jugador.x = virtualX;
            jugador.y = virtualY;
        }

    }

    protected void reglasDeMovimientoEnemigos(){
        for(Iterator<EnemigoMelee> iterator = enemigos.iterator(); iterator.hasNext();){
            EnemigoMelee enemigo = iterator.next();

            if(enemigo.HP <= 0 )
                enemigo.estado = EnemigoMelee.ESTADO_MUERTO;

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

            if(colisiona(disparo,virtualX,virtualY)) {
                disparo.estado = DisparoJugador.FINALIZANDO;
                continue;
            }

            for(EnemigoMelee enemigo : enemigos) {
                if (disparo.colisiona(enemigo) && disparo.estado == DisparoJugador.DISPARANDO) {
                    enemigo.HP -= disparo.damage;

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

    protected void reglasDeMovimientoBombas(){

        for(Iterator<BombaActiva> iterator = bombas.iterator(); iterator.hasNext();){
            BombaActiva bomba = iterator.next();

            if(bomba.estado == BombaActiva.EXPLOTADA){
                iterator.remove();
                continue;
            }

            if(bomba.estado == BombaActiva.EXPLOTANDO && !bomba.explotada){
                modelsInExplosion(bomba);
                bomba.explotada = true;
            }

        }

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

    public static String getLayout(){

        int rng = new Random().nextInt(NUMBER_OF_LAYOUTS);

        switch(rng){
            case 0:
                return SALA_CUADRADA_1;

            case 1:
                return SALA_CUADRADA_2;

            case 2:
                return SALA_CUADRADA_3;

            case 3:
                return SALA_CUADRADA_4;
        }

        return null;
    }

}
