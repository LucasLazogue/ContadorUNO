package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Enums.Classic;
import com.contadoruno.UnoScoreHelper.R;

import java.util.List;

public class AdaptadorCartasRegla extends RecyclerView.Adapter<AdaptadorCartasRegla.ViewHolder>{

  private int layout;
  private List<String> cartas;
  private List<Integer> valorCartas;
  private OnItemClickListener listener;

  public AdaptadorCartasRegla(List<String> cartas, List<Integer> valorCartas, OnItemClickListener listener) {
    this.layout = R.layout.view_cartas_agregadas;
    this.cartas = cartas;
    this.valorCartas = valorCartas;
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorCartasRegla.ViewHolder vh = new AdaptadorCartasRegla.ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(cartas.get(position), valorCartas.get(position), position);


  }

  @Override
  public int getItemCount() {
    return cartas.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    //private TextView nombre;
    private ImageView imgCarta;
    private TextView valor;
    private int position;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      //this.nombre = itemView.findViewById(R.id.nombreCarta);
      this.imgCarta = itemView.findViewById(R.id.imgCarta);
      this.valor = itemView.findViewById(R.id.valorcito);
    }

    public void bind(final String nombre, final Integer valor, final int position) {
      //this.nombre.setText(nombre);
      this.imgCarta.setImageResource(Classic.valueOf(nombre).getImagen());
      this.valor.setText(String.valueOf(valor));
      this.position = position;
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.OnItemClickListener(nombre, position);
        }
      });

    }
  }

  public interface OnItemClickListener {
    void OnItemClickListener(String nombre, int position);
  }

  public void updateAdapter(List<Integer> valorCartas) {
    this.valorCartas = valorCartas;
    notifyDataSetChanged();
  }

  /*public void updateAdapter(String[] cartasAgregadas, Integer[] valorCartas) {
    this.cartasAgregadas = cartasAgregadas;
    this.valorCartas = valorCartas;
    notifyDataSetChanged();
  }*/

}
