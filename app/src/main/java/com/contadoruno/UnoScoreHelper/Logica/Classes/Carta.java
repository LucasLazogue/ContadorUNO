package com.contadoruno.UnoScoreHelper.Logica.Classes;

import com.contadoruno.UnoScoreHelper.Logica.Enums.TipoClassic;

import io.realm.RealmObject;

public class Carta extends RealmObject {
  private TipoClassic tipo;
  private int valor;

  public Carta() {}

  public Carta(TipoClassic tipo, int valor) {
    this.tipo = tipo;
    this.valor = valor;
  }

  public TipoClassic getTipo() {
    return tipo;
  }

  public void setTipo(TipoClassic tipo) {
    this.tipo = tipo;
  }

  public int getValor() {
    return valor;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }
}
