package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorJugadoresAgregados;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.ErrorAlCrearPartida;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.PuntajeVacio;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.ReglaNula;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.TamanoJugadores;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;
import java.util.List;

public class NuevaPartida extends AppCompatActivity {

  private RecyclerView.LayoutManager LManagerJugadores;
  private RecyclerView.Adapter adaptadorAddJugador;
  private RecyclerView listaJugadores;
  private Button createEdit;
  private Button btnComenzar;
  private Button btnReglas;
  private RelativeLayout layoutLista;
  private EditText editPuntaje;
  private Spinner spinnerTipoJuego;
  private TextView tituloJugadores;
  private TextView txtPuntuaIndividual;
  private TextView txtPuntuaTodos;
  private TextView txtReglas;

  private RelativeLayout layoutJugadores;
  private TextView textoSeleccionados;
  private Button classicButton;
  private Button flipButton;
  private CheckBox sinLimite;

  private final int SELECCIONAR_JUGADORES = 1;
  private final int SELECCIONAR_REGLAS = 2;
  private String reglaSeleccionada;
  private ArrayList<String> jugadoresAPintar = new ArrayList<String>();
  private String[] tipoJuego = new String[2];
  private Boolean puntuarIndividual;
  private Boolean flip = null;
  private Boolean limite = true;
  private Toolbar toolbar;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.newgame);

    layoutLista = findViewById(R.id.layoutLista);
    tituloJugadores = findViewById(R.id.tituloJugadores);
    editPuntaje = findViewById(R.id.editPuntaje);
    listaJugadores = findViewById(R.id.listaJugadoresSeleccionados);
    createEdit = findViewById(R.id.createEditJugadores);
    btnComenzar = findViewById(R.id.comenzarPartida);
    btnReglas = findViewById(R.id.chooseReglas);
    spinnerTipoJuego = findViewById(R.id.spinnerTipoJuego);
    txtPuntuaIndividual = findViewById(R.id.txtPuntuaIndividual);
    txtPuntuaTodos = findViewById(R.id.txtPuntuaTodos);
    layoutJugadores = findViewById(R.id.layoutJugadores);
    textoSeleccionados = findViewById(R.id.textoSeleccionados);
    tituloJugadores = findViewById(R.id.tituloJugadores);
    classicButton = findViewById(R.id.classicButton);
    flipButton = findViewById(R.id.flipButton);
    sinLimite = findViewById(R.id.sinLimite);
    txtReglas = findViewById(R.id.txtReglas);
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


    tipoJuego[0] = getString(R.string.juegoIndividual);
    tipoJuego[1] = getString(R.string.juegoNoIndividual);

    if (jugadoresAPintar.isEmpty()) {
      createEdit.setText(R.string.selectJugadores);
    } else {
      createEdit.setText(R.string.editarJugadores);
    }

    classicButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Toast.makeText(NuevaPartida.this, "seleccionado classic", Toast.LENGTH_SHORT).show();
        flip = false;
        reglaSeleccionada = null;
        txtReglas.setText(R.string.reglaNoSeleccionada);
        classicButton.setBackgroundResource(R.drawable.classic_button);
        flipButton.setBackgroundResource(R.drawable.gradient_grey);
      }
    });

    flipButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Toast.makeText(NuevaPartida.this, "seleccionado flip", Toast.LENGTH_SHORT).show();
        flip = true;
        reglaSeleccionada = null;
        txtReglas.setText(R.string.reglaNoSeleccionada);
        flipButton.setBackgroundResource(R.drawable.flip_button);
        classicButton.setBackgroundResource(R.drawable.gradient_grey);
      }
    });

    createEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(NuevaPartida.this, AddJugador.class);
        intent.putExtra("jugadoresSeleccionados", jugadoresAPintar);
        startActivityForResult(intent, SELECCIONAR_JUGADORES);
      }
    });

    btnReglas.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (flip != null) {
          Intent intent = new Intent(NuevaPartida.this, SeleccionarReglas.class);
          intent.putExtra("flip", flip);
          startActivityForResult(intent, SELECCIONAR_REGLAS);
        } else {
          Toast.makeText(NuevaPartida.this, getString(R.string.tipoJuegoNoSeleccionado), Toast.LENGTH_SHORT).show();
        }
      }
    });

    btnComenzar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Factory f = Factory.getInstance();
        IPartidaController pc = f.getIPartidaController();
        String[] array = new String[jugadoresAPintar.size()];
        jugadoresAPintar.toArray(array);
        String puntaje;
        if (limite) {
          puntaje = editPuntaje.getText().toString();
        } else {
          puntaje = "MAX_VALUE";
        }

        try {
          pc.crearPartida(reglaSeleccionada, array, puntaje, puntuarIndividual, flip);
          Intent intent = new Intent(NuevaPartida.this, PartidaEnJuego.class);
          intent.putExtra("id", pc.getUltimaPartida());
          startActivity(intent);
          finish();
        } catch (ErrorAlCrearPartida errorAlCrearPartida) {
          Toast.makeText(NuevaPartida.this, getString(R.string.noSeleccionaTipoJuego), Toast.LENGTH_SHORT).show();
        } catch (PuntajeVacio puntajeVacio) {
          Toast.makeText(NuevaPartida.this, getString(R.string.noSeSeleccionoPuntaje), Toast.LENGTH_SHORT).show();
        } catch (TamanoJugadores tamanoJugadores) {
          Toast.makeText(NuevaPartida.this, getString(R.string.errorTamano), Toast.LENGTH_SHORT).show();
        } catch (ReglaNula reglaNula) {
          Toast.makeText(NuevaPartida.this, getString(R.string.reglaNoSeleccionada), Toast.LENGTH_SHORT).show();
        }
      }
    });



    ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(this, R.layout.list_item, tipoJuego);
    spinnerTipoJuego.setAdapter(adapterColor);

    spinnerTipoJuego.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        String selectedItem = parentView.getItemAtPosition(position).toString();
        if (selectedItem.equals(getString(R.string.juegoIndividual))) {
          txtPuntuaIndividual.setVisibility(View.VISIBLE);
          txtPuntuaTodos.setVisibility(View.GONE);
          puntuarIndividual = true;
        }
        else{
          txtPuntuaIndividual.setVisibility(View.GONE);
          txtPuntuaTodos.setVisibility(View.VISIBLE);
          puntuarIndividual = false;
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) {
        // your code here
      }

    });

    sinLimite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          limite = false;
          editPuntaje.setFocusable(false);
          editPuntaje.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
          editPuntaje.setClickable(false);
          editPuntaje.getText().clear();
        } else {
          limite = true;
          editPuntaje.setFocusable(true);
          editPuntaje.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
          editPuntaje.setClickable(true);
        }
      }
    });

  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case SELECCIONAR_JUGADORES:
        if(resultCode == Activity.RESULT_OK){

          jugadoresAPintar = (ArrayList<String>)data.getSerializableExtra("result");
          cargarJugadoresSeleccionados(jugadoresAPintar);

          if (jugadoresAPintar.isEmpty()) {
            createEdit.setText(R.string.selectJugadores);
          } else {
            createEdit.setText(R.string.editarJugadores);
          }

        }
        if (resultCode == Activity.RESULT_CANCELED) {
          if (jugadoresAPintar.isEmpty()) {
            createEdit.setText(R.string.selectJugadores);
          } else {
            createEdit.setText(R.string.editarJugadores);
          }
        }
        break;
      case SELECCIONAR_REGLAS:
        if(resultCode == Activity.RESULT_OK) {
          reglaSeleccionada = data.getStringExtra("result");
          txtReglas.setText(reglaSeleccionada);
        }
        if (resultCode == Activity.RESULT_CANCELED) {
          //Write your code if there's no result
        }
        break;
    }
  }

  @Override
  public void onBackPressed() {
    Intent mainActivity = new Intent(NuevaPartida.this, MainScreen.class);
    startActivity(mainActivity);
    finish();
  }

  protected void cargarJugadoresSeleccionados(List<String> jugadores) {

    checkListaVacia(jugadores);

    LManagerJugadores = new LinearLayoutManager(NuevaPartida.this);
    adaptadorAddJugador = new AdaptadorJugadoresAgregados(R.layout.view_jugador_agregado, jugadores, new AdaptadorJugadoresAgregados.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {
        ((AdaptadorJugadoresAgregados)adaptadorAddJugador).getJugadores().remove(nombre);
        adaptadorAddJugador.notifyDataSetChanged();
        checkListaVacia(((AdaptadorJugadoresAgregados)adaptadorAddJugador).getJugadores());
      }
    });

    listaJugadores.setLayoutManager(LManagerJugadores);
    listaJugadores.setAdapter(adaptadorAddJugador);
    }

    protected void checkListaVacia(List<String> jugadores){
      layoutJugadores = findViewById(R.id.layoutJugadores);
      textoSeleccionados = findViewById(R.id.textoSeleccionados);
      if (!jugadores.isEmpty()) {
        tituloJugadores.setVisibility(View.VISIBLE);
        layoutLista.setVisibility(View.VISIBLE);
        textoSeleccionados.setVisibility(View.GONE);
      } else {
        textoSeleccionados.setVisibility(View.VISIBLE);
        tituloJugadores.setVisibility(View.GONE);
        layoutLista.setVisibility(View.GONE);
      }
    }
}