package com.contadoruno.UnoScoreHelper.Logica.Controllers;

import com.contadoruno.UnoScoreHelper.Logica.Classes.Carta;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Jugador;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Mano;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Partida;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Regla;
import com.contadoruno.UnoScoreHelper.Logica.Classes.mapJugadores;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataCarta;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataJugador;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataMano;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataPartida;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.ErrorAlCrearPartida;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.PuntajeVacio;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.ReglaNula;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.TamanoJugadores;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.ManejadorJugadores;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.ManejadorPartidas;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.ManejadorReglas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;

public class PartidaController implements IPartidaController {
  @Override
  public void crearPartida(String regla, String[] jugadores, String puntaje, Boolean puntuarIndividual, Boolean flip) throws ErrorAlCrearPartida, PuntajeVacio, TamanoJugadores, ReglaNula {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    ManejadorReglas mr = ManejadorReglas.getInstance();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();

    if (regla == null) {
      throw new ReglaNula("No se ha seleccionado regla");
    }

    if (jugadores == null || jugadores.length < 2) {
      throw new TamanoJugadores("Se deben seleccionar al menos 2 jugadores");
    }

    if (puntaje.equals("")) {
      throw new PuntajeVacio("Se debe ingresar el puntaje de la partida");
    }

    if (flip == null || regla.equals("")){
      throw new ErrorAlCrearPartida("Se debe seleccionar el tipo de juego");
    }

    Map<String, Regla> reglas = mr.getReglas();
    List<Jugador> lstJugadores = new ArrayList<Jugador>();
    for (String s : jugadores) {
      lstJugadores.add(mj.getJugadores().get(s));
    }

    Partida nueva = new Partida(reglas.get(regla), flip);
    for (Jugador j : lstJugadores) {
      Realm realm = Realm.getDefaultInstance();
      realm.beginTransaction();
      j.setPartidasJugadas(j.getPartidasJugadas() + 1);
      realm.copyToRealmOrUpdate(j);
      realm.commitTransaction();
      mapJugadores nuevo = new mapJugadores(mj.getJugadores().get(j.getNombre()).getNombre());
      if (!puntuarIndividual) {
        nuevo.setPosicion(0);
        nuevo.setEliminado(nueva.getPuntos() <= nuevo.getPuntaje());
      } else {
        nuevo.setPosicion(0);
        nuevo.setEliminado(nueva.getPuntos() <= nuevo.getPuntaje());
      }
      nueva.getJugadores().add(nuevo);
    }

    int pts;
    if(puntaje.equals("MAX_VALUE")){
      pts = Integer.MAX_VALUE;
    } else {
      pts = Integer.parseInt(puntaje);
    }
    nueva.setPuntos(pts);
    nueva.setCargada(true);
    nueva.setPuntuarIndividual(puntuarIndividual);
    mp.addPartida(nueva);
    ordenarPosiciones(nueva.getId());
  }

  protected void posicionarJugador(Partida part, String nombre) {
    int id = part.getId();
    String jugador = getUltimoEliminado(id);
    RealmList<mapJugadores> jugadores = part.getJugadores();
    if (part.getPuntuarIndividual()) {
      if (jugador == null) {
        for (int i = 0; i < jugadores.size(); i++) {
          if (jugadores.get(i).getNombre().equals(nombre)) {
            mapJugadores aux = jugadores.last();
            jugadores.set(jugadores.size() - 1, jugadores.get(i));
            jugadores.set(i, aux);
            break;
          }
        }
      } else {
        mapJugadores mover = null;
        int posMover = 0;
        mapJugadores aux = null;
        int pos = 0;
        for (int i = 0; i < jugadores.size(); i++) {
          if (jugadores.get(i).getNombre().equals(jugador)) {
            aux = jugadores.get(i);
            pos = i;
            break;
          }
        }
        for (int i = 0; i < jugadores.size(); i++) {
          if (jugadores.get(i).getNombre().equals(nombre)) {
            mover = jugadores.get(i);
            posMover = i;
            break;
          }
        }
        mapJugadores aux2 = jugadores.get(pos - 1);
        jugadores.set(pos - 1, mover);
        jugadores.set(posMover, aux2);
      }
    } else {
      if (jugador == null) {
        for (int i = 0; i < jugadores.size(); i++) {
          if (jugadores.get(i).getNombre().equals(nombre)) {
            mapJugadores aux = jugadores.first();
            jugadores.set(0, jugadores.get(i));
            jugadores.set(i, aux);
            break;
          }
        }
      } else {
        mapJugadores mover = null;
        int posMover = 0;
        mapJugadores aux = null;
        int pos = 0;
        for (int i = 0; i < jugadores.size(); i++) {
          if (jugadores.get(i).getNombre().equals(jugador)) {
            aux = jugadores.get(i);
            pos = i;
            break;
          }
        }
        for (int i = 0; i < jugadores.size(); i++) {
          if (jugadores.get(i).getNombre().equals(nombre)) {
            mover = jugadores.get(i);
            posMover = i;
            break;
          }
        }
        mapJugadores aux2 = jugadores.get(pos + 1);
        jugadores.set(pos + 1, mover);
        jugadores.set(posMover, aux2);
      }
    }
  }

