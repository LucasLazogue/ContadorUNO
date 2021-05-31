package com.contadoruno.UnoScoreHelper.Logica.Controllers;

import com.contadoruno.UnoScoreHelper.Logica.Classes.Jugador;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Partida;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.JugadorYaExiste;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.NombreVacio;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IJugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.ManejadorJugadores;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.ManejadorPartidas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

public class JugadorController implements IJugadorController {
  @Override
  public String[] getJugadores() {
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Map<String, Jugador> js = mj.getJugadores();
    String[] res = new String[js.size()];
    int i = 0;
    for (Map.Entry<String, Jugador> entry : js.entrySet()) {
      res[i] = entry.getKey();
      i++;
    }
    return res;
  }

  @Override
  public void crearJugador(String nombre) throws JugadorYaExiste, NombreVacio {
    ManejadorJugadores mp = ManejadorJugadores.getInstance();

    if (mp.getJugadores().containsKey(nombre)) {
      throw new JugadorYaExiste("El jugador " + nombre + " ya existe");
    }

    if (nombre.equals("")){
      throw new NombreVacio("El nombre no puede ser vacio");
    }

    Jugador nuevo = new Jugador(nombre);
    mp.addJugador(nuevo);
  }

  @Override
  public void eliminarJugador(String nombre) {
    ManejadorJugadores mp = ManejadorJugadores.getInstance();
    mp.eliminarJugador(nombre);
  }

  @Override
  public List<Integer> getPartidasJugadas(String nombre) {
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Jugador j = mj.getJugador(nombre);
    List<Integer> res = new ArrayList<Integer>();
    RealmList<Partida> parts = j.getPartidas();
    for (Partida p : parts) {
      res.add(p.getId());
    }
    return res;
  }

  @Override
  public List<Integer> getPartidasGanadas(String nombre) {
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Integer> parts = getPartidasJugadas(nombre);
    List<Integer> res = new ArrayList<Integer>();
    for (Integer i : parts){
      Partida p = mp.getPartida(i);
      if (p.getGanador() != null && p.getGanador().equals(nombre)){
        res.add(i);
      }
    }
    return res;
  }

  @Override
  public int getCantidadJugadas(String nombre) {
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Jugador j = mj.getJugador(nombre);
    return j.getPartidasJugadas();
  }

  @Override
  public int getCantidadGanadas(String nombre) {
    ManejadorJugadores mj = ManejadorJugadores.getInstance();
    Jugador j = mj.getJugador(nombre);
    return j.getPartidasGanadas();
  }
}
