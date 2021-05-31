package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorManoJugador;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataJugador;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataMano;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

public class VistaDetalleJugadorPartida extends AppCompatActivity {
  private TextView nombreJugador;
  private TextView puntajeJugador;
  private RecyclerView listaManos;
  private RecyclerView.Adapter adaptadorListaManos;
  private LinearLayoutManager LManagerManos;
  private Toolbar toolbar;

  private Boolean iconos = false;
  private int id;
  private String nombre;
  private DataPartida part;

  private static final int MANO_INDIVIDUAL = 1;
  private static final int EDIAR_MANO = 3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.detallejugador);

    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    nombre = (String) getIntent().getExtras().get("jugador");
    id = (int) getIntent().getExtras().get("id");

    part = pc.getDatosPartida(id);

    DataJugador p = pc.getDatosJugador(id, nombre);
    DataMano[] manos = p.getManos();
    int puntaje = p.getPuntaje();

    nombreJugador = findViewById(R.id.nombreJugadorEnPartida);
    puntajeJugador = findViewById(R.id.txtPuntos);
    listaManos = findViewById(R.id.listaManos);
    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(R.string.DetalleJugador);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    adaptadorListaManos = new AdaptadorManoJugador(R.layout.view_mano_jugador, manos, nombre, this, false, id, puntajeJugador);
    LManagerManos = new LinearLayoutManager(this);

    listaManos.setLayoutManager(this.LManagerManos);
    listaManos.setAdapter(adaptadorListaManos);

    nombreJugador.setText(nombre);
    puntajeJugador.setText(String.valueOf(puntaje));
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode){
      case MANO_INDIVIDUAL:
        if (resultCode == RESULT_OK){
          Factory f = Factory.getInstance();
          IPartidaController pc = f.getIPartidaController();

          DataJugador p = pc.getDatosJugador(id, nombre);
          DataMano[] manos = p.getManos();
          ((AdaptadorManoJugador)adaptadorListaManos).updateAdapter(manos);
        }
        break;
      case 3:
        if (resultCode == RESULT_OK){
          Factory f = Factory.getInstance();
          IPartidaController pc = f.getIPartidaController();

          DataJugador p = pc.getDatosJugador(id, nombre);
          DataMano[] manos = p.getManos();
          ((AdaptadorManoJugador)adaptadorListaManos).updateAdapter(manos);
          puntajeJugador.setText(String.valueOf(p.getPuntaje()));
        }
    }
  }

  /*@Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_detalle_jugador, menu);
    return true;
  }*/

  /*@Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.addMano:
        if (part.getPuntuarIndividual()) {
          Intent intent = new Intent(VistaDetalleJugadorPartida.this, NuevaMano.class);
          intent.putExtra("id", id);
          intent.putExtra("jugador", nombre);
          startActivityForResult(intent, MANO_INDIVIDUAL);
        }
        else {
          Toast.makeText(VistaDetalleJugadorPartida.this, getString(R.string.exceptManoJuegoIndividual), Toast.LENGTH_SHORT).show();
        }
        return true;
      case R.id.editarManoJugador:
        iconos = !iconos;
        ((AdaptadorManoJugador)adaptadorListaManos).setIconos(iconos);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }*/

}