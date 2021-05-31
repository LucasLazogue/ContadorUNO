package com.contadoruno.UnoScoreHelper.Logica.Singleton;

import com.contadoruno.UnoScoreHelper.Logica.Classes.Jugador;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Mano;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Partida;
import com.contadoruno.UnoScoreHelper.Logica.Classes.mapJugadores;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ManejadorPartidas {
  private static ManejadorPartidas instance = null;
  private List<Partida> partidas;
  private Partida cargada = null;

  public ManejadorPartidas(){
    this.partidas = new ArrayList<Partida>();
    this.partidas = getPartidas();
    for (Partida p : this.partidas) {
      if (p.getCargada()) {
        cargada = p;
        break;
      }
    }
  }

  public static ManejadorPartidas getInstance() {
    if (instance == null)
      instance = new ManejadorPartidas();
    return instance;
  }

  public List<Partida> getPartidas() {
    this.partidas.clear();
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Partida> lst = realm.where(Partida.class).findAll();
    for (Partida p : lst) {
      this.partidas.add(p);
    }
    return this.partidas;
  }

  public void addPartida(Partida partida){
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.copyToRealm(partida);
    realm.commitTransaction();
    marcarCargada(partida);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        RealmList<mapJugadores> jugadores = partida.getJugadores();
        for (mapJugadores j : jugadores) {
          Jugador jg = mj.getJugador(j.getNombre());
          jg.getPartidas().add(partida);
          realm.copyToRealmOrUpdate(jg);
        }
      }
    });
  }

  public Partida getUltimaPartida() {
    Realm realm = Realm.getDefaultInstance();
    Partida p = realm.where(Partida.class).findAll().last();
    return p;
  }

  public Partida getPartida(int index) {
    List<Partida> lst = this.getPartidas();
    for (Partida p : lst) {
      if (p.getId() == index) {
        return p;
      }
    }
    return null;
  }

  public void addMano(int index, String jugador, Mano nueva) {
    Realm realm = Realm.getDefaultInstance();
    Partida p = getPartida(index);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        RealmList<mapJugadores> jgs =  p.getJugadores();
        for (mapJugadores j : jgs) {
          if (j.getNombre().equals(jugador)) {
            j.getManos().add(nueva);
            j.setPuntaje(j.getPuntaje() + nueva.getValor());
            break;
          }
        }
        realm.copyToRealmOrUpdate(p);
      }
    });
  }

  public Partida getCargada() {
    List<Partida> partidas = this.getPartidas();
    for (Partida p : partidas) {
      if (p.getCargada()) {
        return p;
      }
    }
    return null;
  }

  public void marcarCargada(Partida p) {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        if (cargada != null){
          cargada.setCargada(false);
          realm.copyToRealmOrUpdate(cargada);
        }
        if (p != null) {
          p.setCargada(true);
          realm.copyToRealmOrUpdate(p);
        }
      }
    });
    this.cargada = p;
  }

  public void finalizarPartida(Partida p, String ganador) {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        p.setEstado("Finalizada");
        if (ganador != null) {
          p.setGanador(ganador);
        }
        if(cargada != null) {
          if (p.getId() == cargada.getId()) {
            p.setCargada(false);
            cargada = null;
          }
        }
        realm.copyToRealmOrUpdate(p);
      }
    });
  }

  public void borrarPartida(int index) {
    Realm realm = Realm.getDefaultInstance();
    Partida p = getPartida(index);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        ManejadorJugadores mj = ManejadorJugadores.getInstance();
        RealmList<mapJugadores> jugadores = p.getJugadores();
        for (mapJugadores mapj : jugadores) {
          Jugador j = mj.getJugador(mapj.getNombre());
          RealmList<Partida> parts = j.getPartidas();
          parts.remove(p);
          realm.copyToRealmOrUpdate(j);
        }
        p.getJugadores().clear();
        if (cargada.getId() == index) {
          cargada.setCargada(false);
          cargada = null;
        }
        p.deleteFromRealm();
      }
    });
  }
}
