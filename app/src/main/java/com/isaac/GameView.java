package com.isaac;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.isaac.controles.Pad;
import com.isaac.gestores.CargadorSalas;
import com.isaac.gestores.GestorAudio;
import com.isaac.modelos.Jugador;
import com.isaac.modelos.hud.IconoBomba;
import com.isaac.modelos.hud.IconoLlave;
import com.isaac.modelos.hud.IconoMoneda;
import com.isaac.modelos.nivel.Nivel;
import com.isaac.modelos.nivel.Sala;
import com.isaac.modelos.hud.IconoVida;


public class GameView extends SurfaceView implements SurfaceHolder.Callback  {

    boolean iniciado = false;
    Context context;
    GameLoop gameloop;
    Canvas canvas;
    GestorAudio gestorAudio;

    public static int pantallaAncho;
    public static int pantallaAlto;

    private Nivel nivel;
    public int numeroNivel = 0;
    private Pad padMovimiento;
    private Pad padDisparo;
    private Pad padBombas;

    //Hud
    private IconoLlave llaves;
    private IconoBomba bombas;
    private IconoMoneda monedas;

    public GameView(Context context) {
        super(context);
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;
        gameloop = new GameLoop(this);
        gameloop.setRunning(true);
    }

    public void updateCanvas(Canvas canvas){
        this.canvas = canvas;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId  = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for(int i =0; i < pointerCount; i++){
                    pointerIndex = i;
                    pointerId  = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        procesarEventosTouch();
        return true;
    }

    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];

    public void procesarEventosTouch(){
        boolean pulsacionPadMover = false;
        boolean pulsacionPadDisparo = false;
        boolean pulsacionPadBombas = false;

        for(int i=0; i < 6; i++) {

            if (accion[i] != NO_ACTION) {

                if (padMovimiento.estaPulsado(x[i], y[i])) {

                    int orientacion = padMovimiento.getOrientacion(x[i], y[i]);

                    if (accion[i] != ACTION_UP) {
                        pulsacionPadMover = true;
                        Sala.orientacionPad = orientacion;
                    }

                }

                else if(padDisparo.estaPulsado(x[i], y[i])){
                    int orientacion = padDisparo.getOrientacion(x[i],y[i]);

                    if(accion[i] != ACTION_UP){
                        pulsacionPadDisparo = true;
                        Sala.orientacionDisparo = orientacion;
                    }

                }

                else if(padBombas.estaPulsado(x[i], y[i])){

                    if(accion[i] != ACTION_UP) {
                        pulsacionPadBombas = true;
                        Sala.bombaActiva = true;
                    }
                }

            }
        }

        if(!pulsacionPadMover)
            Sala.orientacionPad = Jugador.PARADO;

        if(!pulsacionPadDisparo)
            Sala.orientacionDisparo = Jugador.NO_DISPARO;

        if(!pulsacionPadBombas)
            Sala.bombaActiva = false;
    }

    public void forceUpdate(){
        dibujar(canvas);
    }

    protected void inicializar() throws Exception {
        nivel = new Nivel(context,numeroNivel,this);
        padMovimiento = new Pad(context,70,270);
        padDisparo = new Pad(context,520,270);
        padBombas = new Pad(context, 520, 50);

        monedas = new IconoMoneda(context,GameView.pantallaAncho *0.04, GameView.pantallaAlto * 0.15);
        bombas = new IconoBomba(context,GameView.pantallaAncho *0.04, GameView.pantallaAlto * 0.22);
        llaves = new IconoLlave(context,GameView.pantallaAncho *0.04, GameView.pantallaAlto * 0.29);

        inicializarGestorAudio(context);
    }

    private void inicializarGestorAudio(Context context){
        gestorAudio = GestorAudio.getInstancia(context, R.raw.music_loop);
        gestorAudio.reproducirMusicaAmbiente();

        registrarSonidos();
    }

    private void registrarSonidos(){
        gestorAudio.registrarSonido(GestorAudio.DISPARAR_LAGRIMA, R.raw.tear_fire);
        gestorAudio.registrarSonido(GestorAudio.DESAPARECER_LAGRIMA, R.raw.tear_disappear);
        gestorAudio.registrarSonido(GestorAudio.BOMBA_EXPLOTAR, R.raw.bomb_explosion);
        gestorAudio.registrarSonido(GestorAudio.ISAAC_DAÑO, R.raw.isaac_hurt);
        gestorAudio.registrarSonido(GestorAudio.PUERTA_ABRIR, R.raw.door_open);
        gestorAudio.registrarSonido(GestorAudio.PUERTA_CERRAR, R.raw.door_close);
        gestorAudio.registrarSonido(GestorAudio.DROP_COFRE, R.raw.chest_drop);
        gestorAudio.registrarSonido(GestorAudio.DROP_LLAVE, R.raw.key_drop);
        gestorAudio.registrarSonido(GestorAudio.DROP_MONEDA, R.raw.coin_drop);
        gestorAudio.registrarSonido(GestorAudio.PICK_LLAVE, R.raw.key_pick);
        gestorAudio.registrarSonido(GestorAudio.PICK_MONEDA, R.raw.coin_pick);
        gestorAudio.registrarSonido(GestorAudio.PICK_COFRE, R.raw.chest_pick);
        gestorAudio.registrarSonido(GestorAudio.PICK_ITEM, R.raw.item_pick);
    }

    public void actualizar(long tiempo) throws Exception {
        nivel.actualizar(tiempo);
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
        padMovimiento.dibujar(canvas);
        padDisparo.dibujar(canvas);
        padBombas.dibujar(canvas);

        IconoVida[] vidas = nivel.getActualHP();

        monedas.actualizar( nivel.getMonedas() );
        monedas.dibujar(canvas);

        bombas.actualizar( nivel.getBombas() );
        bombas.dibujar(canvas);

        llaves.actualizar( nivel.getLlaves() );
        llaves.dibujar(canvas);

        for(IconoVida vida : vidas)
            vida.dibujar(canvas);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            }
            catch (InterruptedException e) {
            }
        }
    }

}

