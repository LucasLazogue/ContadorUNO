package com.contadoruno.UnoScoreHelper.Logica.Datatypes;

import java.util.Date;

public class DataPartida {
  private String[] jugadores;
  private int rondas;
  private String ganador;
  private String reglas;
  private Date fecha;
  private String estado;
  private int puntos;
  private Boolean cargada = false;
  private Boolean puntuarIndividual;
  private Boolean flip;

  //private TipoPartida tipo;

  public DataPartida(String[] jugadores, int rondas, String ganador, String reglas, Date fecha, String estado, int puntos, Boolean puntuarIndividual, Boolean flip) {
    this.jugadores = jugadores;
    this.rondas = rondas;
    this.ganador = ganador;
    this.reglas = reglas;
    this.fecha = fecha;
    this.estado = estado;
    this.puntos = puntos;
    this.puntuarIndividual = puntuarIndividual;
    this.flip = flip;
  }

  public String[] getJugadores() {
    return jugadores;
  }

  public void setJugadores(String[] jugadores) {
    this.jugadores = jugadores;
  }

  public int getRondas() {
    return rondas;
  }

  public void setRondas(int rondas) {
    this.rondas = rondas;
  }

  public String getGanador() {
    return ganador;
  }

  public void setGanador(String ganador) {
    this.ganador = ganador;
  }

  public String getReglas() {
    return reglas;
  }

  public void setReglas(String reglas) {
    this.reglas = reglas;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public int getPuntos() {
    return puntos;
  }

  public void setPuntos(int puntos) {
    this.puntos = puntos;
  }

  public Boolean getCargada() {
    return cargada;
  }

  public void setCargada(Boolean cargada) {
    this.cargada = cargada;
  }

  public Boolean getPuntuarIndividual() {
    return puntuarIndividual;
  }

  public void setPuntuarIndividual(Boolean puntuarIndividual) {
    this.puntuarIndividual = puntuarIndividual;
  }

  public Boolean getFlip() {
    return flip;
  }

  public void setFlip(Boolean flip) {
    this.flip = flip;
  }
}
