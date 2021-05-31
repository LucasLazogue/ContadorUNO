package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataJugador;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.List;

public class AdaptadorResultados extends RecyclerView.Adapter<AdaptadorResultados.ViewHolder> {

  private int layout;
  private List<DataJugador> jugadores;
  private int index;

  public AdaptadorResultados(int index) {
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    this.jugadores = pc.getJugadores(index);
    this.layout = R.layout.view_resultado_partida;
    this.index = index;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorResultados.ViewHolder vh = new AdaptadorResultados.ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(jugadores.get(position));
  }

  @Override
  public int getItemCount() {
    return jugadores.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombreResultado;
    private TextView puntosResultado;
    private TextView posicionResultado;
    private ImageView medalla;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.nombreResultado = itemView.findViewById(R.id.nombreResultado);
      this.puntosResultado = itemView.findViewById(R.id.puntosResultado);
      this.posicionResultado = itemView.findViewById(R.id.posicionResultado);
      this.medalla = itemView.findViewById(R.id.medalla);
    }

    public void bind(final DataJugador jugador) {
      this.nombreResultado.setText(jugador.getNombre());
      this.puntosResultado.setText(String.valueOf(jugador.getPuntaje()));
      if (jugador.getPosicion() > 3) {
        this.medalla.setVisibility(View.GONE);
        this.posicionResultado.setVisibility(View.VISIBLE);
        this.posicionResultado.setText(String.valueOf(jugador.getPosicion()));
      } else {
        this.medalla.setVisibility(View.VISIBLE);
        this.posicionResultado.setVisibility(View.GONE);
        if (jugador.getPosicion() == 1) {
          this.medalla.setImageResource(R.drawable.first_place);
        } else {
          if (jugador.getPosicion() == 2) {
            this.medalla.setImageResource(R.drawable.second_place);
          } else {
            this.medalla.setImageResource(R.drawable.third_place);
          }
        }
      }
    }
  }


  public void updateAdapter(String tipoJuego) {

  }

}
