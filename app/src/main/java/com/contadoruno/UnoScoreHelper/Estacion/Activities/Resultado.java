package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorResultados;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

public class Resultado extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerResultado;
  private RecyclerView.Adapter adaptadorResultado;
  private RecyclerView listaResultado;

  private Button okResultado;
  private TextView textoResultado;
  private TextView textoRondas;


  private int id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.resultado_partida);

    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();

    id = (int) getIntent().getExtras().get("id");
    Boolean empatada = (Boolean) getIntent().getExtras().get("empate");

    listaResultado = findViewById(R.id.listaResultado);
    okResultado = findViewById(R.id.finalizarResultados);
    textoResultado = findViewById(R.id.textoResultado);
    textoRondas = findViewById(R.id.textoRondas);

    if (empatada) {
      textoResultado.setText(getString(R.string.empate));
    } else {
      String ganador = pc.getGanadorPartida(id);
      textoResultado.setText(ganador + getString(R.string.gana));
    }

    textoRondas.setText(String.valueOf(pc.getRondas(id)));

    LManagerResultado = new LinearLayoutManager(Resultado.this);
    adaptadorResultado = new AdaptadorResultados(id);

    listaResultado.setLayoutManager(LManagerResultado);
    listaResultado.setAdapter(adaptadorResultado);

    okResultado.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Resultado.this, MainScreen.class);
        startActivity(intent);
        finish();
      }
    });
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(Resultado.this, MainScreen.class);
    startActivity(intent);
    finish();
  }
}