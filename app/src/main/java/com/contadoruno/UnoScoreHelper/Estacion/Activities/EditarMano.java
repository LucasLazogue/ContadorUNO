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
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataMano;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;
import java.util.List;

public class EditarMano extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerCartas, LManagerSeleccionadas;
  private RecyclerView.Adapter adaptadorCartas, adaptadorCartasSeleccionadas;
  private RecyclerView listaCartitas, listaCartitasAgregadas;
  private TextView nombreJugadorMano, numeroManoAgregar, puntajeManoAgregar;
  private Button confirmar, manual;



  private DataMano manoEditar;
  private String nombreJugador;
  private int id;
  private Boolean cargaCartas;
  private int puntajeMano;
  private int puntajeManual;
  private int puntajeCartas;
  private int numeroMano;
  private List<String> seleccionadas = new ArrayList<String>();
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.nuevamano);
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    IReglaController rc = f.getIReglaController();

    listaCartitas = findViewById(R.id.listaCartitas);
    listaCartitasAgregadas = findViewById(R.id.listaCartitasAgregadas);
    nombreJugadorMano = findViewById(R.id.nombreJugadorMano);
    numeroManoAgregar = findViewById(R.id.numeroManoAgregar);
    puntajeManoAgregar = findViewById(R.id.puntajeManoAgregar);
    puntajeManoAgregar = findViewById(R.id.puntajeManoAgregar);
    confirmar = findViewById(R.id.listoMano);
    manual = findViewById(R.id.btnEntradaManual);
    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(getString(R.string.EditarMano));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    manoEditar = (DataMano) getIntent().getExtras().get("mano");
    nombreJugador = (String) getIntent().getExtras().get("nombre");
    id = (int) getIntent().getExtras().get("id");
    numeroMano = (int) getIntent().getExtras().get("numeroMano");

    DataPartida p = pc.getDatosPartida(id);
    if (p.getPuntuarIndividual()) {
      puntajeMano = manoEditar.getValor();
    } else {
      puntajeMano = manoEditar.getPuntajeAgregado();
    }

    cargaCartas = manoEditar.getCartas().length != 0;

    nombreJugadorMano.setText(nombreJugador);
    numeroManoAgregar.setText(String.valueOf(numeroMano));
    puntajeManoAgregar.setText(String.valueOf(puntajeMano));

    Boolean soloWilds = rc.soloWilds(pc.getRegla(id).getNombre());
    Boolean flip = p.getFlip();

    DataCarta[] cartas = manoEditar.getCartas();
    if (cartas.length != 0) {
      for (DataCarta c : cartas) {
        seleccionadas.add(c.getTipo());
      }
      puntajeCartas = pc.valorCartas(id, seleccionadas);
    }

    LManagerCartas = new GridLayoutManager(this, 5);
    adaptadorCartas = new AdaptadorSeleccionarCartas(flip, soloWilds, new AdaptadorSeleccionarCartas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).getCartas().add(nombre);
        adaptadorCartasSeleccionadas.notifyDataSetChanged();
        actualizarPuntaje(nombre);
        cargaCartas = true;
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
        }
      }
    });

    listaCartitasAgregadas.setLayoutManager(LManagerSeleccionadas);
    listaCartitasAgregadas.setAdapter(adaptadorCartasSeleccionadas);

    manual.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarMano.this);

        View alertComment = LayoutInflater.from(EditarMano.this).inflate(R.layout.dialog_puntajemanual, null);
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
                InputMethodManager inputMethodManager= (InputMethodManager) EditarMano.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
            puntajeCartas = 0;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(EditarMano.this);

        View alertFinalizar = LayoutInflater.from(EditarMano.this).inflate(R.layout.dialog_confirmar_mano, null);
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
              if (p.getPuntuarIndividual()) {
                pc.editarMano(id, nombreJugador, seleccionadas, numeroMano - 1);
              } else {
                //LA PARTE DE AYUDA ES PARA HACER LA FUNCION EDITAR MANO CUANDO SE LE CARGAN TODOS LOS PUNTOS A UN SOLO JUGADOR
                pc.editarMano(id, nombreJugador, seleccionadas, numeroMano - 1, true);
              }
            } else {
              if(p.getPuntuarIndividual()) {
                pc.editarMano(id, nombreJugador, puntajeManual, numeroMano - 1);
              } else {
                pc.editarMano(id, nombreJugador, puntajeManual, numeroMano - 1, true);
              }
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
        puntajeCartas = puntajeCartas + cartas[i].getValor();
        break;
      }
    }
    puntajeManoAgregar.setText(String.valueOf(puntajeCartas));
  }

  private void restarPuntuaje(String nombre) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataRegla regla = pc.getRegla(id);
    DataCarta[] cartas = regla.getCartas();
    for (int i = 0; i < cartas.length; i++) {
      if (cartas[i].getTipo().equals(nombre)) {
        puntajeCartas = puntajeCartas - cartas[i].getValor();
        break;
      }
    }
    puntajeManoAgregar.setText(String.valueOf(puntajeCartas));
  }
}