  private int getMayorPosicionado(Partida part) {
    RealmList<mapJugadores> jugadores = part.getJugadores();
    int res = jugadores.first().getPosicion();
    for (mapJugadores j : jugadores) {
      if (j.getPosicion() <= res && j.getPosicion() != 0) {
        res = j.getPosicion();
      }
    }
    return res;
  }

  protected int getMenorPosicionado(Partida part) {
    RealmList<mapJugadores> jugadores = part.getJugadores();
    int res = jugadores.first().getPosicion();
    for (mapJugadores j : jugadores) {
      if (j.getPosicion() != 0 && j.getPosicion() >= res) {
        res = j.getPosicion();
      }
    }
    return res;
  }



  @Override
  public DataPartida getDatosPartida(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> parts = mp.getPartidas();
    Partida p = null;
    for (Partida ite : parts) {
      if (ite.getId() == index) {
        p = ite;
        break;
      }
    }
    RealmList<mapJugadores> aJgs = p.getJugadores();
    String[] jgs = new String[aJgs.size()];
    int i = 0;
    for (mapJugadores j : aJgs) {
      jgs[i] = j.getNombre();
      i++;
    }
    DataPartida res = new DataPartida(jgs, p.getRondas(), p.getGanador(), p.getReglas().getNombre(), p.getFecha(), p.getEstado(), p.getPuntos(), p.getPuntuarIndividual(), p.getFlip());
    return res;
  }

  @Override
  public String getGanadorPartida(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    if (p.getPuntuarIndividual()) {
      return getMenorPuntuado(index);
    } else {
      RealmList<mapJugadores> jugadores = p.getJugadores();
      mapJugadores aux = jugadores.first();
      for (mapJugadores j : jugadores) {
        if (j.getPosicion() == 1) {
          aux = j;
          break;
        }
      }
      return aux.getNombre();
    }
  }

