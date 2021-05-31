package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorCartasJugadas;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataCarta;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataMano;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;
import java.util.List;

public class CartasJugadas extends AppCompatActivity {

  private RecyclerView listaCartas;
  private RecyclerView.Adapter adaptadorListaCartas;
  private LinearLayoutManager LManagerListaCartas;
  private TextView nombre;
  private TextView valor;
  private TextView jugadorGanadorMano;
  private int id;
  private LinearLayout ganadorMano, haGanadoMano;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.manojugada);

    listaCartas = findViewById(R.id.listaCartas);
    this.valor = findViewById(R.id.puntajecitoMano);
    this.nombre = findViewById(R.id.jugadorcitoMano);
    this.ganadorMano = findViewById(R.id.sumadoA);
    this.jugadorGanadorMano = findViewById(R.id.ganadorMano);
    this.haGanadoMano = findViewById(R.id.haGanadoMano);

    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(getString(R.string.CartasJugadas));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();

    DataCarta[] data = (DataCarta[]) getIntent().getExtras().get("cartas");
    String nombreJugador = (String) getIntent().getExtras().get("nombreJugador");
    Integer valorMano = (Integer) getIntent().getExtras().get("valorMano");
    DataMano mano = (DataMano) getIntent().getExtras().get("mano");
    id = (int) getIntent().getExtras().get("id");

    DataPartida p = pc.getDatosPartida(id);

    List<String> cartas = new ArrayList<String>();
    for(int i = 0; i < data.length; i++){
      cartas.add(data[i].getTipo());
    }

    this.nombre.setText(nombreJugador);


    if (p.getPuntuarIndividual()) {
      this.valor.setText(String.valueOf(valorMano));
      ganadorMano.setVisibility(View.GONE);
      haGanadoMano.setVisibility(View.GONE);
    } else {
      if(!mano.getPunteaJugador().equals(nombreJugador)) {
        this.valor.setText(String.valueOf(mano.getPuntajeAgregado()));
        ganadorMano.setVisibility(View.VISIBLE);
        haGanadoMano.setVisibility(View.GONE);
        this.jugadorGanadorMano.setText(mano.getPunteaJugador());
      } else {
        this.valor.setText(String.valueOf(valorMano));
        ganadorMano.setVisibility(View.GONE);
        haGanadoMano.setVisibility(View.VISIBLE);
      }
    }

    adaptadorListaCartas = new AdaptadorCartasJugadas(R.layout.view_cartas_jugadas, cartas, new AdaptadorCartasJugadas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {

      }
    });
    LManagerListaCartas = new GridLayoutManager(this, 5);

    listaCartas.setLayoutManager(this.LManagerListaCartas);
    listaCartas.setAdapter(adaptadorListaCartas);

  }
}