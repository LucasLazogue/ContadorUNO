package com.contadoruno.UnoScoreHelper.Logica.Datatypes;

public class DataJugador {
  private String nombre;
  private int puntaje;
  private DataMano[] manos;
  private int partidasJugadas;
  private Boolean eliminado;
  private int posicion;

  public DataJugador(String nombre, int puntaje, DataMano[] manos) {
    this.nombre = nombre;
    this.puntaje = puntaje;
    this.manos = manos;
    this.partidasJugadas = 0;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getPuntaje() {
    return puntaje;
  }

  public void setPuntaje(int puntaje) {
    this.puntaje = puntaje;
  }

  public DataMano[] getManos() {
    return manos;
  }

  public void setManos(DataMano[] manos) {
    this.manos = manos;
  }

  public int getPartidasJugadas() {
    return partidasJugadas;
  }

  public void setPartidasJugadas(int partidasJugadas) {
    this.partidasJugadas = partidasJugadas;
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
