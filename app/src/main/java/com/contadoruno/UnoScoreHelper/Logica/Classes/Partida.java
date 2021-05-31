package com.contadoruno.UnoScoreHelper.Logica.Classes;

import com.contadoruno.UnoScoreHelper.App.MyApplication;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Partida extends RealmObject {
  @PrimaryKey
  private int id;
  private RealmList<mapJugadores> jugadores;
  private int rondas;
  private String ganador;
  private Regla reglas;
  private Date fecha;
  private int puntos;
  private String estado;
  private Boolean cargada = false;
  private Boolean puntuarIndividual;
  private Boolean flip;

  //private TipoPartida tipo;

  public Partida() {}

  public Partida(Regla reglas, Boolean flip) {
    this.id = MyApplication.partidaId.incrementAndGet();
    this.jugadores = new RealmList<mapJugadores>();
    this.rondas = 0;
    this.ganador = null;
    this.reglas = reglas;
    this.fecha = Calendar.getInstance().getTime();
    this.estado = "En Curso";
    this.puntos = 500;
    this.flip = flip;
  }

    /*public Partida(RealmList<Jugador> jugadores, Regla reglas) {
        this.id = MyApplication.partidaId.incrementAndGet();
        this.rondas = 0;
        this.ganador = null;
        this.reglas = reglas;
        this.fecha = Calendar.getInstance().getTime();
    }*/

  public RealmList<mapJugadores> getJugadores() {
    return jugadores;
  }

    /*public void setJugadores(RealmList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }*/

  public void addJugador(Jugador jugador) {
    mapJugadores nuevo = new mapJugadores(jugador.getNombre());
    this.jugadores.add(nuevo);
  }

  public int getRondas() {
    return rondas;
  }

  public String getGanador() {
    return ganador;
  }

  public void setGanador(String ganador) {
    this.ganador = ganador;
  }

  public Regla getReglas() {
    return reglas;
  }

  public void setReglas(Regla reglas) {
    this.reglas = reglas;
  }

  public int getId() {
    return id;
  }

  public Date getFecha() {
    return this.fecha;
  }

  public int getPuntos() {
    return puntos;
  }

  public void setPuntos(int puntos) {
    this.puntos = puntos;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
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

  public void setRondas(int i) {
    this.rondas = i;
  }
}
