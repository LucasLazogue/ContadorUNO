package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataJugador;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataMano;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.Arrays;
import java.util.List;

public class AdaptadorJugadoresEnPartidaGrid extends RecyclerView.Adapter<AdaptadorJugadoresEnPartidaGrid.ViewHolder> {

  private int layout;
  private List<String> jugadores;
  private Context mcontext;
  private int id;

  public AdaptadorJugadoresEnPartidaGrid(int id, int layout, List<String> jugadores, Context mcontext) {
    this.jugadores = jugadores;
    this.layout = layout;
    this.mcontext = mcontext;
    this.id = id;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View v = inflater.inflate(this.layout, parent, false);
    return new ViewHolder(v);
    /*View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorJugadoresEnPartidaGrid.ViewHolder vh = new ViewHolder(v);
    return vh;*/
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(jugadores.get(position), this.mcontext, id);
  }

  @Override
  public int getItemCount() {
    return this.jugadores.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre, puntos;
    private RecyclerView listaManos;
    private RecyclerView.Adapter adaptadorListaManos;
    private LinearLayoutManager LManagerManos;
    private ImageView crucecita;
    private TextView posicion;

    private Context mcontext;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.nombre = itemView.findViewById(R.id.nombreJugadorEnPartida);
      this.puntos = itemView.findViewById(R.id.txtPuntos);
      this.listaManos = itemView.findViewById(R.id.listaManos);
      this.crucecita = itemView.findViewById(R.id.crucecita);
      this.posicion = itemView.findViewById(R.id.posicion);
    }

    public void bind(final String nombre, Context mcontext, int id) {
      this.nombre.setText(nombre);
      this.mcontext = mcontext;
      Factory f = Factory.getInstance();
      IPartidaController pc = f.getIPartidaController();
      DataPartida p = pc.getDatosPartida(id);
      DataJugador dp = pc.getDatosJugador(id, nombre);
      DataMano[] manos = dp.getManos();
      this.puntos.setText(String.valueOf(dp.getPuntaje()));

      adaptadorListaManos = new AdaptadorManoJugador(R.layout.view_mano_jugador, manos, nombre, mcontext, false, id, puntos);
      LManagerManos = new LinearLayoutManager(this.mcontext);

      listaManos.setLayoutManager(this.LManagerManos);
      listaManos.setAdapter(adaptadorListaManos);

      if (p.getPuntuarIndividual()) {
        if (dp.getPuntaje() >= p.getPuntos()) {
          crucecita.setVisibility(View.VISIBLE);
          if (dp.getEliminado() != null && dp.getEliminado()) {
            if (dp.getPosicion() == 1) {
              itemView.setBackground(mcontext.getResources().getDrawable(R.drawable.rectangle_first));
              this.posicion.setText(String.valueOf(dp.getPosicion()));
              this.posicion.setVisibility(View.VISIBLE);
            } else {
              if (dp.getPosicion() == 2) {
                itemView.setBackground(mcontext.getResources().getDrawable(R.drawable.rectangle_second));
                this.posicion.setText(String.valueOf(dp.getPosicion()));
                this.posicion.setVisibility(View.VISIBLE);
              } else {
                if (dp.getPosicion() == 3) {
                  itemView.setBackground(mcontext.getResources().getDrawable(R.drawable.rectangle_third));
                  this.posicion.setText(String.valueOf(dp.getPosicion()));
                  this.posicion.setVisibility(View.VISIBLE);
                } else {
                  itemView.setBackground(mcontext.getResources().getDrawable(R.drawable.rectangle_generic_position));
                  this.posicion.setText(String.valueOf(dp.getPosicion()));
                  this.posicion.setVisibility(View.VISIBLE);
                }
              }
            }
          }
        } else {
          crucecita.setVisibility(View.GONE);
          this.posicion.setVisibility(View.GONE);
          itemView.setBackgroundResource(0);
        }
      } else {
        if (dp.getEliminado() != null && dp.getEliminado()) {
          if (dp.getPosicion() == 1) {
            itemView.setBackground(mcontext.getResources().getDrawable(R.drawable.rectangle_first));
            this.posicion.setText(String.valueOf(dp.getPosicion()));
            this.posicion.setVisibility(View.VISIBLE);
          } else {
            if (dp.getPosicion() == 2) {
              itemView.setBackground(mcontext.getResources().getDrawable(R.drawable.rectangle_second));
              this.posicion.setText(String.valueOf(dp.getPosicion()));
              this.posicion.setVisibility(View.VISIBLE);
            } else {
              if (dp.getPosicion() == 3) {
                itemView.setBackground(mcontext.getResources().getDrawable(R.drawable.rectangle_third));
                this.posicion.setText(String.valueOf(dp.getPosicion()));
                this.posicion.setVisibility(View.VISIBLE);
            } else {
                itemView.setBackground(mcontext.getResources().getDrawable(R.drawable.rectangle_generic_position));
                this.posicion.setText(String.valueOf(dp.getPosicion()));
                this.posicion.setVisibility(View.VISIBLE);
              }
            }
          }
        } else {
          crucecita.setVisibility(View.GONE);
          this.posicion.setVisibility(View.GONE);
          itemView.setBackgroundResource(0);
        }
      }
    }
  }

  public void updateAdapter() {
    Factory f = Factory.getInstance();
    IPartidaController jc = f.getIPartidaController();
    String[] j = jc.getDatosPartida(id).getJugadores();
    List<String> jgs = Arrays.asList(j);
    this.jugadores = jgs;
    notifyDataSetChanged();
  }

}