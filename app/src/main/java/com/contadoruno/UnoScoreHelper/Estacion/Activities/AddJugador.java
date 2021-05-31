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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorAddJugador;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.JugadorYaExiste;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.NombreVacio;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IJugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;

public class AddJugador extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerJugadores;
  private RecyclerView.Adapter adaptadorAddJugador;
  private RecyclerView listaJugadores;
  private ArrayList<String> jugadoresAPintar;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.addjugador);

    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(getString(R.string.AgregarJugador));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    jugadoresAPintar = (ArrayList<String>) getIntent().getExtras().get("jugadoresSeleccionados");

    listaJugadores = findViewById(R.id.listaJugadores);

    LManagerJugadores = new LinearLayoutManager(this);
    adaptadorAddJugador = new AdaptadorAddJugador(R.layout.view_jugador_seleccionar, jugadoresAPintar, this);

    listaJugadores.setLayoutManager(LManagerJugadores);
    listaJugadores.setAdapter(adaptadorAddJugador);
    //listaJugadores.addItemDecoration(new DividerItemDecoration(listaJugadores.getContext(), DividerItemDecoration.VERTICAL));


    Button btnCrear = findViewById(R.id.btnCrearJugador);
    btnCrear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddJugador.this);

        View alertComment = LayoutInflater.from(AddJugador.this).inflate(R.layout.dialog_crearjugador, null);
        builder.setView(alertComment);
        final EditText comment = alertComment.findViewById(R.id.nombreACrear);
        final Button btnCrear = alertComment.findViewById(R.id.confirmarJugadorNuevo);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            comment.post(new Runnable() {
              @Override
              public void run() {
                InputMethodManager inputMethodManager= (InputMethodManager) AddJugador.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(comment, InputMethodManager.SHOW_IMPLICIT);
              }
            });
          }
        });
        comment.requestFocus();

        btnCrear.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Factory f = Factory.getInstance();
            IJugadorController jc = f.getIJugadorController();
            String txtNombre = comment.getText().toString();
            try {
              jc.crearJugador(txtNombre);
            } catch (JugadorYaExiste jugadorYaExiste) {
              Toast.makeText(AddJugador.this, getString(R.string.elJugador) + txtNombre + getString(R.string.yaExiste), Toast.LENGTH_SHORT).show();
            } catch (NombreVacio nombreVacio) {
              Toast.makeText(AddJugador.this, getString(R.string.nombreVacio), Toast.LENGTH_SHORT).show();
            }
            ((AdaptadorAddJugador)adaptadorAddJugador).updateAdapter();
            dialog.dismiss();
          }
        });
      }
    });

    Button btnListo = findViewById(R.id.btnConfirmarJugadores);
    btnListo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent returnIntent = new Intent();
        ArrayList sJugadores = (ArrayList) ((AdaptadorAddJugador)adaptadorAddJugador).getSeleccionados();
        returnIntent.putExtra("result", sJugadores);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
      }
    });

  }
}