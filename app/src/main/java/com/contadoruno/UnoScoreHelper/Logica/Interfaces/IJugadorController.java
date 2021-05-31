package com.contadoruno.UnoScoreHelper.Logica.Interfaces;

import com.contadoruno.UnoScoreHelper.Logica.Exceptions.JugadorYaExiste;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.NombreVacio;

import java.util.List;

public interface IJugadorController {
  public abstract String[] getJugadores();
  public abstract void crearJugador(String nombre) throws JugadorYaExiste, NombreVacio;
  public abstract void eliminarJugador(String nombre);
  public abstract List<Integer> getPartidasJugadas(String nombre);
  public abstract List<Integer> getPartidasGanadas(String nombre);
  public abstract int getCantidadJugadas(String nombre);
  public abstract int getCantidadGanadas(String nombre);
}
