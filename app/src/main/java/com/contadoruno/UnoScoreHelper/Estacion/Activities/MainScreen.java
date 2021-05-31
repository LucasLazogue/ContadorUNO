package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.contadoruno.UnoScoreHelper.Logica.Exceptions.NombreVacio;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.ReglaYaExiste;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IJugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainScreen extends AppCompatActivity {
  private Button newGame, newReglas, historial, cargarPartida, continuarPartida, jugadores;
  private AdView ads;

  private Boolean cargado = false;
  private Boolean partidaEnCurso = false;
  private Boolean partidaCargada = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mainscreen);

    newGame = findViewById(R.id.newGame);
    newReglas = findViewById(R.id.crearReglas);
    historial = findViewById(R.id.historial);
    cargarPartida = findViewById(R.id.cargarPartida);
    continuarPartida = findViewById(R.id.continuarPartida);
    jugadores = findViewById(R.id.jugadores);
    ads = findViewById(R.id.adView);

    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    IReglaController rc = f.getIReglaController();
    IJugadorController jc = f.getIJugadorController();

    cargarReglasDefault();

    MobileAds.initialize(MainScreen.this);
    AdRequest adRequest = new AdRequest.Builder().build();
    ads.loadAd(adRequest);

    if (pc.hayPartidasEnCurso()) {
      cargarPartida.setVisibility(View.VISIBLE);
      partidaCargada = true;
    }

    if (pc.hayPartidaCargada()) {
      continuarPartida.setVisibility(View.VISIBLE);
      partidaEnCurso = true;
    }

    if (pc.hayPartidasFinalizadas()) {
      historial.setVisibility(View.VISIBLE);
    }

    continuarPartida.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainScreen.this, PartidaEnJuego.class);
        int id = pc.getUltimaPartidaCargada();
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
      }
    });

    newGame.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainScreen.this, NuevaPartida.class);
        startActivity(intent);
        finish();
      }
    });

    cargarPartida.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainScreen.this, Historial.class);
        intent.putExtra("historial", false);
        startActivity(intent);
        finish();
      }
    });

    newReglas.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainScreen.this, Reglas.class);
        startActivity(intent);
        finish();
      }
    });

    jugadores.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainScreen.this, Jugadores.class);
        startActivity(intent);
        finish();
      }
    });

    historial.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainScreen.this, Historial.class);
        intent.putExtra("historial", true);
        startActivity(intent);
        finish();
      }
    });
  }

  private void cargarReglasDefault(){
    Factory f = Factory.getInstance();
    IJugadorController jc = f.getIJugadorController();
    IReglaController rc = f.getIReglaController();
    //CARGAR REGLAS
    try {
      rc.altaRegla("UNO Classic", true);
      rc.altaRegla("UNO Flip", false);
    } catch (ReglaYaExiste | NombreVacio reglaYaExiste) {
      reglaYaExiste.printStackTrace();
    }
  }
}