package com.isaac.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.isaac.R;
import com.isaac.gestores.CargadorGraficos;
import com.isaac.gestores.GestorAudio;
import com.isaac.graficos.Sprite;
import com.isaac.modelos.disparos.BombaActiva;
import com.isaac.modelos.disparos.DisparoJugador;
import com.isaac.modelos.item.BasicShot;
import com.isaac.modelos.item.DamageModifier;
import com.isaac.modelos.item.ShotModifier;
import com.isaac.modelos.nivel.Sala;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 24/10/2017.
 */

public class Jugador extends Modelo{

    // VARIABLES GRÁFICAS

    public static final int MOVIMIENTO_DERECHA = 0;
    public static final int MOVIMIENTO_IZQUIERDA = 1;
    public static final int MOVIMIENTO_ABAJO = 2;
    public static final int MOVIMIENTO_ARRIBA = 3;
    public static final int MOVIMIENTO_ARRIBA_DERECHA = 4;
    public static final int MOVIMIENTO_ARRIBA_IZQUIERDA = 5;
    public static final int MOVIMIENTO_ABAJO_DERECHA = 6;
    public static final int MOVIMIENTO_ABAJO_IZQUIERDA = 7;
    public static final int PARADO = 8;

    public static final int DISPARO_DERECHA = 0;
    public static final int DISPARO_IZQUIERDA = 1;
    public static final int DISPARO_ABAJO = 2;
    public static final int DISPARO_ARRIBA = 3;
    public static final int NO_DISPARO = 4;

    private static final String CABEZA_DERECHA = "cabeza_derecha";
    private static final String CABEZA_IZQUIERDA = "cabeza_izquierda";
    private static final String CABEZA_ATRAS = "cabeza_atras";
    private static final String CABEZA_ADELANTE = "cabeza_adelante";
    private static final String MOVER_ADELANTE_ATRAS = "mover_adelante";
    private static final String MOVER_DERECHA = "mover_derecha";
    private static final String MOVER_IZQUIERDA = "mover_izquierda";
    private static final String PARADO_SPRITE = "parado";

    private static final String VOLAR_ADELANTE = "volar_adelante";
    private static final String VOLAR_DERECHA  = "volar_derecha";
    private static final String VOLAR_IZQUIERDA = "volar_izquierda";
    private static final String VOLAR_ATRAS = "volar_atras";

    private final static int alturaCabeza = 25;
    private final static int anchoCabeza = 29;
    private final static int alturaCuerpo = 14;
    private final static int anchoCuerpo = 32;

    private Sprite spriteCabeza;
    private Sprite spriteCuerpo;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    private int orientacion;
    private Drawable escudo;
    public double msInmunidad = 0;

    //----------------------------------------------------------------------

    //VARIABLES LOGICAS

    private double aceleracionX;
    private double aceleracionY;

    private long tearDelay;
    private long tearRange;
    private double tearDamage;
    private int HP;
    private int maxHP;
    private int actualMaxHP;
    private double speed;

    private int actualDelay;

    private boolean flying;

    private int numBombas;
    private int numLlaves;
    private int numMonedas;

    private boolean shielded;
    private long actualShield;
    private long maxShield;

    private List<ShotModifier> shotModifiers;
    private List<DamageModifier> damageModifiers;

    public Jugador(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, alturaCabeza+alturaCuerpo, Math.max(anchoCabeza,anchoCuerpo) );

        this.x =  xInicial;
        this.y =  yInicial - altura/2;

        aceleracionX = 0;
        aceleracionY = 0;

        tearDelay = 400;
        tearRange = 1000;
        actualDelay = 0;
        tearDamage = 3.5;
        HP = 6;
        actualMaxHP = 6;
        maxHP = 20;
        speed = 5;

        shotModifiers = new ArrayList<>();
        shotModifiers.add(new BasicShot());

        damageModifiers = new ArrayList<>();

        numLlaves = 99;
        numBombas = 99;
        flying = false;

        shielded = false;
        escudo = CargadorGraficos.cargarDrawable(context, R.drawable.shield);

