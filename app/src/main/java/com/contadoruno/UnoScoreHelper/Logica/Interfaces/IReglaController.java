package com.contadoruno.UnoScoreHelper.Logica.Interfaces;

import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.NombreVacio;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.ReglaYaExiste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IReglaController {
  public abstract void altaRegla(String nombre, Boolean clasicas) throws ReglaYaExiste, NombreVacio;
  public abstract void altaRegla(String nombre, Boolean flip, Map<String, Integer> cartas) throws ReglaYaExiste;
  public abstract void modificarRegla(String nombre, Map<String, Integer> cartas);
  public abstract DataRegla getDatosRegla(String nombre);
  public abstract List<String> listarReglas();
  public abstract List<String> listarReglas(Boolean flip);
  public abstract Boolean reglaTerminada(String nombre);
  public abstract HashMap<String, ArrayList<String>> getCartasSinPuntos(String nombre);
  public abstract void completarReglas(String nombre);
  public abstract Map<String, Integer> reglasClasicas();
  public abstract Map<String, Integer> reglasFlip();
  public abstract Boolean soloWilds(String nombre);
  public abstract void editarRegla(String nombre, Map<String, Integer> cartasAgregadas);
  public abstract void borrarRegla(String nombre);
}
