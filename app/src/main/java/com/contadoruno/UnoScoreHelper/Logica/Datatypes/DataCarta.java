package com.contadoruno.UnoScoreHelper.Logica.Datatypes;

import java.io.Serializable;

public class DataCarta implements Serializable {
  private String tipo;
  private int valor;

  public DataCarta(String tipo, int valor) {
    this.tipo = tipo;
    this.valor = valor;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public int getValor() {
    return valor;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }
}
