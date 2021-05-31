package com.contadoruno.UnoScoreHelper.Estacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contadoruno.UnoScoreHelper.Logica.Enums.Classic;
import com.contadoruno.UnoScoreHelper.R;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorSeleccionarCartas extends RecyclerView.Adapter<AdaptadorSeleccionarCartas.ViewHolder> {

  private int layout;
  private List<String> cartas;
  private OnItemClickListener listener;

  public AdaptadorSeleccionarCartas(Boolean flip, Boolean soloWilds, OnItemClickListener listener) {
    this.layout = R.layout.view_cartas_jugadas;
    this.listener = listener;

    if (flip) {
      this.cartas = cargarFlip();
    } else {
      this.cartas = cargarCartas();
    }

    /*if(soloWilds) {
      this.cartas = cargarCartas();
    } else {
      this.cartas = cargarRojas();
    }*/
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorSeleccionarCartas.ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(cartas.get(position));
  }

  @Override
  public int getItemCount() {
    return this.cartas.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imagencitaCartita;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.imagencitaCartita = itemView.findViewById(R.id.imagencitaCartita);
    }

    public void bind(final String nombre) {
      this.imagencitaCartita.setImageResource(Classic.valueOf(nombre).getImagen());
      itemView.setOnClickListener(new View.OnClickListener() {
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

  public void updateAdapter(List<String> nuevas) {
    this.cartas = nuevas;
    notifyDataSetChanged();
  }

  public void cargarColor(String nombre) {
    switch (nombre){
      case "#ff5555":
        this.cartas = cargarRojas();
        notifyDataSetChanged();
        break;
      case "#ffaa00":
        this.cartas = cargarAmarillas();
        notifyDataSetChanged();
        break;
      case "#55aa55":
        this.cartas = cargarVerdes();
        notifyDataSetChanged();
        break;
      case "#5555ff":
        this.cartas = cargarAzules();
        notifyDataSetChanged();
        break;
    }
  }

  private List<String> cargarCartas() {
    List<String> res = new ArrayList<String>();
    res.add("CEROR");
    res.add("UNOR");
    res.add("DOSR");
    res.add("TRESR");
    res.add("CUATROR");
    res.add("CINCOR");
    res.add("SEISR");
    res.add("SIETER");
    res.add("OCHOR");
    res.add("NUEVER");
    res.add("REVERSER");
    res.add("DRAW2R");
    res.add("SKIPR");
    res.add("WILD");
    res.add("WILD4");
    return res;
  }

  private List<String> cargarFlip() {
    List<String> res = new ArrayList<String>();
    res.add("CEROR");
    res.add("UNOR");
    res.add("DOSR");
    res.add("TRESR");
    res.add("CUATROR");
    res.add("CINCOR");
    res.add("SEISR");
    res.add("SIETER");
    res.add("OCHOR");
    res.add("NUEVER");
    res.add("REVERSER");
    res.add("REVERSED");
    res.add("DRAW1FL");
    res.add("DRAW2R");
    res.add("DRAW5FD");
    res.add("SKIPR");
    res.add("SKIPD");
    res.add("FLIPD");
    res.add("FLIPL");
    res.add("WILD");
    res.add("WILD4");
    res.add("WILDDRAW2");
    res.add("WILDF");
    res.add("WILDDRAWC");
    return res;
  }


  private List<String> cargarRojas() {
    List<String> res = new ArrayList<String>();
    res.add("CEROR");
    res.add("UNOR");
    res.add("DOSR");
    res.add("TRESR");
    res.add("CUATROR");
    res.add("CINCOR");
    res.add("SEISR");
    res.add("SIETER");
    res.add("OCHOR");
    res.add("NUEVER");
    res.add("REVERSER");
    res.add("DRAW2R");
    res.add("SKIPR");
    res.add("WILD");
    res.add("WILD4");
    return res;
  }

  private List<String> cargarAzules() {
    List<String> res = new ArrayList<String>();
    res.add("CEROB");
    res.add("UNOB");
    res.add("DOSB");
    res.add("TRESB");
    res.add("CUATROB");
    res.add("CINCOB");
    res.add("SEISB");
    res.add("SIETEB");
    res.add("OCHOB");
    res.add("NUEVEB");
    res.add("REVERSEB");
    res.add("DRAW2B");
    res.add("SKIPB");
    res.add("WILD");
    res.add("WILD4");
    return res;
  }

  private List<String> cargarVerdes() {
    List<String> res = new ArrayList<String>();
    res.add("CEROG");
    res.add("UNOG");
    res.add("DOSG");
    res.add("TRESG");
    res.add("CUATROG");
    res.add("CINCOG");
    res.add("SEISG");
    res.add("SIETEG");
    res.add("OCHOG");
    res.add("NUEVEG");
    res.add("REVERSEG");
    res.add("DRAW2G");
    res.add("SKIPG");
    res.add("WILD");
    res.add("WILD4");
    return res;
  }

  private List<String> cargarAmarillas() {
    List<String> res = new ArrayList<String>();
    res.add("CEROY");
    res.add("UNOY");
    res.add("DOSY");
    res.add("TRESY");
    res.add("CUATROY");
    res.add("CINCOY");
    res.add("SEISY");
    res.add("SIETEY");
    res.add("OCHOY");
    res.add("NUEVEY");
    res.add("REVERSEY");
    res.add("DRAW2Y");
    res.add("SKIPY");
    res.add("WILD");
    res.add("WILD4");
    return res;
  }

}