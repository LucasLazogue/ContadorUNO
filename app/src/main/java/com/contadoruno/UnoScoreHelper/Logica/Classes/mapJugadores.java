package com.contadoruno.UnoScoreHelper.Logica.Classes;

import io.realm.RealmList;
import io.realm.RealmObject;

public class mapJugadores extends RealmObject {
  private String nombre;
  private RealmList<Mano> manos;
  private int puntaje;
  private Boolean eliminado;
  private int posicion;

  public mapJugadores() {}

  public mapJugadores(String nombre){
    this.nombre = nombre;
    this.manos = new RealmList<Mano>();
    this.puntaje = 0;
  }

  public RealmList<Mano> getManos() {
    return this.manos;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getPuntaje() {
    return this.puntaje;
  }

  public void setPuntaje(int puntaje) {
    this.puntaje = puntaje;
  }

  public Boolean getEliminado() {
    return eliminado;
  }

  public void setEliminado(Boolean eliminado) {
    this.eliminado = eliminado;
  }

  public int getPosicion() {
    return posicion;
  }

  public void setPosicion(int posicion) {
    this.posicion = posicion;
  }
}
