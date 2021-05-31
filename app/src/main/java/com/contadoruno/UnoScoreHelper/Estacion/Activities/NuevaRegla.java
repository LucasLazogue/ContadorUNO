package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorCartasRegla;
import com.contadoruno.UnoScoreHelper.Logica.Enums.Classic;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.ReglaYaExiste;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NuevaRegla extends AppCompatActivity {

  private Map<String, Integer> cartasAgregadas;

  private List<String> cartas = new ArrayList<String>();
  private List<Integer> valorCartas = new ArrayList<Integer>();
  private String nombreRegla;
  private Boolean flip;

  private Button cancelarRegla, aceptarRegla;
  private TextView nombreReglaCreando;

  private RecyclerView.LayoutManager LManagerCartas;
  private RecyclerView.Adapter adaptadorListaCartas;
  private RecyclerView listaCartas;

  private static final int AGREGAR_CARTAS = 1;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.nueva_regla);
    listaCartas = findViewById(R.id.cartasAgregadas);
    nombreReglaCreando = findViewById(R.id.nombreReglaCreando);
    aceptarRegla = findViewById(R.id.aceptarRegla);
    cancelarRegla = findViewById(R.id.cancelarRegla);
    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(getString(R.string.NuevaRegla));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    nombreRegla = getIntent().getStringExtra("nombreRegla");
    flip = (Boolean) getIntent().getExtras().get("flip");
    nombreReglaCreando.setText(nombreRegla);

    Factory f = Factory.getInstance();
    IReglaController rc = f.getIReglaController();

    cartasAgregadas = cargarReglas(flip);

    LManagerCartas = new LinearLayoutManager(this);
    adaptadorListaCartas = new AdaptadorCartasRegla(cartas, valorCartas, new AdaptadorCartasRegla.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NuevaRegla.this);

        View alertComment = LayoutInflater.from(NuevaRegla.this).inflate(R.layout.dialog_edit_puntos_carta, null);
        builder.setView(alertComment);

        final EditText puntaje = alertComment.findViewById(R.id.nuevoPuntaje);
        final Button confirmarPuntos = alertComment.findViewById(R.id.confirmarEditarPuntaje);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        confirmarPuntos.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (puntaje.getText().equals("")) {
              Toast.makeText(NuevaRegla.this, getString(R.string.puntajeNoIngresado), Toast.LENGTH_SHORT).show();
            } else {
              valorCartas.set(position, Integer.parseInt(puntaje.getText().toString()));
              ((AdaptadorCartasRegla)adaptadorListaCartas).updateAdapter(valorCartas);
              cartasAgregadas.put(nombre, Integer.parseInt(puntaje.getText().toString()));
              dialog.dismiss();
            }
          }
        });
      }
    });

    listaCartas.setLayoutManager(LManagerCartas);
    listaCartas.setAdapter(adaptadorListaCartas);

    aceptarRegla.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Factory f = Factory.getInstance();
        IReglaController rc = f.getIReglaController();
        try {
          rc.altaRegla(nombreRegla, flip, cartasAgregadas);
          Intent returnIntent = new Intent();
          setResult(Activity.RESULT_OK, returnIntent);
          finish();
        } catch (ReglaYaExiste reglaYaExiste) {
          reglaYaExiste.printStackTrace();
        }
      }
    });

    cancelarRegla.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cartas.clear();
        valorCartas.clear();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
      }
    });

  }

  private Map<String, Integer> cargarReglas(Boolean flip) {
    Factory f = Factory.getInstance();
    IReglaController rc = f.getIReglaController();
    Map<String, Integer> res;
    if (flip) {
      res = rc.reglasFlip();
    } else {
      res = rc.reglasClasicas();
    }

    if (!flip) {
      cartas.add(Classic.CEROR.toString());
      cartas.add(Classic.UNOR.toString());
      cartas.add(Classic.DOSR.toString());
      cartas.add(Classic.TRESR.toString());
      cartas.add(Classic.CUATROR.toString());
      cartas.add(Classic.CINCOR.toString());
      cartas.add(Classic.SEISR.toString());
      cartas.add(Classic.SIETER.toString());
      cartas.add(Classic.OCHOR.toString());
      cartas.add(Classic.NUEVER.toString());
      cartas.add(Classic.SKIPR.toString());
      cartas.add(Classic.REVERSER.toString());
      cartas.add(Classic.DRAW2R.toString());
      cartas.add(Classic.WILD.toString());
      cartas.add(Classic.WILD4.toString());

      valorCartas.add(0);
      valorCartas.add(1);
      valorCartas.add(2);
      valorCartas.add(3);
      valorCartas.add(4);
      valorCartas.add(5);
      valorCartas.add(6);
      valorCartas.add(7);
      valorCartas.add(8);
      valorCartas.add(9);
      valorCartas.add(20);
      valorCartas.add(20);
      valorCartas.add(20);
      valorCartas.add(50);
      valorCartas.add(50);
    } else {

      cartas.add(Classic.CEROR.toString());
      cartas.add(Classic.UNOR.toString());
      cartas.add(Classic.DOSR.toString());
      cartas.add(Classic.TRESR.toString());
      cartas.add(Classic.CUATROR.toString());
      cartas.add(Classic.CINCOR.toString());
      cartas.add(Classic.SEISR.toString());
      cartas.add(Classic.SIETER.toString());
      cartas.add(Classic.OCHOR.toString());
      cartas.add(Classic.NUEVER.toString());
      cartas.add(Classic.SKIPR.toString());
      cartas.add(Classic.SKIPD.toString());
      cartas.add(Classic.REVERSER.toString());
      cartas.add(Classic.DRAW1FL.toString());
      cartas.add(Classic.DRAW2R.toString());
      cartas.add(Classic.DRAW5FD.toString());
      cartas.add(Classic.REVERSED.toString());
      cartas.add(Classic.FLIPD.toString());
      cartas.add(Classic.FLIPL.toString());
      cartas.add(Classic.WILD.toString());
      cartas.add(Classic.WILD4.toString());
      cartas.add(Classic.WILDF.toString());
      cartas.add(Classic.WILDDRAW2.toString());
      cartas.add(Classic.WILDDRAWC.toString());

      valorCartas.add(0);
      valorCartas.add(1);
      valorCartas.add(2);
      valorCartas.add(3);
      valorCartas.add(4);
      valorCartas.add(5);
      valorCartas.add(6);
      valorCartas.add(7);
      valorCartas.add(8);
      valorCartas.add(9);
      valorCartas.add(20);
      valorCartas.add(30);
      valorCartas.add(20);
      valorCartas.add(10);
      valorCartas.add(20);
      valorCartas.add(20);
      valorCartas.add(20);
      valorCartas.add(20);
      valorCartas.add(20);
      valorCartas.add(50);
      valorCartas.add(50);
      valorCartas.add(40);
      valorCartas.add(50);
      valorCartas.add(60);
    }
    return res;
  }

}