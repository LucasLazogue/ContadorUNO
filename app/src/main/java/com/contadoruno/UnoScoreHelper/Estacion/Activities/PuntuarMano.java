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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PuntuarMano extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerCartas, LManagerSeleccionadas;
  private RecyclerView.Adapter adaptadorCartas, adaptadorCartasSeleccionadas;
  private RecyclerView listaCartitas, listaCartitasAgregadas;

  private Button btnListo;
  private Spinner spinnerJugador;
  private Button confirmar, manual;
  private TextView nombreJugadorMano, numeroManoAgregar, puntajeManoAgregar;
  private RelativeLayout layoutColores;

  private int index;
  private String nombreJugador;
  private String nombrePuntuar;
  private int puntajeMano;
  private List<String> cartas = new ArrayList<String>();
  private List<String> seleccionadas = new ArrayList<String>();
  private Map<String, Boolean> cargaCartas = new HashMap<String, Boolean>();
  private Map<String, List<String>> cartasCargadas = new HashMap<String, List<String>>();
  private Map<String, Integer> cargaManual = new HashMap<String, Integer>();
  private Map<String, Integer> puntajeCartas = new HashMap<String, Integer>();
  private Map<String, Boolean> jugadoresClickeados = new HashMap<String, Boolean>();
  private Toolbar toolbar;
  private Toolbar toolbarBorrar;
  private LinearLayout layoutCartitasAgregadas;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.puntuarmano);

    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    IReglaController rc = f.getIReglaController();

    spinnerJugador = findViewById(R.id.spinnerJugador);

    nombreJugadorMano = findViewById(R.id.nombreJugadorMano);
    numeroManoAgregar = findViewById(R.id.numeroManoAgregar);
    puntajeManoAgregar = findViewById(R.id.puntajeManoAgregar);
    confirmar = findViewById(R.id.listoMano);
    manual = findViewById(R.id.btnEntradaManual);
    listaCartitas = findViewById(R.id.listaCartitas);
    listaCartitasAgregadas = findViewById(R.id.listaCartitasAgregadas);
    toolbar = findViewById(R.id.toolbar2);
    toolbarBorrar = findViewById(R.id.toolbar);
    toolbarBorrar.setVisibility(View.GONE);
    layoutCartitasAgregadas = findViewById(R.id.layoutCartitasAgregadas);

    toolbar.setTitle(getString(R.string.NuevaMano));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });


    index = (int) getIntent().getExtras().get("id");
    DataPartida p = pc.getDatosPartida(index);
    List<String> jugadores = pc.listarNoEliminados(index);


    if (!p.getPuntuarIndividual()){
      nombrePuntuar = (String) getIntent().getExtras().get("jugadorPuntuar");
      jugadores.remove(nombrePuntuar);
    }

    for(String j : jugadores) {
      jugadoresClickeados.put(j, false);
    }

    ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(this, R.layout.list_item, jugadores);
    spinnerJugador.setAdapter(adapterColor);

    nombreJugador = spinnerJugador.getSelectedItem().toString();

    for (String j : jugadores) {
      cargaCartas.put(j, false);
      cartasCargadas.put(j, new ArrayList<String>());
      cargaManual.put(j, 0);
      puntajeCartas.put(j, 0);
    }
    if(!p.getPuntuarIndividual()) {
      cargaCartas.put(nombrePuntuar, false);
      cartasCargadas.put(nombrePuntuar, new ArrayList<String>());
      cargaManual.put(nombrePuntuar, 0);
      puntajeCartas.put(nombrePuntuar, 0);
    }

    Boolean soloWilds = rc.soloWilds(pc.getRegla(index).getNombre());
    Boolean flip = p.getFlip();

    LManagerCartas = new GridLayoutManager(this, 5);
    adaptadorCartas = new AdaptadorSeleccionarCartas(flip, soloWilds, new AdaptadorSeleccionarCartas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        List<String> cartasJugador = cartasCargadas.get(nombreJugador);
        cartasJugador.add(nombre);
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).setCartas(cartasJugador);
        adaptadorCartasSeleccionadas.notifyDataSetChanged();
        cargaManual.put(nombreJugador, 0);
        actualizarPuntaje(nombre);
        cargaCartas.put(nombreJugador, true);
        layoutCartitasAgregadas.setVisibility(View.VISIBLE);
      }
    });

    listaCartitas.setLayoutManager(LManagerCartas);
    listaCartitas.setAdapter(adaptadorCartas);

    LManagerSeleccionadas = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    adaptadorCartasSeleccionadas = new AdaptadorCartasJugadas(R.layout.view_cartas_jugadas, cartasCargadas.get(nombreJugador), new AdaptadorCartasJugadas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        List<String> cartasJugador = cartasCargadas.get(nombreJugador);
        cartasJugador.remove(nombre);
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).setCartas(cartasJugador);
        adaptadorCartasSeleccionadas.notifyDataSetChanged();
        if (cartasJugador.isEmpty()) {
          cargaCartas.put(nombreJugador, false);
          layoutCartitasAgregadas.setVisibility(View.GONE);
        }
        restarPuntuaje(nombre);
      }
    });

    listaCartitasAgregadas.setLayoutManager(LManagerSeleccionadas);
    listaCartitasAgregadas.setAdapter(adaptadorCartasSeleccionadas);

    index = (int) getIntent().getExtras().get("id");

    manual.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PuntuarMano.this);

        View alertComment = LayoutInflater.from(PuntuarMano.this).inflate(R.layout.dialog_puntajemanual, null);
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
                InputMethodManager inputMethodManager= (InputMethodManager) PuntuarMano.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(puntaje, InputMethodManager.SHOW_IMPLICIT);
              }
            });
          }
        });
        puntaje.requestFocus();

        confirmarPuntos.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (puntaje.getText().toString().equals("")) {
              Toast.makeText(PuntuarMano.this, getString(R.string.ingresarPuntajeCorrecto), Toast.LENGTH_SHORT).show();
            } else {
              cargaManual.put(nombreJugador, Integer.parseInt(puntaje.getText().toString()));
              puntajeManoAgregar.setText(String.valueOf(cargaManual.get(nombreJugador)));
              cartasCargadas.get(nombreJugador).clear();
              cargaCartas.put(nombreJugador, false);
              puntajeCartas.put(nombreJugador, Integer.parseInt(puntaje.getText().toString()));
              dialog.dismiss();
            }
          }
        });
      }
    });

    confirmar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PuntuarMano.this);

        View alertFinalizar = LayoutInflater.from(PuntuarMano.this).inflate(R.layout.dialog_confirmar_mano, null);
        builder.setView(alertFinalizar);

        final Button aceptarPuntos = alertFinalizar.findViewById(R.id.aceptarPuntos);
        final Button cancelarPuntos = alertFinalizar.findViewById(R.id.cancelarPuntos);
        final TextView helpManoDialog = alertFinalizar.findViewById(R.id.helpManoDialog);

        if (checkClickeados()) {
          helpManoDialog.setText(getString(R.string.helpConfirmarManoDialog));
        } else {
          helpManoDialog.setText(R.string.helpConfirmarManoDialogJugadoresNoClickeados);
        }

        AlertDialog confirmar = builder.create();
        confirmar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmar.show();

        aceptarPuntos.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Factory f = Factory.getInstance();
            IPartidaController pc = f.getIPartidaController();
            if (p.getPuntuarIndividual()) {
              for (Map.Entry<String, Boolean> carga : cargaCartas.entrySet()) {
                if (carga.getValue()) {
                  pc.agregarMano(index, carga.getKey(), cartasCargadas.get(carga.getKey()));
                } else {
                  pc.agregarMano(index, carga.getKey(), cargaManual.get(carga.getKey()));
                }
              }
              pc.ordenarPosiciones(index);
              Intent intent = new Intent();
              setResult(Activity.RESULT_OK, intent);
              confirmar.dismiss();
              finish();
            } else {
              int valorMano = 0;
              for (Map.Entry<String, Boolean> carga : cargaCartas.entrySet()) {
                if (!carga.getKey().equals(nombrePuntuar)) {
                  if (carga.getValue()) {
                    valorMano = valorMano + pc.valorCartas(index, cartasCargadas.get(carga.getKey()));
                  } else {
                    valorMano = valorMano + cargaManual.get(carga.getKey());
                  }
                }
              }
              for (Map.Entry<String, Boolean> carga : cargaCartas.entrySet()) {
                if (!carga.getKey().equals(nombrePuntuar)) {
                  if (carga.getValue()) {
                    pc.agregarMano(index, carga.getKey(), cartasCargadas.get(carga.getKey()), nombrePuntuar);
                  } else {
                    pc.agregarMano(index, carga.getKey(), cargaManual.get(carga.getKey()), nombrePuntuar);
                  }
                } else {
                  pc.agregarMano(index, carga.getKey(), valorMano, false);
                }
              }
              pc.ordenarPosiciones(index);
              Intent intent = new Intent();
              setResult(Activity.RESULT_OK, intent);
              confirmar.dismiss();
              finish();
            }
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

    spinnerJugador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        nombreJugador = parent.getItemAtPosition(position).toString();
        jugadoresClickeados.put(nombreJugador, true);
        DataJugador dj = pc.getDatosJugador(index, nombreJugador);
        nombreJugadorMano.setText(nombreJugador);
        numeroManoAgregar.setText(String.valueOf(dj.getManos().length + 1));
        puntajeManoAgregar.setText(String.valueOf(puntajeCartas.get(nombreJugador)));
        List<String> cartasJugador = cartasCargadas.get(nombreJugador);
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).setCartas(cartasJugador);
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).notifyDataSetChanged();

        if(cartasJugador.isEmpty()) {
          layoutCartitasAgregadas.setVisibility(View.GONE);
        } else {
          layoutCartitasAgregadas.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private Boolean checkClickeados() {
    for (Map.Entry<String, Boolean> entry : jugadoresClickeados.entrySet()) {
      if (!entry.getValue()) {
        return false;
      }
    }
    return true;
  }

  private void restarPuntuaje(String nombre) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataRegla regla = pc.getRegla(index);
    DataCarta[] cartas = regla.getCartas();
    for (int i = 0; i < cartas.length; i++) {
      if (cartas[i].getTipo().equals(nombre)) {
        //puntaje = puntaje + cartas[i].getValor();
        puntajeCartas.put(nombreJugador, puntajeCartas.get(nombreJugador) - cartas[i].getValor());
        //puntajeMano = puntajeMano + cartas[i].getValor();
        break;
      }
    }
    puntajeManoAgregar.setText(String.valueOf(puntajeCartas.get(nombreJugador)));
  }

  private void actualizarPuntaje(String nombre) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataRegla regla = pc.getRegla(index);
    DataCarta[] cartas = regla.getCartas();
    for (int i = 0; i < cartas.length; i++) {
      if (cartas[i].getTipo().equals(nombre)) {
        //puntaje = puntaje + cartas[i].getValor();
        puntajeCartas.put(nombreJugador, puntajeCartas.get(nombreJugador) + cartas[i].getValor());
        //puntajeMano = puntajeMano + cartas[i].getValor();
        break;
      }
    }
    puntajeManoAgregar.setText(String.valueOf(puntajeCartas.get(nombreJugador)));
  }
}