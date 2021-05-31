package com.contadoruno.UnoScoreHelper.Logica.Classes;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Mano extends RealmObject {
  private int valor;
  private RealmList<Carta> cartas;
  private Jugador punteaJugador = null;
  private int valorAgregado = 0;

  public Mano() {}

  public Mano(int valor) {
    this.valor = valor;
    cartas = new RealmList<Carta>();
  }

  public Mano(RealmList<Carta> cartas) {
    this.cartas = cartas;
  }

  public Mano(int valor, RealmList<Carta> cartas) {
    this.valor = valor;
    this.cartas = cartas;
  }

  public int getValor() {
    return valor;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }

  public RealmList<Carta> getCartas() {
    return cartas;
  }

  public void setCartas(RealmList<Carta> cartas) {
    this.cartas = cartas;
  }

  public Jugador getPunteaJugador() {
    return punteaJugador;
  }

  public void setPunteaJugador(Jugador punteaJugador) {
    this.punteaJugador = punteaJugador;
  }

  public int getValorAgregado() {
    return valorAgregado;
  }

  public void setValorAgregado(int valorAgregado) {
    this.valorAgregado = valorAgregado;
  }
}
