package com.contadoruno.UnoScoreHelper.Estacion.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorJugadoresEnPartidaGrid;
import com.contadoruno.UnoScoreHelper.Estacion.Adapters.AdaptadorJugadoresEnPartidaList;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PartidaEnJuego extends AppCompatActivity {

  private LinearLayoutManager LManagerJugadores;
  private LinearLayoutManager LManagerJugadoresList;

  private RecyclerView.Adapter adaptadorAddJugador;
  private RecyclerView.Adapter adaptadorAddJugadorList;

  private RecyclerView listaJugadores;
  private TextView txtReglas, txtPuntaje;
  private Button agregarManoTodos, finalizarPartida, reiniciarPartida, editarManopla;
  private Toolbar toolbar;
  private Drawable horizontalDivider, verticalDivider;
  private GridDividerItemDecoration separadores;

  private InterstitialAd mInterstitialAd;


  private Boolean viewLista = true;
  private int id;
  private int eliminados = 0;

  private final int AGREGAR_MANO = 1;
  private final int AGREGAR_MANO_A_TODOS = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.game);
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();

    listaJugadores = findViewById(R.id.jugadoresEnPartida);
    agregarManoTodos = findViewById(R.id.agregarManoTodos);
    finalizarPartida = findViewById(R.id.finalizarPartida);
    toolbar = findViewById(R.id.toolbar);
    txtReglas = findViewById(R.id.reglasEnJuego);
    txtPuntaje = findViewById(R.id.txtPuntaje);
    reiniciarPartida = findViewById(R.id.reiniciarPartida);
    editarManopla = findViewById(R.id.editarManopla);

    toolbar.setTitle(getString(R.string.PartidaEnJuego));
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mInterstitialAd = new InterstitialAd(this);
    mInterstitialAd.loadAd(new AdRequest.Builder().build());


    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });


    id = (int) getIntent().getExtras().get("id");

    DataPartida p = pc.getDatosPartida(id);
    String[] jgs = p.getJugadores();

    txtReglas.setText(p.getReglas());
    if (p.getPuntos() == Integer.MAX_VALUE) {
      txtPuntaje.setText(R.string.sinLimite);
    } else {
      txtPuntaje.setText(String.valueOf(p.getPuntos()));
    }

    if (jgs.length >= 3) {
      LManagerJugadores = new GridLayoutManager(this, 3);
    } else {
      LManagerJugadores = new GridLayoutManager(this, jgs.length);
    }

      horizontalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
      verticalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);

    if (jgs.length >= 3) {
      separadores = new GridDividerItemDecoration(horizontalDivider, verticalDivider, 3);
    } else {
      separadores = new GridDividerItemDecoration(horizontalDivider, verticalDivider, jgs.length);
    }

    if (p.getRondas() > 0) {
      editarManopla.setVisibility(View.VISIBLE);
    } else {
      editarManopla.setVisibility(View.GONE);
    }

    adaptadorAddJugador = new AdaptadorJugadoresEnPartidaGrid(id, R.layout.view_jugador_partida_grid, Arrays.asList(jgs), PartidaEnJuego.this);

    List<String> posiciones = pc.getTodasPosiciones(id);
    /*List<String> aux = new ArrayList<String>();
    for (Map.Entry<Integer, String> entry : posiciones.entrySet()) {
      aux.add(entry.getValue());
    }*/

    LManagerJugadoresList = new LinearLayoutManager(PartidaEnJuego.this);
    adaptadorAddJugadorList = new AdaptadorJugadoresEnPartidaList(id, R.layout.view_jugador_partida_list, /*posiciones*/Arrays.asList(jgs), PartidaEnJuego.this, new AdaptadorJugadoresEnPartidaList.OnItemClickListener() {
      @Override
      public void OnItemClickListener(String nombre) {

      }
    });

    listaJugadores.setHasFixedSize(true);
    listaJugadores.addItemDecoration(new DividerItemDecoration(this,
      DividerItemDecoration.HORIZONTAL));
    listaJugadores.addItemDecoration(new DividerItemDecoration(this,
      DividerItemDecoration.VERTICAL));

    listaJugadores.setLayoutManager(LManagerJugadores);
    listaJugadores.setAdapter(adaptadorAddJugador);

    agregarManoTodos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(PartidaEnJuego.this, PuntuarMano.class);
        intent.putExtra("id", id);
        if (!p.getPuntuarIndividual()) {
          String[] nombrePuntuar = new String[1];

          AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);
          View alertPuntuar = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_agregar_mano, null);
          builder.setView(alertPuntuar);

          final Button cancelarMano = alertPuntuar.findViewById(R.id.cancelarMano);
          final Button aceptarMano = alertPuntuar.findViewById(R.id.aceptarMano);
          final Spinner spinnerJugadoresEnJuego = alertPuntuar.findViewById(R.id.spinnerJugadoresEnJuego);

          ArrayAdapter<String> adapterJugadores = new ArrayAdapter<String>(PartidaEnJuego.this, R.layout.list_item, pc.listarNoEliminados(id));
          spinnerJugadoresEnJuego.setAdapter(adapterJugadores);

          AlertDialog agregarMano = builder.create();
          agregarMano.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          agregarMano.show();
          spinnerJugadoresEnJuego.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
              nombrePuntuar[0] = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
              // your code here
            }
          });

          aceptarMano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              intent.putExtra("jugadorPuntuar", nombrePuntuar[0]);
              agregarMano.dismiss();
              startActivityForResult(intent, AGREGAR_MANO_A_TODOS);
            }
          });

          cancelarMano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              agregarMano.dismiss();
            }
          });
        } else {
          startActivityForResult(intent, AGREGAR_MANO_A_TODOS);
        }
      }
    });

    finalizarPartida.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);

        View alertComment = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_finalizar_partida, null);
        builder.setView(alertComment);

        final Button cancelarFinalizar = alertComment.findViewById(R.id.cancelarFinalizar);
        final Button aceptarFinalizar = alertComment.findViewById(R.id.aceptarFinalizar);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        aceptarFinalizar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();

            if(pc.hayEmpate(id)) {
              AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);

              View alertFinalizar = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_finalizar_empate, null);
              builder.setView(alertFinalizar);

              final Button aceptarEmpate = alertFinalizar.findViewById(R.id.aceptarEmpate);
              final Button cancelarEmpate = alertFinalizar.findViewById(R.id.cancelarEmpate);
              RadioButton radio = alertFinalizar.findViewById(R.id.sortear);

              AlertDialog empate = builder.create();
              empate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              empate.setCanceledOnTouchOutside(false);
              empate.show();

              aceptarEmpate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Boolean sortear = radio.isChecked();
                  empate.dismiss();
                  //CARGA EL DIALOG RESULTADO CUANDO HAY EMPATADOS Y SE QUIERE SORTEAR O DEJAR EN EMPATE
                  dialogResultado(true, sortear);
                }
              });

              cancelarEmpate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  empate.dismiss();
                }
              });
            } else {
              //CARGA EL DIALOG RESULTADO CUANDO NO HAY EMPATADOS
              dialogResultado(false, false);
            }
          }
        });
        cancelarFinalizar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        });
      }
    });

    reiniciarPartida.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);
        View alertPuntuar = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_reiniciar_partida, null);
        builder.setView(alertPuntuar);

        final Button aceptarReiniciar = alertPuntuar.findViewById(R.id.aceptarReiniciar);
        final Button cancelarReiniciar = alertPuntuar.findViewById(R.id.cancelarReiniciar);

        AlertDialog reiniciarPartida = builder.create();
        reiniciarPartida.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        reiniciarPartida.show();

        aceptarReiniciar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Factory f = Factory.getInstance();
            IPartidaController pc = f.getIPartidaController();
            pc.reiniciarPartida(id);
            reiniciarPartida.dismiss();
            ((AdaptadorJugadoresEnPartidaList)adaptadorAddJugadorList).updateAdapter();
            ((AdaptadorJugadoresEnPartidaGrid)adaptadorAddJugador).updateAdapter();
          }
        });
      }
    });

    editarManopla.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);
        View alertBorrarUltimaMano = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_confirmar_borrar_mano, null);
        builder.setView(alertBorrarUltimaMano);

        final Button cancelarBorrado = alertBorrarUltimaMano.findViewById(R.id.cancelarBorradoUltimaMano);
        final Button aceptarBorrado = alertBorrarUltimaMano.findViewById(R.id.aceptarBorradoUltimaMano);

        AlertDialog borrarUltimaMano = builder.create();
        borrarUltimaMano.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        borrarUltimaMano.show();

        aceptarBorrado.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int nroRondas = pc.getRondas(id);
            pc.borrarMano(id, nroRondas);
            ((AdaptadorJugadoresEnPartidaList)adaptadorAddJugadorList).updateAdapter();
            ((AdaptadorJugadoresEnPartidaGrid)adaptadorAddJugador).updateAdapter();
            if (pc.getRondas(id) == 0) {
              editarManopla.setVisibility(View.GONE);
            }
            borrarUltimaMano.dismiss();
          }
        });

        cancelarBorrado.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            borrarUltimaMano.dismiss();
          }
        });
      }
    });

    /*editarManopla.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);
        View alertEditar = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_seleccionar_numero_mano_editar, null);
        builder.setView(alertEditar);

        final Button cancelarEditar = alertEditar.findViewById(R.id.cancelarEditar);
        final Button aceptarEditar = alertEditar.findViewById(R.id.aceptarEditar);
        final Spinner spinnerRondas = alertEditar.findViewById(R.id.spinnerRondas);
        final int[] manoEditar = new int[1];
        List<String> rondas = new ArrayList<String>();
        int nroRondas = pc.getRondas(id);
        for (int i = 1; i <= nroRondas; i++) {
          rondas.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapterRondas = new ArrayAdapter<String>(PartidaEnJuego.this, R.layout.list_item, rondas);
        spinnerRondas.setAdapter(adapterRondas);

        spinnerRondas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            manoEditar[0] = Integer.parseInt(parentView.getItemAtPosition(position).toString());
          }
          @Override
          public void onNothingSelected(AdapterView<?> parentView) {
            // your code here
          }
        });

        AlertDialog editarMano = builder.create();
        editarMano.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editarMano.show();

        aceptarEditar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(PartidaEnJuego.this, EditarManoTotal.class);
            intent.putExtra("id", id);
            intent.putExtra("mano", manoEditar[0]);
            startActivity(intent);
          }
        });
      }
    });*/

  }

  private void checkGanador() {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataPartida p = pc.getDatosPartida(id);
    int auxEliminados = 0;
    if (p.getPuntuarIndividual() == true) {
      Map<Integer, String> aux = pc.getPosiciones(id);
      if (pc.listarNoEliminados(id).size() == 1) {
        dialogResultado(false, false);
      }
    } else {
      if (pc.listarNoEliminados(id).size() == 1) {
        dialogResultado(false, false);
      } else {
        if (pc.hayGanador(id)) {
          auxEliminados = pc.listarEliminados(id).size();
          if (auxEliminados != eliminados) {
            dialogContinuar();
            eliminados = auxEliminados;
          }
        }
      }
    }
  }

  private void dialogContinuar() {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);

    View alerContinuar = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_continuar_partida, null);
    builder.setView(alerContinuar);

    final Button continuarBtn = alerContinuar.findViewById(R.id.continuarPartidaDialog);
    final RadioButton radio = alerContinuar.findViewById(R.id.continuar);


    AlertDialog continuarDialog = builder.create();
    continuarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    continuarDialog.setCanceledOnTouchOutside(false);
    continuarDialog.show();

    continuarBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (radio.isChecked()) {
          continuarDialog.dismiss();
        } else {
          if (pc.hayEmpate(id)) {
            dialogResultado(true, true);
          } else {
            dialogResultado(false, false);
          }
        }
      }
    });
  }

  private void dialogResultado(Boolean empate, Boolean sortear) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    DataPartida p = pc.getDatosPartida(id);

    if (empate) {

      AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);

      View alertResultado = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_resultado_partida, null);
      builder.setView(alertResultado);

      final Button aceptarFinalizar = alertResultado.findViewById(R.id.aceptarFinalizar);
      final LinearLayout layoutGanador = alertResultado.findViewById(R.id.layoutGanador);
      final TextView textoEmpate = alertResultado.findViewById(R.id.textoEmpate);
      final TextView textoGanador = alertResultado.findViewById(R.id.textoGanador);
      Boolean empatada;

      if (pc.hayGanador(id) || pc.noHayEmpatadosMinimos(id) || (!pc.noHayEmpatadosMinimos(id) && sortear)) {
        layoutGanador.setVisibility(View.VISIBLE);
        textoEmpate.setVisibility(View.INVISIBLE);
        pc.sortearEmpatados(id);
        //String ganador = sortearEmpatados();
        textoGanador.setText(pc.getGanadorPartida(id));
        empatada = false;
        pc.finalizarPartida(id, true);
      } else {
        ViewGroup.LayoutParams params = layoutGanador.getLayoutParams();
        params.height = 0;
        params.width = 0;
        layoutGanador.setLayoutParams(params);
        layoutGanador.setVisibility(View.INVISIBLE);
        empatada = true;
        pc.finalizarPartida(id, false);
      }
      AlertDialog resultado = builder.create();
      resultado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      resultado.setCanceledOnTouchOutside(false);
      resultado.show();
      aceptarFinalizar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mInterstitialAd.isLoaded()) {
            resultado.dismiss();
            Intent intent = new Intent(PartidaEnJuego.this, Resultado.class);
            intent.putExtra("empate", empatada);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
            mInterstitialAd.show();
          } else {
            resultado.dismiss();
            Intent intent = new Intent(PartidaEnJuego.this, Resultado.class);
            intent.putExtra("empate", empatada);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
            Log.d("TAG", "The interstitial wasn't loaded yet.");
          }
        }
      });
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(PartidaEnJuego.this);

      View alertResultado = LayoutInflater.from(PartidaEnJuego.this).inflate(R.layout.dialog_resultado_partida, null);
      builder.setView(alertResultado);

      final Button aceptarFinalizar = alertResultado.findViewById(R.id.aceptarFinalizar);
      final LinearLayout layoutGanador = alertResultado.findViewById(R.id.layoutGanador);
      final TextView textoEmpate = alertResultado.findViewById(R.id.textoEmpate);
      final TextView textoGanador = alertResultado.findViewById(R.id.textoGanador);

      layoutGanador.setVisibility(View.VISIBLE);
      textoEmpate.setVisibility(View.INVISIBLE);

      textoGanador.setText(pc.getGanadorPartida(id));
      pc.finalizarPartida(id, true);

      AlertDialog resultado = builder.create();
      resultado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      resultado.setCanceledOnTouchOutside(false);
      resultado.show();

      aceptarFinalizar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mInterstitialAd.isLoaded()) {
            resultado.dismiss();
            Intent intent = new Intent(PartidaEnJuego.this, Resultado.class);
            intent.putExtra("id", id);
            intent.putExtra("empate", false);
            startActivity(intent);
            finish();
            mInterstitialAd.show();
          } else {
            resultado.dismiss();
            Intent intent = new Intent(PartidaEnJuego.this, Resultado.class);
            intent.putExtra("id", id);
            intent.putExtra("empate", false);
            startActivity(intent);
            finish();
            Log.d("TAG", "The interstitial wasn't loaded yet.");
          }
        }
      });
    }
  }

  private String sortearEmpatados() {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    List<String> empatados = pc.getEmpatados(id);
    Collections.shuffle(empatados);
    return empatados.get(0);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_partida, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.view:
        if (viewLista) {
          item.setIcon(R.drawable.ic_grid_on);
        } else {
          item.setIcon(R.drawable.rank);
        }
        viewLista = !viewLista;
        cargarViews();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case AGREGAR_MANO:
        if(resultCode == Activity.RESULT_OK){
          ((AdaptadorJugadoresEnPartidaList)adaptadorAddJugadorList).updateAdapter();
          ((AdaptadorJugadoresEnPartidaGrid)adaptadorAddJugador).updateAdapter();
          checkGanador();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
          //Write your code if there's no result
        }
        break;
      case AGREGAR_MANO_A_TODOS:
        if(resultCode == Activity.RESULT_OK){
          Factory f = Factory.getInstance();
          IPartidaController pc = f.getIPartidaController();
          ((AdaptadorJugadoresEnPartidaList)adaptadorAddJugadorList).updateAdapter();
          ((AdaptadorJugadoresEnPartidaGrid)adaptadorAddJugador).updateAdapter();
          editarManopla.setVisibility(View.VISIBLE);
          pc.sumarRonda(id);
          checkGanador();
        }
        break;
      case 3:
        if (resultCode == Activity.RESULT_CANCELED) {
          ((AdaptadorJugadoresEnPartidaList)adaptadorAddJugadorList).updateAdapter();
          ((AdaptadorJugadoresEnPartidaGrid)adaptadorAddJugador).updateAdapter();
          checkGanador();
        }
        break;
    }
  }

  @Override
  public void onBackPressed() {
    Intent mainActivity = new Intent(PartidaEnJuego.this, MainScreen.class);
    startActivity(mainActivity);
    finish();
  }

  @Override
  protected void onResume() {
    super.onResume();
    ((AdaptadorJugadoresEnPartidaList)adaptadorAddJugadorList).updateAdapter();
    ((AdaptadorJugadoresEnPartidaGrid)adaptadorAddJugador).updateAdapter();
  }

  protected void cargarViews(){
    if (viewLista) {

      listaJugadores.setHasFixedSize(true);
      listaJugadores.addItemDecoration(new DividerItemDecoration(this,
        DividerItemDecoration.HORIZONTAL));
      listaJugadores.addItemDecoration(new DividerItemDecoration(this,
        DividerItemDecoration.VERTICAL));

      listaJugadores.setLayoutManager(LManagerJugadores);
      listaJugadores.setAdapter(adaptadorAddJugador);

    }
    else {
      listaJugadores.setLayoutManager(LManagerJugadoresList);
      listaJugadores.setAdapter(adaptadorAddJugadorList);
      listaJugadores.removeItemDecorationAt(0);
    }
  }
}