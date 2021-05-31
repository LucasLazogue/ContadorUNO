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

import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.R;

import java.util.List;

public class AdaptadorReglas extends RecyclerView.Adapter<AdaptadorReglas.ViewHolder> {

  private List<String> reglas;
  private int layout;
  private OnItemClickListener listener;
  private Boolean todas;
  private Context context;

  public AdaptadorReglas(Context context, int layout, Boolean flip, Boolean todas, OnItemClickListener listener) {
    Factory f = Factory.getInstance();
    IReglaController rc = f.getIReglaController();
    if (flip != null) {
      this.reglas = rc.listarReglas(flip);
    } else {
      this.reglas = rc.listarReglas();
    }
    this.layout = layout;
    this.listener = listener;
    this.todas = todas;
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
    AdaptadorReglas.ViewHolder vh = new AdaptadorReglas.ViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(reglas.get(position));
    ImageView more =  holder.itemView.findViewById(R.id.optionsRegla);
    Factory f = Factory.getInstance();
    IReglaController rc = f.getIReglaController();
    DataRegla regla = rc.getDatosRegla(reglas.get(position));
    if (regla.getEditable() == false) {
      more.setVisibility(View.GONE);
    } else {
      more.setVisibility(View.VISIBLE);
      more.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          PopupMenu popup = new PopupMenu(v.getContext(), v);
          MenuInflater inflater = popup.getMenuInflater();
          inflater.inflate(R.menu.menu_context_regla, popup.getMenu());
          popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
              switch (item.getItemId()){
                case R.id.borrarRegla:
                  AlertDialog.Builder builder = new AlertDialog.Builder(context);

                  View alertComment = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_regla, null);
                  builder.setView(alertComment);
                  final Button btnAceptar = alertComment.findViewById(R.id.aceptarBorrarRegla);
                  final Button btnCancelar = alertComment.findViewById(R.id.cancelarBorrarRegla);

                  AlertDialog dialog = builder.create();
                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  dialog.show();

                  btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      dialog.dismiss();
                    }
                  });

                  btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Factory f = Factory.getInstance();
                      IReglaController rc = f.getIReglaController();
                      rc.borrarRegla(reglas.get(position));
                      updateAdapter();
                      dialog.dismiss();
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
    return reglas.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre;
    private ImageView remove;
    private TextView txtTipo;
    private ImageView optionsRegla;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.nombre = itemView.findViewById(R.id.nombreReglaExistente);
      this.remove = itemView.findViewById(R.id.removerAgregado);
      this.txtTipo = itemView.findViewById(R.id.tipoJuegotxt);
      this.optionsRegla = itemView.findViewById(R.id.optionsRegla);
    }

    public void bind(final String nombre) {
      Factory f = Factory.getInstance();
      IReglaController rc = f.getIReglaController();
      DataRegla regla = rc.getDatosRegla(nombre);
      this.nombre.setText(nombre);
      if (regla.getFlip()) {
        txtTipo.setText(R.string.unoFlip);
      } else {
        txtTipo.setText(R.string.unoClassic);
      }
      if (!regla.getEditable()) {
        this.optionsRegla.setVisibility(View.GONE);
      }
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

  public void updateAdapter() {
    Factory f = Factory.getInstance();
    IReglaController rc = f.getIReglaController();
    this.reglas = rc.listarReglas();
    notifyDataSetChanged();
  }

}
