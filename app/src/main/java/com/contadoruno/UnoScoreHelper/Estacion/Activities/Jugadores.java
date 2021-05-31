package com.contadoruno.UnoScoreHelper.Estacion.Activities;

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

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorJugadoresTotales;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.JugadorYaExiste;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.NombreVacio;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IJugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

public class Jugadores extends AppCompatActivity {
  private RecyclerView.LayoutManager LManagerJugadores;
  private RecyclerView.Adapter adaptadorAddJugador;
  private RecyclerView listaJugadores;

  private Button crearJugador;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.jugadores);

    listaJugadores = findViewById(R.id.listaJugadoresTotales);
    crearJugador = findViewById(R.id.crearJugador);
    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(getString(R.string.Jugadores));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });



    LManagerJugadores = new LinearLayoutManager(this);
    adaptadorAddJugador = new AdaptadorJugadoresTotales(this);

    listaJugadores.setLayoutManager(LManagerJugadores);
    listaJugadores.setAdapter(adaptadorAddJugador);

    crearJugador.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Jugadores.this);

        View alertComment = LayoutInflater.from(Jugadores.this).inflate(R.layout.dialog_crearjugador, null);
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
                InputMethodManager inputMethodManager= (InputMethodManager) Jugadores.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
              Toast.makeText(Jugadores.this, getString(R.string.elJugador) + txtNombre + getString(R.string.yaExiste), Toast.LENGTH_SHORT).show();
            } catch (NombreVacio nombreVacio) {
              Toast.makeText(Jugadores.this, getString(R.string.nombreVacio), Toast.LENGTH_SHORT).show();
            }
            ((AdaptadorJugadoresTotales)adaptadorAddJugador).updateAdapter();
            dialog.dismiss();
          }
        });
      }
    });
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(Jugadores.this, MainScreen.class);
    startActivity(intent);
    finish();
  }
}