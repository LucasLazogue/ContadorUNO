package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.List;

public class AdaptadorSelectRegla extends RecyclerView.Adapter<AdaptadorSelectRegla.ViewHolder> {

  private List<String> reglas;
  private String reglaSeleccionada;
  private int layout;

  protected ViewHolder holderSeleccionada = null;

  public AdaptadorSelectRegla(int layout) {
    Factory f = Factory.getInstance();
    IReglaController rc = f.getIReglaController();
    this.reglas = rc.listarReglas();
    this.layout = layout;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorSelectRegla.ViewHolder vh = new AdaptadorSelectRegla.ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(reglas.get(position));

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int pos = holder.getAdapterPosition();
        if(reglaSeleccionada != null) {
          if (reglaSeleccionada.equals(reglas.get(pos))) {
            reglaSeleccionada = null;
            holderSeleccionada = null;
            holder.itemView.setBackgroundResource(android.R.color.transparent);
          } else {
            reglaSeleccionada = reglas.get(pos);
            holderSeleccionada.itemView.setBackgroundResource(android.R.color.transparent);
            holderSeleccionada = holder;
            holder.itemView.setBackgroundResource(R.color.purple_200);
          }
        }
        else {
          reglaSeleccionada = reglas.get(pos);
          holder.itemView.setBackgroundResource(R.color.purple_200);
          holderSeleccionada = holder;
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return reglas.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.nombre = itemView.findViewById(R.id.nombreRegla);
    }

    public void bind(final String nombre) {
      this.nombre.setText(nombre);

    }
  }



  public String getReglaSeleccionada() {
    return this.reglaSeleccionada;
  }
}
