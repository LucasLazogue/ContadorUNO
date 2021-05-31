package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Enums.Classic;
import com.contadoruno.UnoScoreHelper.R;

import java.util.List;

public class AdaptadorCartasJugadas extends RecyclerView.Adapter<AdaptadorCartasJugadas.ViewHolder>{

  private int layout;
  private List<String> cartas;
  private OnItemClickListener listener;


  public AdaptadorCartasJugadas(int layout, List<String> cartas, OnItemClickListener listener) {
    this.layout = layout;
    this.cartas = cartas;
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorCartasJugadas.ViewHolder vh = new AdaptadorCartasJugadas.ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(cartas.get(position));
  }

  @Override
  public int getItemCount() {
    return cartas.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imagen;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.imagen = itemView.findViewById(R.id.imagencitaCartita);

    }

    public void bind(final String nombre) {
      this.imagen.setImageResource(Classic.valueOf(nombre).getImagen());
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.OnItemClickListener(nombre);
        }
      });
    }
  }

  public interface OnItemClickListener {
    void OnItemClickListener(String nombre);
  }

  public List<String> getCartas() {
    return cartas;
  }

  public void setCartas(List<String> cartas) {
    this.cartas = cartas;
  }

  /*public void updateAdapter(String[] cartasAgregadas, Integer[] valorCartas) {
    this.cartasAgregadas = cartasAgregadas;
    this.valorCartas = valorCartas;
    notifyDataSetChanged();
  }*/
}
