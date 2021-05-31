package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.R;

public class AdaptadorAgregarCarta extends RecyclerView.Adapter<AdaptadorAgregarCarta.ViewHolder>{

  private int layout;
  private String[] cartasAgregadas;
  private Integer[] valorCartas;

  public AdaptadorAgregarCarta(int layout, String[] cartasAgregadas, Integer[] valorCartas) {
    this.layout = layout;
    this.cartasAgregadas = cartasAgregadas;
    this.valorCartas = valorCartas;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorAgregarCarta.ViewHolder vh = new AdaptadorAgregarCarta.ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(cartasAgregadas[position], valorCartas[position]);


  }

  @Override
  public int getItemCount() {
    return cartasAgregadas.length;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre;
    private TextView valor;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      //this.nombre = itemView.findViewById(R.id.nombreCarta);
      this.valor = itemView.findViewById(R.id.valorcito);
    }

    public void bind(final String nombre, final Integer valor) {
      this.nombre.setText(nombre);
      this.valor.setText(String.valueOf(valor));

    }
  }
  public void updateAdapter(String[] cartasAgregadas, Integer[] valorCartas) {
    this.cartasAgregadas = cartasAgregadas;
    this.valorCartas = valorCartas;
    notifyDataSetChanged();
  }

  public String[] getCartasAgregadas() {
    return this.cartasAgregadas;
  }
}
