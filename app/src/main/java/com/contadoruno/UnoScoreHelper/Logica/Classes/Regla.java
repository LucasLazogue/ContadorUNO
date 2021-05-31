package com.contadoruno.UnoScoreHelper.Logica.Classes;

import com.contadoruno.UnoScoreHelper.App.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Regla extends RealmObject {
  @PrimaryKey
  private int id;
  private String nombre;
  private RealmList<Carta> cartas;
  private Boolean soloWilds;
  private Boolean flip;
  private Boolean editable;

  public Regla() {}

  public Regla(String nombre) {
    this.nombre = nombre;
    this.cartas = new RealmList<Carta>();
    this.soloWilds = true;
    this.id = MyApplication.reglaId.incrementAndGet();
    this.editable = true;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public RealmList<Carta> getCartas() {
    return cartas;
  }

  public int getId() {
    return id;
  }

  public void setCartas(RealmList<Carta> cartas) {
    this.cartas = cartas;
  }

  /*public void addCarta(Carta carta) {
    this.cartas.add(carta);
  }*/

  public void setSoloWilds(Boolean soloWilds) {
    this.soloWilds = soloWilds;
  }

  public Boolean getSoloWilds() {
    return soloWilds;
  }

  public void removerCarta(String nombre) {
    if (!cartas.isEmpty()) {
      for (Carta c : cartas) {
        if (c.getTipo().toString().equals(nombre)) {
          cartas.remove(c);
          break;
        }
      }
    }
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
