package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorHistorialPartidas;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

public class Historial extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerPartidas;
  private RecyclerView.Adapter adaptadorListaPartidas;
  private RecyclerView listaPartidas;
  private Spinner spinnerTipo;

  private Boolean historial;

  private AdaptadorHistorialPartidas.OnItemClickListener listener = null;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.historial);
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();

    listaPartidas = findViewById(R.id.historialPartidas);
    spinnerTipo = findViewById(R.id.spinnerTipo);
    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(getString(R.string.NuevaPartida));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    historial = (Boolean) getIntent().getExtras().get("historial");

    LManagerPartidas = new LinearLayoutManager(Historial.this);

    AdaptadorHistorialPartidas.OnItemClickListener listener = new AdaptadorHistorialPartidas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(int index) {
        Intent intent = new Intent(Historial.this, PartidaEnJuego.class);
        intent.putExtra("id", index);
        pc.marcarPartidaCargada(index);
        startActivity(intent);
        finish();
      }
    };

    if (!historial){
      adaptadorListaPartidas = new AdaptadorHistorialPartidas(R.layout.view_partida_jugada, historial, listener, "All", this);
    } else {
      adaptadorListaPartidas = new AdaptadorHistorialPartidas(R.layout.view_partida_historial, historial, null, "All", this);
      DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listaPartidas.getContext(), DividerItemDecoration.VERTICAL);
      listaPartidas.addItemDecoration(dividerItemDecoration);
    }

    listaPartidas.setLayoutManager(LManagerPartidas);
    listaPartidas.setAdapter(adaptadorListaPartidas);

    String[] tipoJuego = new String[3];
    tipoJuego[0] = "All";
    tipoJuego[1] = "Uno Classic";
    tipoJuego[2] = "Uno Flip";

    ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(this, R.layout.list_item, tipoJuego);
    spinnerTipo.setAdapter(adapterColor);

    spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        String tipo = parentView.getItemAtPosition(position).toString();
        ((AdaptadorHistorialPartidas)adaptadorListaPartidas).updateAdapter(tipo);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) {
        // your code here
      }

    });

  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(Historial.this, MainScreen.class);
    startActivity(intent);
    finish();
  }
}