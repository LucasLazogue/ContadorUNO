package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.contadoruno.UnoScoreHelper.Estacion.Activities.CartasJugadas;
import com.contadoruno.UnoScoreHelper.Estacion.Activities.EditarMano;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataMano;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

public class AdaptadorManoJugador extends RecyclerView.Adapter<AdaptadorManoJugador.ViewHolder>{

  private int layout;
  private DataMano[] manos;
  private String nombreJugador;
  private Context mcontext;
  private Boolean iconos;
  private int id;
  private TextView puntajeJugador;


  public AdaptadorManoJugador(int layout, DataMano[] manos, String nombreJugador, Context context, Boolean iconos, int id, TextView puntajeJugador) {
    this.layout = layout;
    this.manos = manos;
    this.nombreJugador = nombreJugador;
    this.mcontext = context;
    this.iconos = iconos;
    this.id = id;
    this.puntajeJugador = puntajeJugador;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorManoJugador.ViewHolder vh = new AdaptadorManoJugador.ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(manos[position], position);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(mcontext, CartasJugadas.class);
        intent.putExtra("cartas", manos[position].getCartas());
        intent.putExtra("mano", manos[position]);
        intent.putExtra("valorMano", manos[position].getValor());
        intent.putExtra("nombreJugador", nombreJugador);
        intent.putExtra("id", id);
        mcontext.startActivity(intent);
      }
    });

    ImageView editarMano = holder.itemView.findViewById(R.id.editarMano);
    editarMano.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(mcontext, EditarMano.class);
        intent.putExtra("mano", manos[position]);
        intent.putExtra("nombre", nombreJugador);
        intent.putExtra("id", id);
        intent.putExtra("numeroMano", position + 1);
        ((Activity)mcontext).startActivityForResult(intent, 3);
      }
    });

    ImageView borrarMano = holder.itemView.findViewById(R.id.eliminarMano);
    borrarMano.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

        View alertComment = LayoutInflater.from(mcontext).inflate(R.layout.dialog_borrar_mano, null);
        builder.setView(alertComment);

        final Button cancelarBorradoMano = alertComment.findViewById(R.id.cancelarBorradoMano);
        final Button aceptarBorradoMano = alertComment.findViewById(R.id.aceptarBorradoMano);

        AlertDialog borrarMano = builder.create();
        borrarMano.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        borrarMano.show();

        aceptarBorradoMano.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Factory f = Factory.getInstance();
            IPartidaController pc = f.getIPartidaController();
            pc.borrarMano(id, position);
            borrarMano.dismiss();
            manos = pc.getDatosJugador(id, nombreJugador).getManos();
            puntajeJugador.setText(String.valueOf(pc.getDatosJugador(id, nombreJugador).getPuntaje()));
            notifyDataSetChanged();
          }
        });
      }
    });

  }

  @Override
  public int getItemCount() {
    return manos.length;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre;
    private TextView numeritoMano;
    private RelativeLayout iconosMano;
    private ImageView editarMano;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.nombre = itemView.findViewById(R.id.puntosMano);
      this.numeritoMano = itemView.findViewById(R.id.numeritoMano);
      this.iconosMano = itemView.findViewById(R.id.iconosMano);
      this.editarMano = itemView.findViewById(R.id.editarMano);
    }

    public void bind(final DataMano mano, int pos) {
      this.nombre.setText(String.valueOf(mano.getValor()));
      this.numeritoMano.setText(String.valueOf(pos + 1));
      if (iconos){
        this.iconosMano.setVisibility(View.VISIBLE);
        if (nombreJugador.equals(mano.getPunteaJugador())) {
          editarMano.setVisibility(View.GONE);
        }
      } else {
        this.iconosMano.setVisibility(View.GONE);
      }
    }
  }

  public void setIconos(Boolean iconos){
    this.iconos = iconos;
    notifyDataSetChanged();
  }

  public void updateAdapter(DataMano[] manos) {
    this.manos = manos;
    notifyDataSetChanged();
  }

  /*public void updateAdapter(String[] cartasAgregadas, Integer[] valorCartas) {
    this.cartasAgregadas = cartasAgregadas;
    this.valorCartas = valorCartas;
    notifyDataSetChanged();
  }*/
}
