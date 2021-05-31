package com.contadoruno.UnoScoreHelper.Logica.Datatypes;

import java.io.Serializable;

public class DataRegla implements Serializable {
  String nombre;
  private DataCarta[] cartas;
  private Boolean flip;
  private Boolean editable;

  public DataRegla(String nombre, DataCarta[] cartas) {
    this.nombre = nombre;
    this.cartas = cartas;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public DataCarta[] getCartas() {
    return cartas;
  }

  public void setCartas(DataCarta[] cartas) {
    this.cartas = cartas;
  }

  public Boolean getFlip() {
    return flip;
  }

  public void setFlip(Boolean flip) {
    this.flip = flip;
  }

  public Boolean getEditable() {
    return editable;
  }

  public void setEditable(Boolean editable) {
    this.editable = editable;
  }
}
