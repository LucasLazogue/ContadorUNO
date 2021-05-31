package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorCartasJugadas;
import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorSeleccionarCartas;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataCarta;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataJugador;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;
import java.util.List;

public class NuevaMano extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerCartas, LManagerSeleccionadas;
  private RecyclerView.Adapter adaptadorCartas, adaptadorCartasSeleccionadas;
  private RecyclerView listaCartitas, listaCartitasAgregadas;

  private Button confirmar, manual;
  private TextView nombreJugadorMano, numeroManoAgregar, puntajeManoAgregar;

  private int id;
  private Boolean cargaCartas;
  private String nombreJugador;
  private int puntajeMano;
  private int puntajeManual;
  private List<String> cartas = new ArrayList<String>();
  private List<String> seleccionadas = new ArrayList<String>();
  private Toolbar toolbar;
  private LinearLayout layoutCartitasAgregadas;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.nuevamano);

    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    IReglaController rc = f.getIReglaController();

    nombreJugadorMano = findViewById(R.id.nombreJugadorMano);
    numeroManoAgregar = findViewById(R.id.numeroManoAgregar);
    puntajeManoAgregar = findViewById(R.id.puntajeManoAgregar);
    confirmar = findViewById(R.id.listoMano);
    manual = findViewById(R.id.btnEntradaManual);
    listaCartitas = findViewById(R.id.listaCartitas);
    listaCartitasAgregadas = findViewById(R.id.listaCartitasAgregadas);
    toolbar = findViewById(R.id.toolbar);
    layoutCartitasAgregadas = findViewById(R.id.layoutCartitasAgregadas);

    layoutCartitasAgregadas.setVisibility(View.GONE);

    toolbar.setTitle(getString(R.string.NuevaMano));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });


    id = (int) getIntent().getExtras().get("id");
    nombreJugador = (String) getIntent().getExtras().get("jugador");

    Boolean soloWilds = rc.soloWilds(pc.getRegla(id).getNombre());
    DataPartida p = pc.getDatosPartida(id);
    Boolean flip = p.getFlip();

    LManagerCartas = new GridLayoutManager(this, 5);
    adaptadorCartas = new AdaptadorSeleccionarCartas(flip, soloWilds, new AdaptadorSeleccionarCartas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).getCartas().add(nombre);
        adaptadorCartasSeleccionadas.notifyDataSetChanged();
        actualizarPuntaje(nombre);
        cargaCartas = true;
        layoutCartitasAgregadas.setVisibility(View.VISIBLE);
      }
    });

    listaCartitas.setLayoutManager(LManagerCartas);
    listaCartitas.setAdapter(adaptadorCartas);

    LManagerSeleccionadas = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    adaptadorCartasSeleccionadas = new AdaptadorCartasJugadas(R.layout.view_cartas_jugadas, seleccionadas, new AdaptadorCartasJugadas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        seleccionadas.remove(nombre);
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).setCartas(seleccionadas);
        adaptadorCartasSeleccionadas.notifyDataSetChanged();
        restarPuntuaje(nombre);
        if (seleccionadas.isEmpty()) {
          cargaCartas = false;
          layoutCartitasAgregadas.setVisibility(View.GONE);
        }
      }
    });

    listaCartitasAgregadas.setLayoutManager(LManagerSeleccionadas);
    listaCartitasAgregadas.setAdapter(adaptadorCartasSeleccionadas);

    DataJugador dj = pc.getDatosJugador(id, nombreJugador);

    nombreJugadorMano.setText(nombreJugador);
    numeroManoAgregar.setText(String.valueOf(dj.getManos().length + 1));

    manual.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NuevaMano.this);

        View alertComment = LayoutInflater.from(NuevaMano.this).inflate(R.layout.dialog_puntajemanual, null);
        builder.setView(alertComment);

        final EditText puntaje = alertComment.findViewById(R.id.puntajeAgregar);
        final Button confirmarPuntos = alertComment.findViewById(R.id.confirmarPuntos);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        puntaje.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            puntaje.post(new Runnable() {
              @Override
              public void run() {
                InputMethodManager inputMethodManager= (InputMethodManager) NuevaMano.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(puntaje, InputMethodManager.SHOW_IMPLICIT);
              }
            });
          }
        });
        puntaje.requestFocus();

        confirmarPuntos.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            puntajeManual = Integer.parseInt(puntaje.getText().toString());
            puntajeManoAgregar.setText(String.valueOf(puntajeManual));
            dialog.dismiss();
            seleccionadas.clear();
            cargaCartas = false;
          }
        });
      }
    });

    confirmar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(NuevaMano.this);

        View alertFinalizar = LayoutInflater.from(NuevaMano.this).inflate(R.layout.dialog_confirmar_mano, null);
        builder.setView(alertFinalizar);

        final Button aceptarPuntos = alertFinalizar.findViewById(R.id.aceptarPuntos);
        final Button cancelarPuntos = alertFinalizar.findViewById(R.id.cancelarPuntos);

        AlertDialog confirmar = builder.create();
        confirmar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmar.show();

        aceptarPuntos.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Factory f = Factory.getInstance();
            IPartidaController pc = f.getIPartidaController();
            //pc.agregarMano(id, nombreJugador, puntajeMano);
            if (cargaCartas) {
              pc.agregarMano(id, nombreJugador, seleccionadas);
            } else {
              pc.agregarMano(id, nombreJugador, puntajeManual);
            }
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            confirmar.dismiss();
            finish();
          }
        });

        cancelarPuntos.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            confirmar.dismiss();
          }
        });
      }
    });

  }

  private void actualizarPuntaje(String nombre) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataRegla regla = pc.getRegla(id);
    DataCarta[] cartas = regla.getCartas();
    for (int i = 0; i < cartas.length; i++) {
      if (cartas[i].getTipo().equals(nombre)) {
        puntajeMano = puntajeMano + cartas[i].getValor();
        break;
      }
    }
    puntajeManoAgregar.setText(String.valueOf(puntajeMano));
  }

  private void restarPuntuaje(String nombre) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataRegla regla = pc.getRegla(id);
    DataCarta[] cartas = regla.getCartas();
    for (int i = 0; i < cartas.length; i++) {
      if (cartas[i].getTipo().equals(nombre)) {
        puntajeMano = puntajeMano - cartas[i].getValor();
        break;
      }
    }
    puntajeManoAgregar.setText(String.valueOf(puntajeMano));
  }

}