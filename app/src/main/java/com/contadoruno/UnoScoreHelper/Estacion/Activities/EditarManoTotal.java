package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorCartasJugadas;
import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorSeleccionarCartas;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataCarta;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataJugador;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataMano;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditarManoTotal extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerCartas, LManagerSeleccionadas;
  private RecyclerView.Adapter adaptadorCartas, adaptadorCartasSeleccionadas;
  private RecyclerView listaCartitas, listaCartitasAgregadas;

  private Button btnListo;
  private Spinner spinnerJugador;
  private Button confirmar, manual;
  private TextView nombreJugadorMano, numeroManoAgregar, puntajeManoAgregar;

  private int index;
  private int numeroMano;
  private List<String> jugadoresMano;
  private String nombreJugador;

  private List<String> cartas = new ArrayList<String>();
  private List<String> seleccionadas = new ArrayList<String>();
  private Map<String, Boolean> cargaCartas = new HashMap<String, Boolean>();
  private Map<String, List<String>> cartasCargadas = new HashMap<String, List<String>>();
  private Map<String, Integer> cargaManual = new HashMap<String, Integer>();
  private Map<String, Integer> puntajeCartas = new HashMap<String, Integer>();
  private Map<String, Boolean> jugadoresClickeados = new HashMap<String, Boolean>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.puntuarmano);

    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();

    index = (int) getIntent().getExtras().get("id");
    numeroMano = (int) getIntent().getExtras().get("mano");

    spinnerJugador = findViewById(R.id.spinnerJugador);
    nombreJugadorMano = findViewById(R.id.nombreJugadorMano);
    numeroManoAgregar = findViewById(R.id.numeroManoAgregar);
    puntajeManoAgregar = findViewById(R.id.puntajeManoAgregar);
    confirmar = findViewById(R.id.listoMano);
    manual = findViewById(R.id.btnEntradaManual);
    listaCartitas = findViewById(R.id.listaCartitas);
    listaCartitasAgregadas = findViewById(R.id.listaCartitasAgregadas);

    jugadoresMano = pc.getJugadoresMano(index, numeroMano);
    DataPartida p = pc.getDatosPartida(index);

    ArrayAdapter<String> adaptadorJugadores = new ArrayAdapter<String>(this, R.layout.list_item, jugadoresMano);
    spinnerJugador.setAdapter(adaptadorJugadores);
    nombreJugador = spinnerJugador.getSelectedItem().toString();

    for (String s : jugadoresMano) {
      DataJugador dataJ = pc.getDatosJugador(index, s);
      DataMano mano = dataJ.getManos()[numeroMano - 1];
      cartasCargadas.put(s, new ArrayList<String>());
      if (mano.getCartas() != null && mano.getCartas().length != 0) {
        cargaCartas.put(s, true);
        puntajeCartas.put(s, mano.getValor());
        for (DataCarta c : mano.getCartas()) {
          cartasCargadas.get(s).add(c.getTipo());
        }
      } else {
        cargaCartas.put(s, false);
        cargaManual.put(s, mano.getValor());
        puntajeCartas.put(s, mano.getValor());
      }
    }

    LManagerCartas = new GridLayoutManager(this, 5);
    adaptadorCartas = new AdaptadorSeleccionarCartas(p.getFlip(), true, new AdaptadorSeleccionarCartas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).getCartas().add(nombre);
        adaptadorCartasSeleccionadas.notifyDataSetChanged();
        actualizarPuntaje(nombre);
        cargaCartas.put(nombreJugador, true);
      }
    });

    listaCartitas.setLayoutManager(LManagerCartas);
    listaCartitas.setAdapter(adaptadorCartas);

    LManagerSeleccionadas = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    adaptadorCartasSeleccionadas = new AdaptadorCartasJugadas(R.layout.view_cartas_jugadas, cartasCargadas.get(nombreJugador), new AdaptadorCartasJugadas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        cartasCargadas.get(nombreJugador).remove(nombre);
        ((AdaptadorCartasJugadas)adaptadorCartasSeleccionadas).setCartas(cartasCargadas.get(nombreJugador));
        adaptadorCartasSeleccionadas.notifyDataSetChanged();
        //restarPuntuaje(nombre);
        if (cartasCargadas.get(nombreJugador).isEmpty()) {
          cargaCartas.put(nombreJugador, false);
        }
      }
    });

    listaCartitasAgregadas.setLayoutManager(LManagerSeleccionadas);
    listaCartitasAgregadas.setAdapter(adaptadorCartasSeleccionadas);



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
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

  }

  private void actualizarPuntaje(String nombre) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataRegla regla = pc.getRegla(index);
    DataCarta[] cartas = regla.getCartas();
    for (int i = 0; i < cartas.length; i++) {
      if (cartas[i].getTipo().equals(nombre)) {
        puntajeCartas.put(nombre, puntajeCartas.get(nombre) + cartas[i].getValor());
        //puntajeCartas = puntajeCartas + cartas[i].getValor();
        break;
      }
    }
    puntajeManoAgregar.setText(String.valueOf(puntajeCartas));
  }

  private void restarPuntuaje(String nombre) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataRegla regla = pc.getRegla(index);
    DataCarta[] cartas = regla.getCartas();
    for (int i = 0; i < cartas.length; i++) {
      if (cartas[i].getTipo().equals(nombre)) {
        puntajeCartas.put(nombre, puntajeCartas.get(nombre) - cartas[i].getValor());
        break;
      }
    }
    puntajeManoAgregar.setText(String.valueOf(puntajeCartas));
  }
}