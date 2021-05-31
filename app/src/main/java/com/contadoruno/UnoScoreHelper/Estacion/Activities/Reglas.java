package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorReglas;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

public class Reglas extends AppCompatActivity {
  private RecyclerView.LayoutManager LManagerReglas;
  private RecyclerView.Adapter adaptadorReglas;
  private RecyclerView listaReglas;

  private Button crearRegla;

  private static final int EDITAR_MANO = 1;
  private static final int CREAR_REGLA = 2;

  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.reglas);

    Factory f = Factory.getInstance();
    IReglaController rc = f.getIReglaController();

    crearRegla = findViewById(R.id.newRegla);
    listaReglas = findViewById(R.id.listaReglas);
    toolbar = findViewById(R.id.toolbar);


    toolbar.setTitle(getString(R.string.Valores));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    LManagerReglas = new LinearLayoutManager(Reglas.this);
    adaptadorReglas = new AdaptadorReglas(Reglas.this, R.layout.view_reglas_creadas, null,true, new AdaptadorReglas.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        DataRegla regla = rc.getDatosRegla(nombre);
        if (regla.getEditable()) {
          Intent intent = new Intent(Reglas.this, EditarRegla.class);
          intent.putExtra("regla", regla);
          startActivityForResult(intent, EDITAR_MANO);
        }
      }
    });

    listaReglas.setLayoutManager(LManagerReglas);
    listaReglas.setAdapter(adaptadorReglas);

    crearRegla.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Reglas.this);


        View alertComment = LayoutInflater.from(Reglas.this).inflate(R.layout.dialog_crearregla, null);
        builder.setView(alertComment);

        final EditText comment = alertComment.findViewById(R.id.nombreReglaNueva);
        final Button btnNombre = alertComment.findViewById(R.id.confirmRegla);
        final RadioGroup radioTipo = alertComment.findViewById(R.id.radioTipo);
        final RadioButton unoClassic = alertComment.findViewById(R.id.unoClassic);
        final RadioButton unoFlip = alertComment.findViewById(R.id.unoFlip);


        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnNombre.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Boolean flip = null;

            if (unoClassic.isChecked()) {
              flip = false;
            } else {
              if (unoFlip.isChecked()) {
                flip = true;
              }
            }

            String txtNombre = comment.getText().toString();
            if (flip == null || txtNombre.equals("")) {
              if (flip == null) {
                Toast.makeText(Reglas.this, getString(R.string.tipoJuegoNoSeleccionado), Toast.LENGTH_SHORT).show();
              } else {
                Toast.makeText(Reglas.this, getString(R.string.nombreReglaVacio), Toast.LENGTH_SHORT).show();
              }
            } else {
              dialog.dismiss();
              Intent intent = new Intent(Reglas.this, NuevaRegla.class);
              intent.putExtra("nombreRegla", txtNombre);
              intent.putExtra("flip", flip);
              startActivityForResult(intent, CREAR_REGLA);
            }
          }
        });

      }
    });
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(Reglas.this, MainScreen.class);
    startActivity(intent);
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case EDITAR_MANO:
        if (resultCode == RESULT_OK) {
          ((AdaptadorReglas)adaptadorReglas).updateAdapter();
          break;
        }
      case CREAR_REGLA:
        if (resultCode == RESULT_OK) {
          ((AdaptadorReglas)adaptadorReglas).updateAdapter();
          break;
        }
    }
  }
}