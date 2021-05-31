package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IJugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.Arrays;
import java.util.List;

public class AdaptadorJugadoresAgregados extends RecyclerView.Adapter<AdaptadorJugadoresAgregados.ViewHolder> {

  private int layout;
  private List<String> jugadores;
  private OnItemClickListener listener;

  public AdaptadorJugadoresAgregados(int layout, List<String> jugadores, OnItemClickListener listener) {
    this.jugadores = jugadores;
    this.layout = layout;
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorJugadoresAgregados.ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(jugadores.get(position));
    ImageView remove = holder.itemView.findViewById(R.id.removerAgregado);
    /*remove.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jugadores.remove(position);
        notifyDataSetChanged();
      }
    });*/
  }

  @Override
  public int getItemCount() {
    return this.jugadores.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre;
    private ImageView removerAgregado;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.nombre = itemView.findViewById(R.id.nombreAdd);
      this.removerAgregado = itemView.findViewById(R.id.removerAgregado);
    }

    public void bind(final String nombre) {
      this.nombre.setText(nombre);
      removerAgregado.setOnClickListener(new View.OnClickListener() {
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
    IJugadorController jc = f.getIJugadorController();
    String[] j = jc.getJugadores();
    List<String> jgs = Arrays.asList(j);
    this.jugadores = jgs;
    notifyDataSetChanged();
  }

  public List<String> getJugadores() {
    return jugadores;
  }
}