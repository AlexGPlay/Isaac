package com.isaac.gestores;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.isaac.R;

import java.util.HashMap;

/**
 * Created by alexgp1234 on 4/10/17.
 */
public class GestorAudio implements MediaPlayer.OnPreparedListener {

    // Pool de sonidos, para efectos de sonido.
    // Suele fallar el utilizar ficheros de sonido demasiado grandes
    private SoundPool poolSonidos;
    private HashMap<Integer, Integer> mapSonidos;
    private Context contexto;
    // Media Player para bucle de sonido de fondo.
    private MediaPlayer sonidoAmbiente;
    private AudioManager gestorAudio;

    private static GestorAudio instancia = null;


    //------------------------- VARIABLES SONIDOS
    public static final int DISPARAR_LAGRIMA = 1;
    public static final int DESAPARECER_LAGRIMA = 2;
    public static final int BOMBA_EXPLOTAR = 3;
    public static final int ISAAC_DAÑO = 4;
    public static final int PUERTA_ABRIR = 5;
    public static final int PUERTA_CERRAR = 6;
    public static final int DROP_LLAVE = 7;
    public static final int DROP_MONEDA = 8;
    public static final int DROP_COFRE = 9;
    public static final int PICK_LLAVE = 10;
    public static final int PICK_MONEDA = 11;
    public static final int PICK_COFRE = 12;
    public static final int PICK_ITEM = 13;
    public static final int ISAAC_DIES = 14;
    public static final int SELECT_LEFT = 15;
    public static final int SELECT_RIGHT = 16;
    public static final int START_UP = 17;

    private GestorAudio() { }

    public static GestorAudio getInstancia(Context contexto, int idMusicaAmbiente) {
        synchronized (GestorAudio.class) {
            if (instancia == null) {
                instancia = new GestorAudio();
                instancia.initSounds(contexto, idMusicaAmbiente);
            }
            return instancia;
        }
    }

    public static GestorAudio getInstancia() {
        return instancia;
    }

    public void initSounds(Context contexto, int idMusicaAmbiente) {
        this.contexto = contexto;
        poolSonidos = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mapSonidos = new HashMap<Integer, Integer>();
        gestorAudio = (AudioManager) contexto.getSystemService(Context.AUDIO_SERVICE);
        sonidoAmbiente = MediaPlayer.create(contexto, idMusicaAmbiente);
        sonidoAmbiente.setLooping(true);
        sonidoAmbiente.setVolume(1, 1);
    }

    public void changeSound(int idMusicaAmbiente){
        sonidoAmbiente = MediaPlayer.create(contexto, idMusicaAmbiente);
        sonidoAmbiente.setLooping(true);
        sonidoAmbiente.setVolume(1, 1);
    }

    public void reproducirMusicaAmbiente() {
        try {
            if (!sonidoAmbiente.isPlaying()) {
                try {
                    sonidoAmbiente.setOnPreparedListener(this);
                    sonidoAmbiente.prepareAsync();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pararMusicaAmbiente() {
        if (sonidoAmbiente.isPlaying()) {
            sonidoAmbiente.stop();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void registrarSonido(int index, int SoundID) {
        mapSonidos.put(index, poolSonidos.load(contexto, SoundID, 1));
    }

    public void reproducirSonido(int index) {
        float volumen = gestorAudio.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumen = volumen / gestorAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        poolSonidos.play(mapSonidos.get(index), volumen, volumen, 1, 0, 1f);
        Log.d("Sonidos","Reproduciendo sonido " + index);
    }

}
