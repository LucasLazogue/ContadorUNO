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
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IJugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

public class AdaptadorJugadoresTotales extends RecyclerView.Adapter<AdaptadorJugadoresTotales.ViewHolder> {

  private int layout;
  private String[] jugadores;
  private Context mContext;

  public AdaptadorJugadoresTotales(Context context) {
    Factory f = Factory.getInstance();
    IJugadorController jc = f.getIJugadorController();
    String[] jugadores = jc.getJugadores();
    this.jugadores = jugadores;
    this.layout = R.layout.view_jugador_total;
    this.mContext = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorJugadoresTotales.ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(jugadores[position]);
    ImageView more = holder.itemView.findViewById(R.id.optionsJugador);
    more.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_context_jugador, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {
            switch(item.getItemId()){
              case R.id.borrarJugador:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View alertBorrar = LayoutInflater.from(mContext).inflate(R.layout.dialog_borrar_jugador, null);
                builder.setView(alertBorrar);

                final Button cancelarBorradoJugador = alertBorrar.findViewById(R.id.cancelarBorradoJugador);
                final Button aceptarBorradoJugador = alertBorrar.findViewById(R.id.aceptarBorradoJugador);

                AlertDialog borrarJugador = builder.create();
                borrarJugador.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                borrarJugador.show();

                aceptarBorradoJugador.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Factory f = Factory.getInstance();
                    IJugadorController jc = f.getIJugadorController();
                    jc.eliminarJugador(jugadores[position]);
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

  @Override
  public int getItemCount() {
    return this.jugadores.length;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombreJugador, cantJugadas, cantGanadas;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      nombreJugador = itemView.findViewById(R.id.nombreJugador);
      cantJugadas = itemView.findViewById(R.id.cantJugadas);
      cantGanadas = itemView.findViewById(R.id.cantGanadas);

    }

    public void bind(final String nombre) {
      Factory f = Factory.getInstance();
      IJugadorController mj = f.getIJugadorController();
      nombreJugador.setText(nombre);
      cantJugadas.setText(String.valueOf(mj.getCantidadJugadas(nombre)));
      cantGanadas.setText(String.valueOf(mj.getCantidadGanadas(nombre)));
    }
  }

  public interface OnItemClickListener {
    void OnItemClickListener(String nombre);
  }

  public void updateAdapter() {
    Factory f = Factory.getInstance();
    IJugadorController jc = f.getIJugadorController();
    String[] j = jc.getJugadores();
    this.jugadores = j;
    notifyDataSetChanged();
  }

}