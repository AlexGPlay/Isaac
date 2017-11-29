package com.isaac.modelos.nivel;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.isaac.GameView;
import com.isaac.gestores.CargadorSalas;
import com.isaac.gestores.GestorAudio;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.Modelo;
import com.isaac.modelos.disparos.BombaActiva;
import com.isaac.modelos.disparos.DisparoEnemigo;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.enemigo.EnemigoBase;
import com.isaac.modelos.enemigo.EnemigoDispara;
import com.isaac.modelos.enemigo.EnemigoMelee;
import com.isaac.modelos.item.Item;
import com.isaac.modelos.item.pickups.Bomba;
import com.isaac.modelos.item.pickups.Cofre;
import com.isaac.modelos.item.pickups.Llave;
import com.isaac.modelos.item.pickups.Moneda;
import com.isaac.modelos.item.pickups.PickupID;
import com.isaac.modelos.item.pickups.Vida;

import java.io.Console;
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
    protected List <EnemigoBase> enemigos;
    protected List<DisparoJugador> disparosJugador;
    protected  List<DisparoEnemigo> disparosEnemigo;
    protected List<BombaActiva> bombas;
    protected List<Item> items;
    protected List<Roca> rocas;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    public Nivel nivel;
    protected Context context;

    protected HashMap<String,Puerta> puertas;

    private boolean sonidoCerrada = false;
    private boolean sonidoAbierta = false;

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
        enemigos.add(new EnemigoDispara(context,150,100));

        this.disparosEnemigo= new ArrayList<DisparoEnemigo>();

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
                puerta.setAbierta(false);
                int x = puerta.getXEntrada();
                int y = puerta.getYEntrada();

                mapaTiles[x][y].tipoDeColision = Tile.SOLIDO;

                if(puerta.isForzada()){
                    puerta.setAbierta(true);
                    mapaTiles[x][y].tipoDeColision = Tile.PASABLE;
                }

                if(!sonidoCerrada){
                    sonidoCerrada = true;
                    GestorAudio.getInstancia().reproducirSonido(GestorAudio.PUERTA_CERRAR);
                }

            }
        }

        else if(enemigos.size()<=0){
            for(Puerta puerta : puertas.values()) {
                if(!(puerta instanceof PuertaLlave))
                    puerta.setAbierta(true);

                else if(puerta instanceof PuertaLlave && ((PuertaLlave)puerta).getLlavesNecesarias()==0)
                    puerta.setAbierta(true);

                int x = puerta.getXEntrada();
                int y = puerta.getYEntrada();

                if(!sonidoAbierta){
                    sonidoAbierta = true;
                    GestorAudio.getInstancia().reproducirSonido(GestorAudio.PUERTA_ABRIR);
                }

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
                GestorAudio.getInstancia().reproducirSonido(GestorAudio.DROP_LLAVE);
                break;

            case PickupID.MONEDA:
                items.add(new Moneda(context, (anchoMapaTiles()*Tile.ancho)/2, (altoMapaTiles()*Tile.altura)/2));
                GestorAudio.getInstancia().reproducirSonido(GestorAudio.DROP_MONEDA);
                break;

            case PickupID.VIDA:
                items.add(new Vida(context, (anchoMapaTiles()*Tile.ancho)/2, (altoMapaTiles()*Tile.altura)/2));
                break;

            case PickupID.COFRE:
                items.add(new Cofre(context, (anchoMapaTiles()*Tile.ancho)/2, (altoMapaTiles()*Tile.altura)/2));
                GestorAudio.getInstancia().reproducirSonido(GestorAudio.DROP_COFRE);
                break;

            case PickupID.NONE:
                break;
        }

        itemsDropped = true;
    }

    public void moveToRoom(String puerta){
        String contraria = getOppositeDoor(puerta);
        Puerta entrada = puertas.get(contraria);

        if(entrada!=null) {
            if(contraria == PUERTA_ABAJO){
                jugador.setX( entrada.getXSalida() * Tile.ancho + Tile.ancho/2 );
                jugador.setY( entrada.getYSalida() * Tile.altura - Tile.altura/2 );
            }

            else if(contraria == PUERTA_ARRIBA){
                jugador.setX( entrada.getXSalida() * Tile.ancho + Tile.ancho/2 );
                jugador.setY( entrada.getYSalida() * Tile.altura + Tile.altura );
            }

            else if(contraria == PUERTA_DERECHA){
                jugador.setX( entrada.getXSalida() * Tile.ancho + Tile.ancho/2 );
                jugador.setY( entrada.getYSalida() * Tile.altura + Tile.altura/2 );
            }

            else if(contraria == PUERTA_IZQUIERDA){
                jugador.setX( entrada.getXSalida() * Tile.ancho + Tile.ancho/2 );
                jugador.setY( entrada.getYSalida() * Tile.altura + Tile.altura/2 );
            }

            scrollEjeX = 0;
            scrollEjeY = 0;

            for(Puerta temp : puertas.values()) {
                temp.setAbierta(false);
                temp.setForzada(false);
            }

        }

        else {
            jugador.setX( ((anchoMapaTiles()*Tile.ancho)/2) );
            jugador.setY( ((altoMapaTiles()*Tile.altura)/2) );
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

        for(EnemigoBase enemigo : enemigos)
            enemigo.actualizar(time);

        for(DisparoJugador disparo : disparosJugador)
            disparo.actualizar(time);

        for(BombaActiva bomba : bombas)
            bomba.actualizar(time);

        for(DisparoEnemigo disparoEnemigo : disparosEnemigo){
            disparoEnemigo.actualizar(time);
        }

        aplicarReglasMovimiento();
    }

    public void dibujar(Canvas canvas){
        dibujarTiles(canvas);

        for( Puerta puerta : puertas.values() )
            puerta.dibujar(canvas);

        for(EnemigoBase enemigo : enemigos)
            enemigo.dibujar(canvas);

        for(DisparoJugador disparo : disparosJugador)
            disparo.dibujar(canvas);

        for(Item item : items)
            item.dibujar(canvas);

        for(Roca roca : rocas)
            roca.dibujar(canvas);

        jugador.dibujar(canvas);

        for(BombaActiva bomba : bombas) {
            bomba.dibujar(canvas);
        }

        if(disparosEnemigo.size()>=1) {
            for (DisparoEnemigo disparoEnemigo : disparosEnemigo) {
                disparoEnemigo.dibujar(canvas);
            }
        }



    }

    protected List<Modelo> modelIn(Modelo modelo, int x, int y){
        List<Modelo> modelos = new ArrayList<>();

        if(jugador.colisionanCoordenadas(modelo,x,y) && jugador!=modelo)
            modelos.add(jugador);

        for(EnemigoBase enemigo : enemigos)
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
            jugador.takeDamage(1);

        for(EnemigoBase enemigo : enemigos)
            if( checkExplosion(bomba, enemigo) )
                enemigo.setHP(enemigo.getHP() - bomba.getDaño());

        for(Iterator<Roca> iterator = rocas.iterator(); iterator.hasNext();) {
            Roca roca = iterator.next();
            if (checkExplosion(bomba, roca)) {
                iterator.remove();
                continue;
            }
        }

        for(Puerta puerta : puertas.values()){
            if( checkExplosion(bomba, puerta) )
                puerta.setForzada(true);
        }

    }

    protected boolean checkExplosion(BombaActiva bomba, Modelo modelo){
        double fC = Math.pow(modelo.getX() - bomba.getX(),2);
        double sC = Math.pow(modelo.getY() - bomba.getY(),2);
        double res = Math.sqrt(fC + sC);

        if(res<=bomba.getRadio())
            return true;

        fC = Math.pow(modelo.getX() + modelo.getAncho() - bomba.getX(),2);
        sC = Math.pow(modelo.getY() - bomba.getY(),2);
        res = Math.sqrt(fC + sC);

        if(res<=bomba.getRadio())
            return true;

        fC = Math.pow(modelo.getX() - modelo.getAncho() - bomba.getX(),2);
        sC = Math.pow(modelo.getY() - bomba.getY(),2);
        res = Math.sqrt(fC + sC);

        if(res<=bomba.getRadio())
            return true;

        fC = Math.pow(modelo.getX() - bomba.getX(),2);
        sC = Math.pow(modelo.getY() + modelo.getAltura() - bomba.getY(),2);
        res = Math.sqrt(fC + sC);

        if(res<=bomba.getRadio())
            return true;

        fC = Math.pow(modelo.getX() - bomba.getX(),2);
        sC = Math.pow(modelo.getY() - modelo.getAltura() - bomba.getY(),2);
        res = Math.sqrt(fC + sC);

        if(res<=bomba.getRadio())
            return true;

        return false;
    }

    protected int colisiona(Modelo modelo, int x, int y){
        int tileX = (int) (x / Tile.ancho);
        int tileY = (int) (y / Tile.altura);

        return colisiona(modelo,x,y,tileX,tileY);
    }

    protected int colisiona(Modelo modelo, int x, int y, int tileX, int tileY){
        boolean colision = (mapaTiles[tileX][tileY].tipoDeColision != Tile.PASABLE);

        if(colision)
            return Modelo.TILE;

        List<Modelo> modelos = modelIn(modelo,x,y);

        if(modelos.size()==0)
            return Modelo.VOID;

        for(Modelo model : modelos) {
            if (model.colision != Modelo.PASABLE) {
                return model.getTipoModelo();
            }
        }

        return Modelo.VOID;
    }

    protected int colisiona(Modelo modelo, List<Modelo> modelos){
        if(modelos.size()==0)
            return Modelo.VOID;

        for(Modelo model : modelos) {
            if (model.colision != Modelo.PASABLE) {
                return model.getTipoModelo();
            }
        }

        return Modelo.VOID;
    }

    protected void aplicarReglasMovimiento() throws Exception {
        reglasMovimientoJugador();
        reglasMovimientoColisionPuerta();
        reglasDeMovimientoEnemigos();
        reglasDeMovimientoDisparosJugador();
        reglasDeMovimientoColisionPickUps();
        reglasDeMovimientoBombas();
        reglasDeMovimientoDisparosEnemigo();
    }

    protected Puerta reglasMovimientoColisionPuerta(){
        for(String key : puertas.keySet()) {
            if (jugador.colisiona(puertas.get(key)) && puertas.get(key).isAbierta()) {
                disparosJugador.clear();
                nivel.moverSala(key);

                return puertas.get(key);
            }

            else if(jugador.colisiona(puertas.get(key)) && !puertas.get(key).isAbierta()){
                return puertas.get(key);
            }

        }

        return null;
    }

    protected void reglasMovimientoJugador(){

        int virtualX = (int) (jugador.getX() + jugador.getAceleracionX());
        int virtualY = (int) (jugador.getY() + jugador.getAceleracionY());

        int tileXJugador = (int) (virtualX / Tile.ancho);
        int tileYJugador = (int) (virtualY / Tile.altura);

        if(jugador.getAceleracionX()<0){
            tileXJugador = (int) ( (virtualX-jugador.getAncho()/2) / Tile.ancho);
        }

        if(jugador.getAceleracionX()>0){
            tileXJugador = (int) ( (virtualX+jugador.getAncho()/2) / Tile.ancho);
        }

        if(jugador.getAceleracionY()>0){
            tileYJugador = (int) ( (virtualY+jugador.getAltura()/2) / Tile.altura);
        }

        int colision = colisiona(jugador,virtualX,virtualY,tileXJugador,tileYJugador);

        if(colision == Modelo.VOID){
            jugador.setX(virtualX);
            jugador.setY(virtualY);
        }

        else if(colision == Modelo.ROCA && jugador.isFlying()){
            jugador.setX(virtualX);
            jugador.setY(virtualY);
        }

        else if(colision == Modelo.PUERTA){
            double ogX = jugador.getX();
            double ogY = jugador.getY();

            jugador.setX(virtualX);
            jugador.setY(virtualY);

            Puerta colisionPuerta = reglasMovimientoColisionPuerta();

            if(colisionPuerta != null && colisionPuerta instanceof PuertaLlave){
                if(jugador.getNumLlaves()>0) {
                    ((PuertaLlave) colisionPuerta).insertKey();
                    jugador.setNumLlaves(jugador.getNumLlaves()-1);
                }
            }

            jugador.setX(ogX);
            jugador.setY(ogY);
        }
    }

    protected void reglasDeMovimientoEnemigos() {
        for (Iterator<EnemigoBase> iterator = enemigos.iterator(); iterator.hasNext(); ) {
            EnemigoBase enemigo = iterator.next();

            // -- COMPROBAR ESTADO

            if (enemigo.getHP() <= 0)
                enemigo.estado = EnemigoMelee.ESTADO_MUERTO;

            if (enemigo.estado == EnemigoMelee.ESTADO_MUERTO) {
                iterator.remove();
                continue;
            }

            // -- CALCULO DE MOVIMIENTO
            double movX = 0;
            double movY = 0;

            if (jugador.getX() < enemigo.getX()) {
                movX = Math.min(enemigo.getAceleracionX(), Math.abs(jugador.getX() - enemigo.getX()));
                movX = -Math.abs(movX);
            }
            else if (jugador.getX() > enemigo.getX()) {
                movX = Math.min(enemigo.getAceleracionX(), Math.abs(jugador.getX() - enemigo.getX()));
            }

            if (jugador.getY() < enemigo.getY()) {
                movY = Math.min(enemigo.getAceleracionY(), Math.abs(jugador.getY() - enemigo.getY()));
                movY = -Math.abs(movY);
            }
            else if (jugador.getY() > enemigo.getY()) {
                movY = Math.min(enemigo.getAceleracionY(), Math.abs(jugador.getY() - enemigo.getY()));
            }

            if (movX != 0 && movY != 0) {
                movX /= 1.5;
                movY /= 1.5;
            }

            // -- COMPROBAR MOVIMIENTO
            int colision = colisiona(enemigo, (int) (enemigo.getX() + movX), (int) (enemigo.getY() + movY));

            if (colision == Modelo.VOID) {
                enemigo.setX(enemigo.getX() + movX);
                enemigo.setY(enemigo.getY() + movY);
            }
            else {
                int colisionX = colisiona(enemigo, (int) (enemigo.getX() + movX), (int) enemigo.getY());

                if (colisionX == Modelo.VOID) {
                    enemigo.setX(enemigo.getX() + movX);
                    enemigo.setY(enemigo.getY());
                }
                else {
                    int colisionY = colisiona(enemigo, (int) enemigo.getX(), (int) (enemigo.getY() + movY));

                    if (colisionY == Modelo.VOID) {
                        enemigo.setX(enemigo.getX());
                        enemigo.setY(enemigo.getY() + movY);
                    }

                }
            }

            if (enemigo.colisiona(jugador)) {
                jugador.takeDamage(1);
                enemigo.setX(enemigo.getX() - movX);
                enemigo.setY(enemigo.getY() - movY);
            }

            if(enemigo instanceof EnemigoDispara){
                DisparoEnemigo disparo;
                if(((EnemigoDispara) enemigo).comprobarAlineacionY(jugador)){
                    if(enemigo.getY()<jugador.getY()){
                        disparo = ((EnemigoDispara) enemigo).procesarDisparoDirigido(2);
                        if(disparo!=null) {
                            disparosEnemigo.add(disparo);
                        }
                    }
                    else {
                        disparo=((EnemigoDispara) enemigo).procesarDisparoDirigido(3);
                        if(disparo!=null) {
                            disparosEnemigo.add(disparo);
                        }
                    }
                }
                else if(((EnemigoDispara) enemigo).comprobarAlineacionX(jugador)){
                    if(enemigo.getX()<jugador.getX()){
                        disparo = ((EnemigoDispara) enemigo).procesarDisparoDirigido(0);
                        if(disparo!=null) {
                            disparosEnemigo.add(disparo);
                        }
                    }
                    else {
                        disparo = ((EnemigoDispara) enemigo).procesarDisparoDirigido(1);
                        if(disparo!=null) {
                            disparosEnemigo.add(disparo);
                        }
                    }
                    for(DisparoEnemigo disparoEnemigo : disparosEnemigo){
                        if(disparoEnemigo==null){
                            disparosEnemigo.remove(disparoEnemigo);
                        }
                    }
                }

            }

            // --GESTION DE SPRITE
            if(movX>0 && movY>0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_ABAJO);

            else if(movX==0 && movY>0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_ABAJO);

            else if(movX<0 && movY>0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_ABAJO);

            else if(movX<0 && movY==0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_IZQUIERDA);

            else if(movX<0 && movY<0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_ARRIBA);

            else if(movX==0 && movY<0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_ARRIBA);

            else if(movX>0 && movY<0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_ARRIBA);

            else if(movX>0 && movY==0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_DERECHA);

            else if(movX==0 && movY==0)
                enemigo.setMovimiento(EnemigoBase.MOVIMIENTO_PARADO);

        }
    }

    protected void reglasDeMovimientoDisparosJugador(){
        for(Iterator<DisparoJugador> iterator = disparosJugador.iterator(); iterator.hasNext();){
            DisparoJugador disparo = iterator.next();

            int virtualX = (int) (disparo.getX() + disparo.getAceleracionX());
            int virtualY = (int) (disparo.getY() + disparo.getAceleracionY());

            int tileXDisparo = (int) (virtualX / Tile.ancho);
            int tileYDisparo = (int) (virtualY / Tile.altura);

            if(disparo.estado == DisparoJugador.FINALIZADO){
                iterator.remove();
                GestorAudio.getInstancia().reproducirSonido(GestorAudio.DESAPARECER_LAGRIMA);
                continue;
            }

            if(disparo.isOutOfRange()){
                disparo.estado = DisparoJugador.FINALIZANDO;
                continue;
            }

            int colision = colisiona(disparo,virtualX,virtualY);
            if(colision != Modelo.VOID && colision != Modelo.ENEMIGO ) {
                disparo.estado = DisparoJugador.FINALIZANDO;
                continue;
            }

            for(EnemigoBase enemigo : enemigos) {
                if (disparo.colisiona(enemigo) && disparo.estado == DisparoJugador.DISPARANDO) {
                    enemigo.setHP(enemigo.getHP()-disparo.getDamage());

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
                disparo.setX(virtualX);
                disparo.setY(virtualY);
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

            if(bomba.estado == BombaActiva.EXPLOTANDO && !bomba.isExplotada()){
                modelsInExplosion(bomba);
                bomba.setExplotada(true);
            }

        }

    }

    protected void reglasDeMovimientoDisparosEnemigo(){
        for(Iterator<DisparoEnemigo> iterator = disparosEnemigo.iterator(); iterator.hasNext();){
            DisparoEnemigo disparo = iterator.next();

            int virtualX = (int) (disparo.getX() + disparo.getAceleracionX());
            int virtualY = (int) (disparo.getY() + disparo.getAceleracionY());

            if(disparo.estado == DisparoJugador.FINALIZADO){
                iterator.remove();
                GestorAudio.getInstancia().reproducirSonido(GestorAudio.DESAPARECER_LAGRIMA);
                continue;
            }

            if(disparo.isOutOfRange()){
                disparo.estado = DisparoJugador.FINALIZANDO;
                continue;
            }

            int colision = colisiona(disparo,virtualX,virtualY);
            if(colision != Modelo.VOID && colision != Modelo.ENEMIGO ) {
                disparo.estado = DisparoJugador.FINALIZANDO;
                continue;
            }

            if(disparo.colisiona(jugador)){
                jugador.takeDamage((int)disparo.getDamage());
            }

            for(Puerta puerta : puertas.values()) {
                if (puerta.colisiona(disparo)) {
                    disparo.estado = DisparoJugador.FINALIZANDO;
                    continue;
                }
            }

            if(disparo.estado == DisparoJugador.DISPARANDO) {
                disparo.setX(virtualX);
                disparo.setY(virtualY);
            }
        }
    }

    protected void dibujarTiles(Canvas canvas){
        // Calcular que tiles serán visibles en la pantalla
        // La matriz de tiles es más grande que la pantalla
        int tileXJugador = (int) jugador.getX() / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.getX() - scrollEjeX));
        izquierda = Math.max(0,izquierda); // Que nunca sea < 0, ej -1

        if ( jugador.getX()  < anchoMapaTiles()* Tile.ancho - GameView.pantallaAncho*0.3 )
            if( jugador.getX() - scrollEjeX > GameView.pantallaAncho * 0.7 ){
                scrollEjeX = (int) ((jugador.getX() ) - GameView.pantallaAncho* 0.7);
            }


        if ( jugador.getX()  > GameView.pantallaAncho*0.3 )
            if( jugador.getX() - scrollEjeX < GameView.pantallaAncho *0.3 ){
                scrollEjeX = (int)(jugador.getX() - GameView.pantallaAncho*0.3);
            }


        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        // el ultimo tile visible, no puede superar el tamaño del mapa
        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        int tileYJugador = (int) jugador.getY() / Tile.altura;
        int alto = (int) (tileYJugador - tilesEnDistanciaY(jugador.getY() - scrollEjeY));
        alto = Math.max(0,alto);

        if(jugador.getY() < altoMapaTiles()*Tile.altura - GameView.pantallaAlto*0.3){
            if(jugador.getY() -scrollEjeY > GameView.pantallaAlto*0.7){
                scrollEjeY = (int) (jugador.getY() - GameView.pantallaAlto*0.7);
            }
        }

        if(jugador.getY() > GameView.pantallaAlto*0.3){
            if(jugador.getY()-scrollEjeY < GameView.pantallaAlto*0.3){
                scrollEjeY = (int)(jugador.getY() - GameView.pantallaAlto*0.3);
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
