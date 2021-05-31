package com.contadoruno.UnoScoreHelper.Logica.Datatypes;

import java.io.Serializable;

public class DataMano implements Serializable{
  private int valor;
  private DataCarta[] cartas;
  private String punteaJugador = "";
  private int puntajeAgregado = 0;

  public DataMano(int valor, DataCarta[] cartas) {
    this.valor = valor;
    this.cartas = cartas;
  }

  public int getValor() {
    return valor;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }

  public DataCarta[] getCartas() {
    return cartas;
  }

  public void setCartas(DataCarta[] cartas) {
    this.cartas = cartas;
  }

  public String getPunteaJugador() {
    return punteaJugador;
  }

  public void setPunteaJugador(String punteaJugador) {
    this.punteaJugador = punteaJugador;
  }

  public int getPuntajeAgregado() {
    return puntajeAgregado;
  }

  public void setPuntajeAgregado(int puntajeAgregado) {
    this.puntajeAgregado = puntajeAgregado;
  }
}
