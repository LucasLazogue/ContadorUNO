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
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataCarta;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Enums.Classic;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditarRegla extends AppCompatActivity {

  private TextView nombreRegla;
  private RecyclerView.LayoutManager LManagerCartas;
  private RecyclerView.Adapter adaptadorListaCartas;
  private RecyclerView listaCartas;
  private Button cancelarRegla, aceptarRegla;


  private Map<String, Integer> cartasAgregadas;
  private List<String> cartas = new ArrayList<String>();
  private List<Integer> valorCartas = new ArrayList<Integer>();

  private Boolean flip;
  private DataRegla regla;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.nueva_regla);

    nombreRegla = findViewById(R.id.nombreReglaCreando);
    listaCartas = findViewById(R.id.cartasAgregadas);
    aceptarRegla = findViewById(R.id.aceptarRegla);
    cancelarRegla = findViewById(R.id.cancelarRegla);
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

    regla = (DataRegla) getIntent().getExtras().get("regla");
    flip = regla.getFlip();

    nombreRegla.setText(regla.getNombre());
    DataCarta[] dcartas = regla.getCartas();

    cartasAgregadas = cargarReglas(dcartas, flip);

    LManagerCartas = new LinearLayoutManager(this);
    adaptadorListaCartas = new AdaptadorCartasRegla(cartas, valorCartas, new AdaptadorCartasRegla.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarRegla.this);

        View alertComment = LayoutInflater.from(EditarRegla.this).inflate(R.layout.dialog_edit_puntos_carta, null);
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
              Toast.makeText(EditarRegla.this, getString(R.string.puntajeNoIngresado), Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarRegla.this);

        View alertComment = LayoutInflater.from(EditarRegla.this).inflate(R.layout.dialog_confirmar_editar_regla, null);
        builder.setView(alertComment);

        final Button cancelarEditarValores = alertComment.findViewById(R.id.cancelarEditarValores);
        final Button aceptarEditarValores = alertComment.findViewById(R.id.aceptarEditarValores);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        cancelarEditarValores.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        });

        aceptarEditarValores.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Factory f = Factory.getInstance();
            IReglaController rc = f.getIReglaController();
            rc.editarRegla(regla.getNombre(), cartasAgregadas);
            dialog.dismiss();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
          }
        });
      }
    });

  }

  private Map<String, Integer> cargarReglas(DataCarta[] dcartas, Boolean flip) {
    Map<String, Integer> res = new HashMap<String, Integer>();
    for (DataCarta c : dcartas){
      res.put(c.getTipo(), c.getValor());
    }

    if (flip) {
      cartas.add(Classic.valueOf("CEROR").toString());
      cartas.add(Classic.valueOf("UNOR").toString());
      cartas.add(Classic.valueOf("DOSR").toString());
      cartas.add(Classic.valueOf("TRESR").toString());
      cartas.add(Classic.valueOf("CUATROR").toString());
      cartas.add(Classic.valueOf("CINCOR").toString());
      cartas.add(Classic.valueOf("SEISR").toString());
      cartas.add(Classic.valueOf("SIETER").toString());
      cartas.add(Classic.valueOf("OCHOR").toString());
      cartas.add(Classic.valueOf("NUEVER").toString());
      cartas.add(Classic.valueOf("SKIPR").toString());
      cartas.add(Classic.valueOf("SKIPD").toString());
      cartas.add(Classic.valueOf("REVERSER").toString());
      cartas.add(Classic.valueOf("DRAW1FL").toString());
      cartas.add(Classic.valueOf("DRAW2R").toString());
      cartas.add(Classic.valueOf("DRAW5FD").toString());
      cartas.add(Classic.valueOf("REVERSED").toString());
      cartas.add(Classic.valueOf("FLIPD").toString());
      cartas.add(Classic.valueOf("FLIPL").toString());
      cartas.add(Classic.valueOf("WILD").toString());
      cartas.add(Classic.valueOf("WILD4").toString());
      cartas.add(Classic.valueOf("WILDF").toString());
      cartas.add(Classic.valueOf("WILDDRAW2").toString());
      cartas.add(Classic.valueOf("WILDDRAWC").toString());

      valorCartas.add(res.get("CEROR"));
      valorCartas.add(res.get("UNOR"));
      valorCartas.add(res.get("DOSR"));
      valorCartas.add(res.get("TRESR"));
      valorCartas.add(res.get("CUATROR"));
      valorCartas.add(res.get("CINCOR"));
      valorCartas.add(res.get("SEISR"));
      valorCartas.add(res.get("SIETER"));
      valorCartas.add(res.get("OCHOR"));
      valorCartas.add(res.get("NUEVER"));
      valorCartas.add(res.get("SKIPR"));
      valorCartas.add(res.get("SKIPD"));
      valorCartas.add(res.get("REVERSER"));
      valorCartas.add(res.get("DRAW1FL"));
      valorCartas.add(res.get("DRAW2R"));
      valorCartas.add(res.get("DRAW5FD"));
      valorCartas.add(res.get("REVERSED"));
      valorCartas.add(res.get("FLIPD"));
      valorCartas.add(res.get("FLIPL"));
      valorCartas.add(res.get("WILD"));
      valorCartas.add(res.get("WILD4"));
      valorCartas.add(res.get("WILDF"));
      valorCartas.add(res.get("WILDDRAW2"));
      valorCartas.add(res.get("WILDDRAWC"));

    } else {
      cartas.add(Classic.valueOf("CEROR").toString());
      cartas.add(Classic.valueOf("UNOR").toString());
      cartas.add(Classic.valueOf("DOSR").toString());
      cartas.add(Classic.valueOf("TRESR").toString());
      cartas.add(Classic.valueOf("CUATROR").toString());
      cartas.add(Classic.valueOf("CINCOR").toString());
      cartas.add(Classic.valueOf("SEISR").toString());
      cartas.add(Classic.valueOf("SIETER").toString());
      cartas.add(Classic.valueOf("OCHOR").toString());
      cartas.add(Classic.valueOf("NUEVER").toString());
      cartas.add(Classic.valueOf("SKIPR").toString());
      cartas.add(Classic.valueOf("REVERSER").toString());
      cartas.add(Classic.valueOf("DRAW2R").toString());
      cartas.add(Classic.valueOf("WILD").toString());
      cartas.add(Classic.valueOf("WILD4").toString());

      valorCartas.add(res.get("CEROR"));
      valorCartas.add(res.get("UNOR"));
      valorCartas.add(res.get("DOSR"));
      valorCartas.add(res.get("TRESR"));
      valorCartas.add(res.get("CUATROR"));
      valorCartas.add(res.get("CINCOR"));
      valorCartas.add(res.get("SEISR"));
      valorCartas.add(res.get("SIETER"));
      valorCartas.add(res.get("OCHOR"));
      valorCartas.add(res.get("NUEVER"));
      valorCartas.add(res.get("SKIPR"));
      valorCartas.add(res.get("REVERSER"));
      valorCartas.add(res.get("DRAW2R"));
      valorCartas.add(res.get("WILD"));
      valorCartas.add(res.get("WILD4"));
    }

    return res;
  }


}