  @Override
  public void setGanadorPartida(int index, String nombre) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> parts = mp.getPartidas();
    Partida p = null;
    for (Partida ite : parts) {
      if (p.getId() == index) {
        p = ite;
        break;
      }
    }
    p.setGanador(nombre);
  }

  @Override
  public DataRegla getRegla(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    Regla aReg = p.getReglas();
    List<Carta> aCrts = aReg.getCartas();
    DataCarta[] crts = new DataCarta[aCrts.size()];
    int i = 0;
    for (Carta c : aCrts) {
      crts[i] = new DataCarta(c.getTipo().getEnum().toString(), c.getValor());
      i++;
    }
    DataRegla res = new DataRegla(aReg.getNombre(), crts);
    return res;
  }

  @Override
  public DataJugador getDatosJugador(int index, String jugador) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    List<Partida> parts = mp.getPartidas();
    Partida p = null;
    p = mp.getPartida(index);
    RealmList<mapJugadores> aJgs = p.getJugadores();
    mapJugadores mapj = null;
    Jugador aj = null;
    RealmList<Mano> aManos = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugador)) {
        aj = mj.getJugadores().get(j.getNombre());
        aManos = j.getManos();
        mapj = j;
        break;
      }
    }
    DataMano[] manos = new DataMano[aManos.size()];
    int i = 0;
    for (Mano m : aManos) {
      RealmList<Carta> aCartas = m.getCartas();
      DataCarta[] cartas = new DataCarta[aCartas.size()];
      int j = 0;
      for (Carta c : aCartas) {
        cartas[j] = new DataCarta(c.getTipo().getEnum().toString(), c.getValor());
        j++;
      }
      manos[i] = new DataMano(m.getValor(), cartas);
      if (m.getPunteaJugador() != null) {
        manos[i].setPunteaJugador(m.getPunteaJugador().getNombre());
        manos[i].setPuntajeAgregado(m.getValorAgregado());
      }
      i++;
    }
    DataJugador res = new DataJugador(aj.getNombre(), mapj.getPuntaje(), manos);
    res.setEliminado(mapj.getEliminado());
    res.setPosicion(mapj.getPosicion());
    return res;
  }

  @Override
  public void agregarMano(int index, String jugador, List<String> cartas) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    List<Partida> parts = mp.getPartidas();
    Partida p = null;
    p = mp.getPartida(index);

    RealmList<mapJugadores> aJgs = p.getJugadores();
    mapJugadores mapj = null;
    Jugador aj = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugador)) {
        aj = mj.getJugadores().get(j.getNombre());
        mapj = j;
        break;
      }
    }
    RealmList<Carta> nCartas = new RealmList<Carta>();
    int valorMano = 0;
    Regla regla = p.getReglas();
    List<Carta> crts = regla.getCartas();
    for(String c : cartas) {
      for (Carta lCrt : crts) {
        if (lCrt.getTipo().getEnum().toString().equals(c)) {
          valorMano = valorMano + lCrt.getValor();
          nCartas.add(lCrt);
          break;
        }
      }
    }
    Mano nueva = new Mano(valorMano, nCartas);
    Realm realm = Realm.getDefaultInstance();
    Partida finalP = p;
    mapJugadores finalMapj = mapj;
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        finalMapj.getManos().add(nueva);
        finalMapj.setPuntaje(finalMapj.getPuntaje() + nueva.getValor());
        if (finalMapj.getPuntaje() >= finalP.getPuntos()) {
          posicionarJugador(finalP, finalMapj.getNombre());
        }
        finalMapj.setEliminado(finalMapj.getPuntaje() >= finalP.getPuntos());
        realm.copyToRealmOrUpdate(finalP);
      }
    });

  }

  @Override
  public void agregarMano(int index, String jugador, int puntaje) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> parts = mp.getPartidas();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Partida p = null;
    p = mp.getPartida(index);
    RealmList<mapJugadores> aJgs = p.getJugadores();
    mapJugadores mapj = null;
    Jugador aj = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugador)) {
        aj = mj.getJugadores().get(j.getNombre());
        mapj = j;
        break;
      }
    }
    Mano nueva = new Mano(puntaje);
    Realm realm = Realm.getDefaultInstance();
    Partida finalP = p;
    mapJugadores finalMapj = mapj;
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        finalMapj.getManos().add(nueva);
        finalMapj.setPuntaje(finalMapj.getPuntaje() + nueva.getValor());
        if (finalMapj.getPuntaje() >= finalP.getPuntos()) {
          posicionarJugador(finalP, finalMapj.getNombre());
        }
        finalMapj.setEliminado(finalMapj.getPuntaje() >= finalP.getPuntos());
        realm.copyToRealmOrUpdate(finalP);
      }
    });
  }

  @Override
  public void agregarMano(int index, String jugador, List<String> cartas, String jugadorAPuntuar) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    List<Partida> parts = mp.getPartidas();
    Partida p = null;
    p = mp.getPartida(index);

    RealmList<mapJugadores> aJgs = p.getJugadores();
    mapJugadores mapj = null;
    Jugador aj = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugador)) {
        aj = mj.getJugadores().get(j.getNombre());
        mapj = j;
        break;
      }
    }

    mapJugadores mapjPuntuar = null;
    Jugador aPuntuar = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugadorAPuntuar)) {
        aPuntuar = mj.getJugadores().get(j.getNombre());
        mapjPuntuar = j;
        break;
      }
    }

    RealmList<Carta> nCartas = new RealmList<Carta>();
    int valorMano = 0;
    Regla regla = p.getReglas();
    List<Carta> crts = regla.getCartas();
    for(String c : cartas) {
      for (Carta lCrt : crts) {
        if (lCrt.getTipo().getEnum().toString().equals(c)) {
          valorMano = valorMano + lCrt.getValor();
          nCartas.add(lCrt);
          break;
        }
      }
    }

    Mano nueva = new Mano(0, nCartas);
    nueva.setPunteaJugador(aPuntuar);
    nueva.setValorAgregado(valorCartas(index, cartas));
    Realm realm = Realm.getDefaultInstance();
    Partida finalP = p;
    mapJugadores finalMapj = mapj;
    mapJugadores finalMapjPuntuar = mapjPuntuar;
    int finalValorMano = valorMano;
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        finalMapj.getManos().add(nueva);
        realm.copyToRealmOrUpdate(finalP);
      }
    });
  }

  @Override
  public void agregarMano(int index, String jugador, int puntaje, String jugadorAPuntuar) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> parts = mp.getPartidas();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Partida p = null;
    p = mp.getPartida(index);

    RealmList<mapJugadores> aJgs = p.getJugadores();
    mapJugadores mapj = null;
    Jugador aj = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugador)) {
        aj = mj.getJugadores().get(j.getNombre());
        mapj = j;
        break;
      }
    }

    mapJugadores mapjPuntuar = null;
    Jugador aPuntuar = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugadorAPuntuar)) {
        aPuntuar = mj.getJugadores().get(j.getNombre());
        mapjPuntuar = j;
        break;
      }
    }
    Mano nueva = new Mano(0);
    nueva.setValorAgregado(puntaje);
    nueva.setPunteaJugador(aPuntuar);
    Realm realm = Realm.getDefaultInstance();
    Partida finalP = p;

    mapJugadores finalMapjPuntuar = mapjPuntuar;
    mapJugadores finalMapj = mapj;
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        finalMapj.getManos().add(nueva);
        realm.copyToRealmOrUpdate(finalP);
      }
    });
  }

  @Override
  public void agregarMano(int index, String jugador, int puntaje, Boolean ayuda) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> parts = mp.getPartidas();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Partida p = null;
    p = mp.getPartida(index);

    RealmList<mapJugadores> aJgs = p.getJugadores();
    mapJugadores mapj = null;
    Jugador aj = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugador)) {
        aj = mj.getJugadores().get(j.getNombre());
        mapj = j;
        break;
      }
    }

    Mano nueva = new Mano(puntaje);
    nueva.setPunteaJugador(aj);
    Realm realm = Realm.getDefaultInstance();
    Partida finalP = p;

    mapJugadores finalMapj = mapj;
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        finalMapj.setPuntaje(finalMapj.getPuntaje() + puntaje);
        if (finalMapj.getPuntaje() >= finalP.getPuntos()) {
          posicionarJugador(finalP, finalMapj.getNombre());
          if(finalMapj.getPosicion() == 1) {
            finalP.setGanador(finalMapj.getNombre());
          }
        }
        finalMapj.setEliminado(finalMapj.getPuntaje() >= finalP.getPuntos());
        finalMapj.getManos().add(nueva);
        realm.copyToRealmOrUpdate(finalP);
      }
    });
  }

  @Override
  public int getUltimaPartida() {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    return mp.getUltimaPartida().getId();
  }

  @Override
  public DataPartida[] listarPartidas() {
    ManejadorPartidas mr = ManejadorPartidas.getInstance();
    List<Partida> parts = mr.getPartidas();
    DataPartida[] res = new DataPartida[parts.size()];
    for (int i = 0; i < parts.size(); i++) {
      res[i] = getDatosPartida(parts.get(i).getId());
    }
    return res;
  }

  @Override
  public String getMayorPuntuado(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    mapJugadores mayor = jugadores.first();
    for (mapJugadores j : jugadores) {
      if (j.getPuntaje() >= mayor.getPuntaje()) {
        mayor = j;
      }
    }
    return mayor.getNombre();
  }

  @Override
  public String getMenorPuntuado(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    mapJugadores menor = jugadores.first();
    for (mapJugadores j : jugadores) {
      if (j.getPuntaje() <= menor.getPuntaje()) {
        menor = j;
      }
    }
    return menor.getNombre();
  }

  @Override
  public Boolean hayEmpate(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    //if (p.getPuntuarIndividual()) {
      for(int i = 0; i < jugadores.size(); i++) {
        if (jugadores.get(i).getEliminado()) {
          continue;
        }
        for(int j = i ; j < jugadores.size(); j++) {
          if (jugadores.get(j).getEliminado() || jugadores.get(j).getNombre().equals(jugadores.get(i).getNombre())) {
            continue;
          }
          if (jugadores.get(i).getPuntaje() == jugadores.get(j).getPuntaje()) {
            return true;
          }
        }
      }
      return false;
  }

  @Override
  public Boolean noHayEmpatadosMinimos(int id) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(id);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    if (p.getPuntuarIndividual()) {
      int puntajeMenor = 0;
      String menor = getMenorPuntuado(id);
      for (mapJugadores j : jugadores) {
        if (j.getNombre().equals(menor)) {
          puntajeMenor = j.getPuntaje();
        }
      }
      for (mapJugadores j : jugadores) {
        if (!j.getNombre().equals(menor) && j.getPuntaje() == puntajeMenor) {
          return false;
        }
      }
      return true;
    } else {
      int puntajeMayor = 0;
      String mayor = getMayorPuntuado(id);
      for (mapJugadores j : jugadores) {
        if (j.getNombre().equals(mayor)) {
          puntajeMayor = j.getPuntaje();
        }
      }
      for (mapJugadores j : jugadores) {
        if (!j.getNombre().equals(mayor) && j.getPuntaje() == puntajeMayor) {
          return false;
        }
      }
      return true;
    }
  }


  @Override
  public List<String> getEmpatados(int index){
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    if (p.getPuntuarIndividual()) {
      String mayor = getMayorPuntuado(index);
      DataJugador dj = getDatosJugador(index, mayor);
      List<String> res = new ArrayList<String>();
      int puntaje = dj.getPuntaje();
      for (mapJugadores j : jugadores) {
        if (puntaje == j.getPuntaje()) {
          res.add(j.getNombre());
        }
      }
      return res;
    } else {
      String menor = getMenorPuntuado(index);
      DataJugador dj = getDatosJugador(index, menor);
      List<String> res = new ArrayList<String>();
      int puntaje = dj.getPuntaje();
      for (mapJugadores j : jugadores) {
        if (puntaje == j.getPuntaje()) {
          res.add(j.getNombre());
        }
      }
      return res;
    }
  }

  @Override
  public List<Integer> getPartidasFinalizadas(String tipoJuego) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> partidas = mp.getPartidas();
    List<Integer> res = new ArrayList<Integer>();
    for (Partida p : partidas) {
      if (p.getEstado().equals("Finalizada")) {
        if (tipoJuego.equals("All") || ((p.getFlip() && tipoJuego.equals("Uno Flip")) || !p.getFlip() && tipoJuego.equals("Uno Classic"))) {
          res.add(p.getId());
        }
      }
    }
    return res;
  }

  @Override
  public List<Integer> getPartidasEnCurso(String tipoJuego) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> partidas = mp.getPartidas();
    List<Integer> res = new ArrayList<Integer>();
    for (Partida p : partidas) {
      if (p.getEstado().equals("En Curso")) {
        if (tipoJuego.equals("All") || ((p.getFlip() && tipoJuego.equals("Uno Flip")) || !p.getFlip() && tipoJuego.equals("Uno Classic"))) {
          res.add(p.getId());
        }
      }
    }
    return res;
  }

  @Override
  public void finalizarPartida(int index, Boolean ganador) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        RealmList<mapJugadores> jugadores = p.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
          jugadores.get(i).setPosicion(i + 1);
        }
        realm.copyToRealmOrUpdate(p);
      }
    });
    ordenarPosiciones(index);
    if (ganador) {
      RealmList<mapJugadores> jugadores = p.getJugadores();
      for (mapJugadores j : jugadores) {
        if (j.getPosicion() == 1) {
          mp.finalizarPartida(p, j.getNombre());
          ManejadorJugadores mj = ManejadorJugadores.getInstance();
          Jugador jg = mj.getJugador(j.getNombre());
          realm.beginTransaction();
          jg.setPartidasGanadas(jg.getPartidasGanadas() + 1);
          realm.copyToRealmOrUpdate(jg);
          realm.commitTransaction();
          break;
        }
      }
    } else {
      mp.finalizarPartida(p, null);
    }
  }

  @Override
  public Boolean hayPartidasFinalizadas() {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> partidas = mp.getPartidas();
    for (Partida p : partidas) {
      if (p.getEstado().equals("Finalizada")) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Boolean hayPartidasEnCurso() {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> partidas = mp.getPartidas();
    for (Partida p : partidas) {
      if (p.getEstado().equals("En Curso")) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Boolean hayPartidaCargada() {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    return (mp.getCargada() != null);
  }

  @Override
  public int getUltimaPartidaCargada() {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getCargada();
    return p.getId();
  }

  @Override
  public void marcarPartidaCargada(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    mp.marcarCargada(p);
  }

  @Override
  public Boolean hayGanador(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    if (p.getPuntuarIndividual()) {
      for (mapJugadores j : jugadores) {
        if (j.getPuntaje() >= p.getPuntos()) {
          return true;
        }
      }
      return false;
    } else {
      return p.getGanador() != null;
    }
  }

  @Override
  public int valorCartas(int index, List<String> cartas) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    int valorMano = 0;
    Regla regla = p.getReglas();
    List<Carta> crts = regla.getCartas();
    for(String c : cartas) {
      for (Carta lCrt : crts) {
        if (lCrt.getTipo().getEnum().toString().equals(c)) {
          valorMano = valorMano + lCrt.getValor();
          break;
        }
      }
    }
    return valorMano;
  }

  @Override
  public void editarMano(int id, String nombreJugador, List<String> seleccionadas, int posMano) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Realm realm = Realm.getDefaultInstance();
    Partida p = mp.getPartida(id);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        int valorAnterior;
        for (mapJugadores j : jugadores) {
          if (j.getNombre().equals(nombreJugador)) {
            Mano mano = j.getManos().get(posMano);
            valorAnterior = mano.getValor();
            RealmList<Carta> cartas = new RealmList<Carta>();
            int valorMano = 0;
            Regla regla = p.getReglas();
            List<Carta> crts = regla.getCartas();
            for(String c : seleccionadas) {
              for (Carta lCrt : crts) {
                if (lCrt.getTipo().getEnum().toString().equals(c)) {
                  valorMano = valorMano + lCrt.getValor();
                  cartas.add(lCrt);
                  break;
                }
              }
            }
            j.setPuntaje(j.getPuntaje() - valorAnterior + valorMano);
            mano.setCartas(cartas);
            mano.setValor(valorMano);
            realm.copyToRealmOrUpdate(p);
            break;
          }
        }
      }
    });
  }

  @Override
  public void editarMano(int id, String nombreJugador, int puntaje, int posMano) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Realm realm = Realm.getDefaultInstance();
    Partida p = mp.getPartida(id);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        int valorAnterior;
        for (mapJugadores j : jugadores) {
          if (j.getNombre().equals(nombreJugador)) {
            j.getManos().get(posMano).getCartas().clear();
            valorAnterior = j.getManos().get(posMano).getValor();
            j.getManos().get(posMano).setValor(puntaje);
            j.setPuntaje(j.getPuntaje() - valorAnterior + puntaje);
            realm.copyToRealmOrUpdate(p);
            break;
          }
        }
      }
    });
  }

  @Override
  public void editarMano(int id, String nombreJugador, List<String> seleccionadas, int posMano, Boolean ayuda) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Realm realm = Realm.getDefaultInstance();
    List<Partida> parts = mp.getPartidas();
    Partida p = null;
    p = mp.getPartida(id);

    RealmList<mapJugadores> aJgs = p.getJugadores();
    mapJugadores mapj = null;
    Jugador aj = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(nombreJugador)) {
        aj = mj.getJugadores().get(j.getNombre());
        mapj = j;
        break;
      }
    }

    Mano jugador = mapj.getManos().get(posMano);
    String jugadorAPuntuar = jugador.getPunteaJugador().getNombre();

    mapJugadores mapjPuntuar = null;
    Jugador aPuntuar = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugadorAPuntuar)) {
        aPuntuar = mj.getJugadores().get(j.getNombre());
        mapjPuntuar = j;
        break;
      }
    }

    RealmList<Carta> nCartas = new RealmList<Carta>();
    int valorMano = 0;
    Regla regla = p.getReglas();
    List<Carta> crts = regla.getCartas();
    for(String c : seleccionadas) {
      for (Carta lCrt : crts) {
        if (lCrt.getTipo().getEnum().toString().equals(c)) {
          valorMano = valorMano + lCrt.getValor();
          nCartas.add(lCrt);
          break;
        }
      }
    }

    Mano puntuar = mapjPuntuar.getManos().get(posMano);

    Partida finalP = p;
    mapJugadores finalMapjPuntuar = mapjPuntuar;
    int finalValorMano = valorMano;
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        finalMapjPuntuar.setPuntaje((finalMapjPuntuar.getPuntaje() - jugador.getValorAgregado()) + finalValorMano);
        jugador.setCartas(nCartas);
        puntuar.setValor(puntuar.getValor() -  jugador.getValorAgregado() + finalValorMano);

        jugador.setValor(0);
        jugador.setValorAgregado(valorCartas(id, seleccionadas));
        realm.copyToRealmOrUpdate(finalP);
      }
    });
  }

  @Override
  public void editarMano(int id, String nombreJugador, int puntaje, int posMano, Boolean ayuda) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Realm realm = Realm.getDefaultInstance();
    List<Partida> parts = mp.getPartidas();
    Partida p = null;
    p = mp.getPartida(id);

    RealmList<mapJugadores> aJgs = p.getJugadores();
    mapJugadores mapj = null;
    Jugador aj = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(nombreJugador)) {
        aj = mj.getJugadores().get(j.getNombre());
        mapj = j;
        break;
      }
    }

    Mano jugador = mapj.getManos().get(posMano);
    String jugadorAPuntuar = jugador.getPunteaJugador().getNombre();

    mapJugadores mapjPuntuar = null;
    Jugador aPuntuar = null;
    for (mapJugadores j : aJgs) {
      if (j.getNombre().equals(jugadorAPuntuar)) {
        aPuntuar = mj.getJugadores().get(j.getNombre());
        mapjPuntuar = j;
        break;
      }
    }

    Mano puntuar = mapjPuntuar.getManos().get(posMano);

    Partida finalP = p;
    mapJugadores finalMapjPuntuar = mapjPuntuar;
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        finalMapjPuntuar.setPuntaje((finalMapjPuntuar.getPuntaje() - jugador.getValorAgregado()) + puntaje);
        jugador.getCartas().clear();
        jugador.setValor(0);
        puntuar.setValor(puntuar.getValor() - jugador.getValorAgregado() + puntaje);
        jugador.setValorAgregado(puntaje);
        realm.copyToRealmOrUpdate(finalP);
      }
    });
  }

  @Override
  public void borrarMano(int id, int posMano) {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        ManejadorPartidas mp = ManejadorPartidas.getInstance();
        Partida p = mp.getPartida(id);
        RealmList<mapJugadores> jugadores = p.getJugadores();
          for (mapJugadores j : jugadores) {
            if (j.getManos().size() >= posMano) {
              j.setPuntaje(j.getPuntaje() - j.getManos().get(posMano - 1).getValor());
              j.setEliminado(j.getPuntaje() >= p.getPuntos());
              j.getManos().remove(posMano - 1);
            }
          }
          p.setRondas(p.getRondas() - 1);
          realm.copyToRealmOrUpdate(p);
      }
    });
    ordenarPosiciones(id);
  }

  @Override
  public void reiniciarPartida(int id) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        int i = 1;
        Partida p = mp.getPartida(id);
        p.setRondas(0);
        RealmList<mapJugadores> jugadores = p.getJugadores();
        for (mapJugadores j : jugadores) {
          j.getManos().clear();
          j.setPuntaje(0);
          j.setEliminado(false);
          j.setPosicion(i);
          i++;
        }
        p.setGanador(null);
        p.setRondas(0);
        realm.copyToRealmOrUpdate(p);
      }
    });
  }

  @Override
  public void borrarPartida(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    mp.borrarPartida(index);
  }

  @Override
  public List<String> listarNoEliminados(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    List<String> res = new ArrayList<String>();
    for(mapJugadores j : jugadores) {
      if (!j.getEliminado()) {
        res.add(j.getNombre());
      }
    }
    return res;
  }

  @Override
  public List<String> listarEliminados(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    List<String> res = new ArrayList<String>();
    for(mapJugadores j : jugadores) {
      if (j.getEliminado()) {
        res.add(j.getNombre());
      }
    }
    return res;
  }

  @Override
  public Map<Integer, String> getPosiciones(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    Map<Integer, String> res = new HashMap<Integer, String>();
    for (mapJugadores j : jugadores) {
      if (j.getEliminado()) {
        res.put(j.getPosicion(), j.getNombre());
      }
    }
    return res;
  }

  @Override
  public List<String> getTodasPosiciones(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    List<Integer> posiciones = new ArrayList<Integer>();
    List<String> res = new ArrayList<String>();
    for(mapJugadores j : jugadores) {
      posiciones.add(j.getPosicion());
      res.add(j.getNombre());
    }
    if (p.getPuntuarIndividual()) {
      for (int i = 0; i < jugadores.size(); i++) {
        for (int j = i; j < jugadores.size(); j++) {
          if (posiciones.get(i) <= posiciones.get(j)) {
            String temp1 = res.get(i);
            Integer temp2 = posiciones.get(i);
            posiciones.set(i, posiciones.get(j));
            posiciones.set(j, temp2);
            res.set(i, res.get(j));
            res.set(j, temp1);
          }
        }
      }
    } else {
      for (int i = 0; i < jugadores.size(); i++) {
        for (int j = i; j < jugadores.size(); j++) {
          if (posiciones.get(i) >= posiciones.get(j) || posiciones.get(i) == 0) {
            String temp1 = res.get(i);
            Integer temp2 = posiciones.get(i);
            posiciones.set(i, posiciones.get(j));
            posiciones.set(j, temp2);
            res.set(i, res.get(j));
            res.set(j, temp1);
          }
        }
      }
    }
    posiciones.clear();
    return res;
  }

  @Override
  public void ordenarPosiciones(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        Partida p = mp.getPartida(index);
        RealmList<mapJugadores> jugadores = p.getJugadores();
        if (p.getPuntuarIndividual()) {
          for (int i = 0; i < jugadores.size(); i++) {
            for (int j = i; j < jugadores.size(); j++) {
              if ((!jugadores.get(i).getEliminado() && !jugadores.get(j).getEliminado()) && jugadores.get(i).getPuntaje() > jugadores.get(j).getPuntaje()) {
                mapJugadores aux = jugadores.get(i);
                jugadores.set(i, jugadores.get(j));
                jugadores.set(j, aux);
              }
            }
          }
        } else {
          for (int i = 0; i < jugadores.size(); i++) {
            for (int j = i; j < jugadores.size(); j++) {
              if ((!jugadores.get(i).getEliminado() && !jugadores.get(j).getEliminado()) && jugadores.get(i).getPuntaje() < jugadores.get(j).getPuntaje()) {
                mapJugadores aux = jugadores.get(i);
                jugadores.set(i, jugadores.get(j));
                jugadores.set(j, aux);
              }
            }
          }
        }
        for(int i = 0; i < jugadores.size(); i++) {
          jugadores.get(i).setPosicion(i + 1);
        }
        realm.copyToRealmOrUpdate(p);
      }
    });
  }

  @Override
  public String getUltimoEliminado(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    if (p.getPuntuarIndividual()) {
      for (int i = 0 ; i < jugadores.size(); i++) {
        if (jugadores.get(i).getEliminado()) {
          return jugadores.get(i).getNombre();
        }
      }
      return null;
    } else {
      for (int i = jugadores.size() - 1 ; i >= 0; i--) {
        if (jugadores.get(i).getEliminado()) {
          return jugadores.get(i).getNombre();
        }
      }
      return null;
    }
  }

  @Override
  public Map<Integer, ArrayList<DataJugador>> getEmpatadosTotales(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    Map<Integer, ArrayList<DataJugador>> res = new HashMap<Integer, ArrayList<DataJugador>>();
    for (mapJugadores j : jugadores) {
      if (!j.getEliminado() && checkEmpatado(j.getNombre(), index)) {
        if (res.get(j.getPuntaje()) == null) {
          res.put(j.getPuntaje(), new ArrayList<DataJugador>());
        }
        DataJugador colocar = getDatosJugador(index, j.getNombre());
        res.get(j.getPuntaje()).add(colocar);
      }
    }
    return res;
  }

  @Override
  public void sortearEmpatados(int index) {
    Realm realm = Realm.getDefaultInstance();
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> paraver = p.getJugadores();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        RealmList<mapJugadores> jugadores = p.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
          jugadores.get(i).setPosicion(i + 1);
        }
        realm.copyToRealmOrUpdate(p);
      }
    });
    Map<Integer, ArrayList<DataJugador>> empatados = getEmpatadosTotales(index);
    //List<Integer> posiciones = new ArrayList<Integer>();
    for (Map.Entry<Integer, ArrayList<DataJugador>> entry : empatados.entrySet()) {
      sortearLista(index, entry.getValue());
    }
    ordenarPorPosicion(index);
  }

  @Override
  public List<DataJugador> getJugadores(int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    List<DataJugador> res = new ArrayList<DataJugador>();
    for (mapJugadores j : jugadores) {
      res.add(getDatosJugador(index, j.getNombre()));
    }
    return res;
  }

  @Override
  public List<String> getJugadoresMano(int index, int numeroMano) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    List<String> res = new ArrayList<String>();
    for (mapJugadores j : jugadores) {
      if (j.getManos().size() >= numeroMano && j.getManos().get(numeroMano - 1) != null) {
        res.add(j.getNombre());
      }
    }
    return res;
  }

  @Override
  public void sumarRonda(int id) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(id);
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    p.setRondas(p.getRondas() + 1);
    realm.copyToRealmOrUpdate(p);
    realm.commitTransaction();
  }

  @Override
  public int getRondas(int id) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(id);
    return p.getRondas();
  }

  private void ordenarPorPosicion(int index) {
    Realm realm = Realm.getDefaultInstance();
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        RealmList<mapJugadores> jugadores = p.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
          for (int j = i; j < jugadores.size(); j++) {
            if (jugadores.get(i).getPosicion() > jugadores.get(j).getPosicion()) {
              mapJugadores aux = jugadores.get(i);
              jugadores.set(i, jugadores.get(j));
              jugadores.set(j, aux);
            }
          }
        }
        realm.copyToRealmOrUpdate(p);
      }
    });
  }

  private void sortearLista(int index, ArrayList<DataJugador> empatados) {
    Realm realm = Realm.getDefaultInstance();
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        RealmList<mapJugadores> jugadores = p.getJugadores();
        List<Integer> posiciones = new ArrayList<Integer>();
        for (DataJugador j : empatados) {
          posiciones.add(j.getPosicion());
        }
        Collections.shuffle(posiciones);
        for (DataJugador j : empatados) {
          for (mapJugadores mapJ : jugadores) {
            if (mapJ.getNombre().equals(j.getNombre())) {
              mapJ.setPosicion(posiciones.get(0));
              posiciones.remove(0);
              mapJ.setEliminado(true);
              break;
            }
          }
        }
        realm.copyToRealmOrUpdate(p);
      }
    });
  }

  private boolean checkEmpatado(String nombre, int index) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Partida p = mp.getPartida(index);
    RealmList<mapJugadores> jugadores = p.getJugadores();
    mapJugadores chequear = null;
    for (mapJugadores j : jugadores) {
      if (j.getNombre().equals(nombre)) {
        chequear = j;
        break;
      }
    }
    for (mapJugadores j : jugadores) {
      if (!j.getEliminado() && !j.getNombre().equals(chequear.getNombre()) && j.getPuntaje() == chequear.getPuntaje()) {
        return true;
      }
    }
    return false;
  }
}
