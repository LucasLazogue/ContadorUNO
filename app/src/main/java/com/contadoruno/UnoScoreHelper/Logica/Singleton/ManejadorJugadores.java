package com.contadoruno.UnoScoreHelper.Logica.Singleton;

import com.contadoruno.UnoScoreHelper.Logica.Classes.Jugador;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Partida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ManejadorJugadores {
  private static ManejadorJugadores instance = null;
  private Map<String, Jugador> jugadores;

  public ManejadorJugadores(){
    this.jugadores = new HashMap<String, Jugador>();
  }

  public static ManejadorJugadores getInstance() {
    if (instance == null)
      instance = new ManejadorJugadores();
    return instance;
  }

  public Map<String, Jugador> getJugadores() {
    this.jugadores.clear();
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Jugador> jugadores = realm.where(Jugador.class).findAll();
    for (Jugador j : jugadores) {
      this.jugadores.put(j.getNombre(), j);
    }
    return this.jugadores;
  }

  public void addJugador(Jugador jugador){
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.copyToRealm(jugador);
    realm.commitTransaction();
  }

  public void eliminarJugador(String nombre) {
    Realm realm = Realm.getDefaultInstance();
    Map<String, Jugador> jugadores = this.getJugadores();
    List<Integer> nombresPartidas = new ArrayList<Integer>();
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        Jugador jugador = jugadores.get(nombre);
        RealmList<Partida> parts = jugador.getPartidas();
        for (Partida p : parts) {
          nombresPartidas.add(p.getId());
        }
        for (Integer id : nombresPartidas) {
          Partida p = mp.getPartida(id);
          p.deleteFromRealm();
        }
        jugador.deleteFromRealm();
      }
    });
  }

  public void agregarPartida(Jugador jugador, Partida partida) {
    jugador.getPartidas().add(partida);
  }

  public Jugador getJugador(String nombre) {
    Map<String, Jugador> jugadores = this.getJugadores();
    return jugadores.get(nombre);
  }
}
