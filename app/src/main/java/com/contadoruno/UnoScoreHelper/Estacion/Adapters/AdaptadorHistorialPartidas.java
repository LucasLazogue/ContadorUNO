package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdaptadorHistorialPartidas extends RecyclerView.Adapter<AdaptadorHistorialPartidas.ViewHolder> {

  private int layout;
  private List<Integer> partidas;
  private Boolean historial;
  private OnItemClickListener listener;
  private Context mContext;
  private String tipoJuego;

  public AdaptadorHistorialPartidas(int layout, Boolean historial, OnItemClickListener listener, String tipoJuego, Context context) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    if (historial){
      this.partidas = pc.getPartidasFinalizadas(tipoJuego);
    }
    else {
      this.partidas = pc.getPartidasEnCurso(tipoJuego);
    }
    this.layout = layout;
    this.historial = historial;
    this.listener = listener;
    this.mContext = context;
    this.tipoJuego = tipoJuego;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorHistorialPartidas.ViewHolder vh = new AdaptadorHistorialPartidas.ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(partidas.get(position));
    if(!historial){
      ImageView more = holder.itemView.findViewById(R.id.moreOptions);
      more.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          PopupMenu popup = new PopupMenu(v.getContext(), v);
          MenuInflater inflater = popup.getMenuInflater();
          inflater.inflate(R.menu.menu_context_partida_jugada, popup.getMenu());
          popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
              switch(item.getItemId()){
                case R.id.borrarPartida:
                  AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                  View alertBorrar = LayoutInflater.from(mContext).inflate(R.layout.dialog_borrar_partida, null);
                  builder.setView(alertBorrar);

                  final Button cancelarBorradoPartida = alertBorrar.findViewById(R.id.cancelarBorradoPartida);
                  final Button aceptarBorradoPartida = alertBorrar.findViewById(R.id.aceptarBorradoPartida);

                  AlertDialog borrarPartida = builder.create();
                  borrarPartida.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  borrarPartida.show();

                  aceptarBorradoPartida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Factory f = Factory.getInstance();
                      IPartidaController pc = f.getIPartidaController();
                      pc.borrarPartida(partidas.get(position));
                      updateAdapter(tipoJuego);
                      borrarPartida.dismiss();
                    }
                  });

                  cancelarBorradoPartida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      borrarPartida.dismiss();
                    }
                  });

                  return true;
                default:
                  return true;
              }
            }
          });
          popup.show();
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return partidas.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView fecha, reglasPartida, estadoPartida, nombreGanador, tipoJuego;
    private LinearLayout layoutGanadorCarga;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.fecha = itemView.findViewById(R.id.nombrePartidaEnJuego);
      this.reglasPartida = itemView.findViewById(R.id.reglasPartida);
      this.estadoPartida = itemView.findViewById(R.id.estadoPartida);
      this.nombreGanador = itemView.findViewById(R.id.nombreGanador);
      this.tipoJuego = itemView.findViewById(R.id.tipoJuego);
      this.layoutGanadorCarga = itemView.findViewById(R.id.layoutGanadorCarga);
    }

    public void bind(final int index) {
      Factory f = Factory.getInstance();
      IPartidaController pc = f.getIPartidaController();

      DataPartida partida = pc.getDatosPartida(index);
      String tipo;
      if (partida.getFlip()){
        tipo = "Uno Flip";
      } else {
        tipo = "Uno Classic";
      }


      if (partida.getGanador() == null) {
        layoutGanadorCarga.setVisibility(View.GONE);
      }
      else {
        layoutGanadorCarga.setVisibility(View.VISIBLE);
        nombreGanador.setText(partida.getGanador());
      }
      reglasPartida.setText(partida.getReglas());
      String estado;
      if (partida.getEstado().equals("Finalizada")) {
        estado = mContext.getString(R.string.finalizada);
      } else {
        estado = mContext.getString(R.string.enCurso);
      }
      estadoPartida.setText(estado);
      this.tipoJuego.setText(tipo);

      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      this.fecha.setText(dateFormat.format(partida.getFecha()).toString());

      if (listener != null) {
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            listener.OnItemClickListener(index);
          }
        });
      }
    }
  }

  public interface OnItemClickListener {
    void OnItemClickListener(int index);
  }

  public void updateAdapter(String tipoJuego) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    if (this.historial) {
      this.partidas = pc.getPartidasFinalizadas(tipoJuego);
    }
    else {
      this.partidas = pc.getPartidasEnCurso(tipoJuego);
    }
    notifyDataSetChanged();
  }

}