        inicializar();
    }

    public void inicializar (){

        Sprite cabezaDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cabeza_derecha),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_DERECHA, cabezaDerecha);

        Sprite cabezaIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cabeza_izquierda),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_IZQUIERDA, cabezaIzquierda);

        Sprite cabezaAtras = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cabeza_atras),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_ATRAS, cabezaAtras);

        Sprite cabezaAdelante = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cabeza_defrente),
                anchoCabeza, alturaCabeza,
                2, 2, true);
        sprites.put(CABEZA_ADELANTE, cabezaAdelante);

        Sprite cuerpoDerechaDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_andar_derecha),
                anchoCuerpo, alturaCuerpo,
                10, 10, true);
        sprites.put(MOVER_DERECHA, cuerpoDerechaDerecha);

        Sprite cuerpoDerechaIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_andar_izquierda),
                anchoCuerpo, alturaCuerpo,
                10, 10, true);
        sprites.put(MOVER_IZQUIERDA, cuerpoDerechaIzquierda);

        Sprite cuerpoAdelanteAtras = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_andar_adelante_atras),
                anchoCuerpo, alturaCuerpo,
                10, 10, true);
        sprites.put(MOVER_ADELANTE_ATRAS, cuerpoAdelanteAtras);

        Sprite cuerpoParado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_cuerpo_parado),
                anchoCuerpo, alturaCuerpo,
                1, 1, true);
        sprites.put(PARADO_SPRITE, cuerpoParado);

        Sprite volarAdelante = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_volar_adelante),
                44, 31,
                7, 7, true);
        sprites.put(VOLAR_ADELANTE, volarAdelante);

        Sprite volarIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_volar_izquierda),
                29, 31,
                7, 7, true);
        sprites.put(VOLAR_IZQUIERDA, volarIzquierda);

        Sprite volarDerecha = new Sprite(
                        CargadorGraficos.cargarDrawable(context, R.drawable.isaac_volar_derecha),
                        29, 31,
                        7, 7, true);
        sprites.put(VOLAR_DERECHA, volarDerecha);

        Sprite volarArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.isaac_volar_arriba),
                44, 31,
                7, 7, true);
        sprites.put(VOLAR_ATRAS, volarArriba);


        spriteCabeza = sprites.get(CABEZA_ADELANTE);
        spriteCuerpo = sprites.get(PARADO_SPRITE);

    }

    public void actualizar (long tiempo) {
        spriteCuerpo.actualizar(tiempo);
        spriteCabeza.actualizar(tiempo);
        msInmunidad-=tiempo;

        this.actualDelay += tiempo;

        if(shielded){
            this.actualShield += tiempo;

            if(actualShield >= maxShield){
                shielded = false;
            }
        }

    }

    public List<DisparoJugador> procesarDisparos (int orientacionPad){
        ArrayList<DisparoJugador> disparos = new ArrayList<>();

        if(orientacionPad!=Jugador.NO_DISPARO && actualDelay>=tearDelay) {
            for(ShotModifier modifier : shotModifiers){
                disparos = modifier.shot(context, disparos, this.x, this.y, tearRange, tearDamage, orientacionPad);
            }

            actualDelay = 0;
        }

        if(disparos.size()>0)
            GestorAudio.getInstancia().reproducirSonido(GestorAudio.DISPARAR_LAGRIMA);

        return disparos;
    }

    public void procesarOrdenes (int orientacionPad) {
        this.orientacion = orientacionPad;

        if (orientacionPad == MOVIMIENTO_DERECHA) {
            aceleracionY = 0;
            aceleracionX = speed;

            if(!flying) {
                spriteCabeza = sprites.get(CABEZA_DERECHA);
                spriteCuerpo = sprites.get(MOVER_DERECHA);
            }

            else{
                spriteCabeza = sprites.get(CABEZA_DERECHA);
                spriteCuerpo = sprites.get(VOLAR_DERECHA);
            }

        }
        else if (orientacionPad == MOVIMIENTO_ABAJO){
            aceleracionX = 0;
            aceleracionY = speed;

            if(!flying) {
                spriteCabeza = sprites.get(CABEZA_ADELANTE);
                spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
            }
            else{
                spriteCabeza = sprites.get(CABEZA_ADELANTE);
                spriteCuerpo = sprites.get(VOLAR_ADELANTE);
            }
        }
        else if(orientacionPad == MOVIMIENTO_IZQUIERDA){
            aceleracionY  =0;
            aceleracionX= -speed;

            if(!flying) {
                spriteCabeza = sprites.get(CABEZA_IZQUIERDA);
                spriteCuerpo = sprites.get(MOVER_IZQUIERDA);
            }

            else{
                spriteCabeza = sprites.get(CABEZA_IZQUIERDA);
                spriteCuerpo = sprites.get(VOLAR_IZQUIERDA);
            }
        }
        else if(orientacionPad == MOVIMIENTO_ARRIBA) {
            aceleracionX = 0;
            aceleracionY = -speed;

            if(!flying) {
                spriteCabeza = sprites.get(CABEZA_ATRAS);
                spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
            }
            else{
                spriteCabeza = sprites.get(CABEZA_ATRAS);
                spriteCuerpo = sprites.get(VOLAR_ATRAS);
            }
        }
        else if(orientacionPad == MOVIMIENTO_ARRIBA_DERECHA){
            aceleracionX = 5;
            aceleracionY = -speed;

            spriteCabeza = sprites.get(CABEZA_ATRAS);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == MOVIMIENTO_ARRIBA_IZQUIERDA){
            aceleracionX = -speed;
            aceleracionY = -speed;

            spriteCabeza = sprites.get(CABEZA_ATRAS);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == MOVIMIENTO_ABAJO_DERECHA){
            aceleracionX = speed;
            aceleracionY = speed;

            spriteCabeza = sprites.get(CABEZA_ADELANTE);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == MOVIMIENTO_ABAJO_IZQUIERDA){
            aceleracionX = -speed;
            aceleracionY = speed;

            spriteCabeza = sprites.get(CABEZA_ADELANTE);
            spriteCuerpo = sprites.get(MOVER_ADELANTE_ATRAS);
        }
        else if(orientacionPad == PARADO) {
            frenar();

            if(!flying) {
                spriteCabeza = sprites.get(CABEZA_ADELANTE);
                spriteCuerpo = sprites.get(PARADO_SPRITE);
            }

            else{
                spriteCabeza = sprites.get(CABEZA_ADELANTE);
                spriteCuerpo = sprites.get(VOLAR_ADELANTE);
            }

        }

    }

    public void dibujar(Canvas canvas){
        int xCabeza = (int)(x-(ancho/2)+(anchoCabeza/2));
        int yCabeza = (int)(y-(altura/2)+(alturaCabeza/2));

        int xCuerpo = xCabeza;
        int yCuerpo = (yCabeza+(alturaCabeza/2)+(alturaCuerpo/2)-4);

        if(flying && (orientacion == Jugador.PARADO || orientacion == Jugador.MOVIMIENTO_ABAJO)){
            yCuerpo -= 1;
            spriteCuerpo.dibujarSprite(canvas, xCuerpo - Sala.scrollEjeX, yCuerpo - Sala.scrollEjeY);
            spriteCabeza.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCabeza - Sala.scrollEjeY);
        }

        else if(flying && orientacion == Jugador.MOVIMIENTO_IZQUIERDA){
            xCuerpo += 7;
            spriteCuerpo.dibujarSprite(canvas, xCuerpo - Sala.scrollEjeX, yCuerpo - Sala.scrollEjeY);
            spriteCabeza.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCabeza - Sala.scrollEjeY);
        }

        else if(flying && orientacion == Jugador.MOVIMIENTO_DERECHA){
            xCuerpo -= 7;
            spriteCuerpo.dibujarSprite(canvas, xCuerpo - Sala.scrollEjeX, yCuerpo - Sala.scrollEjeY);
            spriteCabeza.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCabeza - Sala.scrollEjeY);
        }

        else if(flying && orientacion == Jugador.MOVIMIENTO_ARRIBA){
            yCuerpo -= 1;
            spriteCabeza.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCabeza - Sala.scrollEjeY);
            spriteCuerpo.dibujarSprite(canvas, xCuerpo - Sala.scrollEjeX, yCuerpo - Sala.scrollEjeY);
        }

        else{
            spriteCuerpo.dibujarSprite(canvas, xCuerpo - Sala.scrollEjeX, yCuerpo - Sala.scrollEjeY);
            spriteCabeza.dibujarSprite(canvas, xCabeza - Sala.scrollEjeX, yCabeza - Sala.scrollEjeY);
        }

        if(shielded){
            int yArriba = (int) (y - 40 / 2) - Sala.scrollEjeY;
            int xIzquierda = (int) (x - 40 / 2) - Sala.scrollEjeX;

            escudo.setBounds(xIzquierda, yArriba, xIzquierda
                    + 40, yArriba + 40);
            escudo.draw(canvas);
        }

    }

    private void frenar(){
        aceleracionX=0;
        aceleracionY=0;
    }

    public int getNumBombas() {
        return numBombas;
    }

    public void setNumBombas(int numBombas) {
        if(numBombas>=0 && numBombas<=99)
            this.numBombas = numBombas;
    }

    public int getNumLlaves() {
        return numLlaves;
    }

    public void setNumLlaves(int numLlaves) {
        if(numLlaves>=0 && numLlaves<=99)
            this.numLlaves = numLlaves;
    }

    public int getNumMonedas() {
        return numMonedas;
    }

    public void setNumMonedas(int numMonedas) {
        if(numMonedas>=0 && numMonedas<=99)
            this.numMonedas = numMonedas;
    }

    public List<BombaActiva> fireBomb(){
        List<BombaActiva> bombas = new ArrayList<>();

        if(this.numBombas>0){
            bombas.add(new BombaActiva(context,x,y));
            numBombas--;
        }

        return bombas;
    }

    public void addDamageModifier(DamageModifier modifier){
        damageModifiers.add(modifier);
    }

    public void addShotModifier(ShotModifier modifier){
        shotModifiers.add(modifier);
    }

    public double getTearDamage(){
        return tearDamage;
    }

    public void setTearDamage(double damage){
        this.tearDamage = damage;
    }

    public void setHP(int newHP){
        if(newHP>=0 && newHP<=this.actualMaxHP){
            this.HP = newHP;
        }
    }

    public int getHP(){
        return this.HP;
    }

    public void setMaxHP(int newMaxHP){
        if(newMaxHP<maxHP){
            actualMaxHP = actualMaxHP+2;
        }
    }

    public int getMaxHP(){
        return actualMaxHP;
    }

    public void setTearDelay(long tearDelay){
        if(tearDelay>=50){
            this.tearDelay = tearDelay;
        }

        else{
            this.tearDelay = 50;
        }

    }

    public void setEspecialTearDelay(long newTearDelay){
        this.tearDelay = newTearDelay;

        if(newTearDelay<25)
            this.tearDelay = 25;
    }

    public long getTearDelay(){
        return this.tearDelay;
    }

    public void setTearRange(long newRange){
        if(newRange>=150){
            this.tearRange = newRange;
        }
        else{
            this.tearRange = 150;
        }
    }

    public long getTearRange(){
        return this.tearRange;
    }

    public void setMovementSpeed(int ms){
        if(ms<10 && ms>2){
            this.speed = ms;
        }
    }

    public double getMovementSpeed(){
        return speed;
    }

    public boolean isFlying(){
        return flying;
    }

    public void setFlying(boolean status){
        flying = status;
    }

    public double getAceleracionX(){
        return aceleracionX;
    }

    public double getAceleracionY(){
        return aceleracionY;
    }

    public boolean isShielded(){
        return shielded;
    }

    public void setShielded(boolean shielded){
        this.shielded = shielded;
    }

    public long getActualShield(){
        return actualShield;
    }

    public void setActualShield(long actualShield){
        this.actualShield = actualShield;
    }

    public long getMaxShield(){
        return maxShield;
    }

    public void setMaxShield(long maxShield){
        this.maxShield = maxShield;
    }

    public void takeDamage(int initialDamage){

        if(!shielded) {
            if(msInmunidad<=0) {

                int damage = initialDamage;

                for (DamageModifier modifier : damageModifiers) {
                    damage = modifier.processDamage(this, damage);
                }

                if (damage > 0) {
                    GestorAudio.getInstancia().reproducirSonido(GestorAudio.ISAAC_DAÑO);
                }
                msInmunidad=3000;

                setHP(getHP() - damage);
            }
        }

    }
    public int getTipoModelo(){
        return Modelo.JUGADOR;
    }

}
