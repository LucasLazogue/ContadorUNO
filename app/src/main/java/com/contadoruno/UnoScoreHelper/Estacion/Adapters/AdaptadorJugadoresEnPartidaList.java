package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Estacion.Activities.VistaDetalleJugadorPartida;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataJugador;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.Arrays;
import java.util.List;

public class AdaptadorJugadoresEnPartidaList extends RecyclerView.Adapter<AdaptadorJugadoresEnPartidaList.ViewHolder> {

  private OnItemClickListener listener;
  private int layout;
  private List<String> jugadores;
  private Context context;
  private int id;



  public AdaptadorJugadoresEnPartidaList(int id, int layout, List<String> jugadores, Context context, OnItemClickListener listener) {
    this.jugadores = jugadores;
    this.layout = layout;
    this.context = context;
    this.listener = listener;
    this.id = id;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorJugadoresEnPartidaList.ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(jugadores.get(position), this.listener, this.id);
    ImageView info = holder.itemView.findViewById(R.id.infoPuntajecito);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, VistaDetalleJugadorPartida.class);
        intent.putExtra("jugador", jugadores.get(position));
        intent.putExtra("id", id);
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return this.jugadores.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre, puntos;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.nombre = itemView.findViewById(R.id.nombreList);
      this.puntos = itemView.findViewById(R.id.puntosList);
    }

    public void bind(final String nombre, OnItemClickListener listener, int id) {
      this.nombre.setText(nombre);
      Factory f = Factory.getInstance();
      IPartidaController pc = f.getIPartidaController();
      DataJugador dp = pc.getDatosJugador(id, nombre);
      this.puntos.setText(String.valueOf(dp.getPuntaje()));
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.OnItemClickListener(nombre);
        }
      });
    }
  }

  public interface OnItemClickListener {
    void OnItemClickListener(String nombre);
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