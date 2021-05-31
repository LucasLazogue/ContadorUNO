package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorReglas;
import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorSelectRegla;
import com.contadoruno.UnoScoreHelper.R;


public class SeleccionarReglas extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerReglas;
  private RecyclerView.Adapter adaptadorSeleccionarReglas;
  private RecyclerView listaReglas;

  private Boolean flip;

  private static final int CREAR_REGLAS = 0;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.selectreglas);

    listaReglas = findViewById(R.id.selectReglas);
    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(getString(R.string.SeleccionarValores));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    flip = (Boolean) getIntent().getExtras().get("flip");


    LManagerReglas = new LinearLayoutManager(this);
    adaptadorSeleccionarReglas = new AdaptadorReglas(SeleccionarReglas.this, R.layout.view_reglas_creadas, flip, false, new AdaptadorReglas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        Intent returnIntent = new Intent();
        String seleccionado = nombre;
        returnIntent.putExtra("result", seleccionado);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
      }
    });

    listaReglas.setLayoutManager(LManagerReglas);
    listaReglas.setAdapter(adaptadorSeleccionarReglas);

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case CREAR_REGLAS:
        if (resultCode == Activity.RESULT_OK) {
          adaptadorSeleccionarReglas = new AdaptadorSelectRegla(R.layout.view_select_reglas);
          listaReglas.setAdapter(adaptadorSeleccionarReglas);
        }
        if (resultCode == Activity.RESULT_CANCELED) {
          //Write your code if there's no result
        }
        break;

    }
  }
}