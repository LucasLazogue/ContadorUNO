package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IJugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.Arrays;
import java.util.List;

public class AdaptadorAddJugador extends RecyclerView.Adapter<AdaptadorAddJugador.ViewHolder> {

  private int layout;
  private List<String> jugadores;
  private List<String> seleccionados;
  private Context context;

  public AdaptadorAddJugador(int layout, List<String> jugadores, Context mContext) {
    Factory f = Factory.getInstance();
    IJugadorController jc = f.getIJugadorController();
    String[] jgs = jc.getJugadores();
    this.jugadores = Arrays.asList(jgs);
    this.layout = layout;
    this.seleccionados = jugadores;
    this.context = mContext;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorAddJugador.ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(jugadores.get(position), position);

    if (!seleccionados.contains(jugadores.get(position))) {
      holder.itemView.findViewById(R.id.layoutPintar).setBackgroundResource(android.R.color.transparent);
    }
    else {
      holder.itemView.findViewById(R.id.layoutPintar).setBackgroundResource(R.color.purple_200);
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RelativeLayout layoutSelec = holder.itemView.findViewById(R.id.layoutPintar);
        int pos = holder.getAdapterPosition();
        if (seleccionados.contains(jugadores.get(pos))) {
          seleccionados.remove(jugadores.get(pos));
          //layoutSelec.setBackgroundResource(android.R.color.transparent);
          layoutSelec.setBackgroundResource(R.drawable.rounded_layout_seleccionar);
        }
        else {
          seleccionados.add(jugadores.get(pos));
          layoutSelec.setBackgroundResource(R.drawable.rounded_layout_seleccionar_painted);
          //layoutSelec.setBackgroundResource(R.color.purple_200);
        }
      }
    });

    ImageView remove = holder.itemView.findViewById(R.id.basuritaJugador);
    remove.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Factory f = Factory.getInstance();
        IJugadorController jc = f.getIJugadorController();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View alertBorrar = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_jugador, null);
        builder.setView(alertBorrar);

        final Button cancelarBorradoJugador = alertBorrar.findViewById(R.id.cancelarBorradoJugador);
        final Button aceptarBorradoJugador = alertBorrar.findViewById(R.id.aceptarBorradoJugador);

        AlertDialog borrarJugador = builder.create();
        borrarJugador.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        borrarJugador.show();

        aceptarBorradoJugador.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            seleccionados.remove(jugadores.get(position));
            jc.eliminarJugador(jugadores.get(position));
            updateAdapter();
            borrarJugador.dismiss();
          }
        });

        cancelarBorradoJugador.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            borrarJugador.dismiss();
          }
        });
      }
    });
  }

  @Override
  public int getItemCount() {
    return this.jugadores.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre;
    private View view;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.nombre = itemView.findViewById(R.id.nombreAdd);
      this.view = itemView.findViewById(R.id.layoutPintar);
    }

    public void bind(final String nombre, int position) {
      this.nombre.setText(nombre);
      if (!seleccionados.contains(jugadores.get(position))) {
        view.setBackgroundResource(android.R.color.transparent);
      }
      else {
        view.setBackgroundResource(R.color.purple_200);
      }

    }

  }

  public void updateAdapter() {
    Factory f = Factory.getInstance();
    IJugadorController jc = f.getIJugadorController();
    String[] j = jc.getJugadores();
    List<String> jgs = Arrays.asList(j);
    this.jugadores = jgs;
    notifyDataSetChanged();
  }

  public List<String> getSeleccionados() {
    return this.seleccionados;
  }

}