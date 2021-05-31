package com.contadoruno.UnoScoreHelper.Logica.Classes;

import com.contadoruno.UnoScoreHelper.App.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Jugador extends RealmObject {
  @PrimaryKey
  private int id;
  private String nombre;
  private RealmList<Partida> partidas;
  private int partidasJugadas;
  private int partidasGanadas;

  public Jugador (){}

  public Jugador(String nombre) {
    this.nombre = nombre;
    this.id = MyApplication.jugadorId.incrementAndGet();
    this.partidas = new RealmList<Partida>();
    this.partidasJugadas = 0;
    this.partidasGanadas = 0;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getId() {
    return this.id;
  }

  public RealmList<Partida> getPartidas() {
    return partidas;
  }

  public int getPartidasJugadas() {
    return partidasJugadas;
  }

  public void setPartidasJugadas(int partidasJugadas) {
    this.partidasJugadas = partidasJugadas;
  }

  public int getPartidasGanadas() {
    return partidasGanadas;
  }

  public void setPartidasGanadas(int partidasGanadas) {
    this.partidasGanadas = partidasGanadas;
  }
}
