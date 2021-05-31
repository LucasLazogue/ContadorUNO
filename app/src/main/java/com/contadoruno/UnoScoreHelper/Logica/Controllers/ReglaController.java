package com.contadoruno.UnoScoreHelper.Logica.Controllers;

import com.contadoruno.UnoScoreHelper.Logica.Classes.Carta;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Partida;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Regla;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataCarta;
import com.contadoruno.UnoScoreHelper.Logica.Datatypes.DataRegla;
import com.contadoruno.UnoScoreHelper.Logica.Enums.Classic;
import com.contadoruno.UnoScoreHelper.Logica.Enums.TipoClassic;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.NombreVacio;
import com.contadoruno.UnoScoreHelper.Logica.Exceptions.ReglaYaExiste;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.Factory;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.ManejadorPartidas;
import com.contadoruno.UnoScoreHelper.Logica.Singleton.ManejadorReglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;

public class ReglaController implements IReglaController {
  @Override
  public void altaRegla(String nombre, Boolean clasicas) throws ReglaYaExiste, NombreVacio {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Map<String, Regla> reglas = mr.getReglas();
    if (reglas.get(nombre) != null) {
      throw new ReglaYaExiste("La regla " + nombre + " ya existe");
    }

    if (nombre.equals("")) {
      throw new NombreVacio("La regla " + nombre + " ya existe");
    }
    Regla nueva = new Regla(nombre);
    nueva.setEditable(false);
    if (clasicas) {
      nueva.setSoloWilds(true);
      Map<String, Integer> reglasClasicas = reglasClasicas();
      for(Map.Entry<String, Integer> e : reglasClasicas.entrySet()) {
        TipoClassic tipo = new TipoClassic();
        tipo.saveEnum(Classic.valueOf(e.getKey()));
        nueva.getCartas().add(new Carta(tipo, e.getValue()));
        nueva.setFlip(false);
      }
    } else {
      nueva.setSoloWilds(true);
      Map<String, Integer> reglasFlip = reglasFlip();
      for(Map.Entry<String, Integer> e : reglasFlip.entrySet()) {
        TipoClassic tipo = new TipoClassic();
        tipo.saveEnum(Classic.valueOf(e.getKey()));
        nueva.getCartas().add(new Carta(tipo, e.getValue()));
        nueva.setFlip(true);
      }
    }
    mr.addRegla(nueva);
  }

  @Override
  public void altaRegla(String nombre, Boolean flip, Map<String, Integer> cartas) throws ReglaYaExiste {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Map<String, Regla> reglas = mr.getReglas();
    if (reglas.get(nombre) != null) {
      throw new ReglaYaExiste("La regla " + nombre + " ya existe");
    }
    Regla nueva = new Regla(nombre);
    for(Map.Entry<String, Integer> e : cartas.entrySet()) {
      TipoClassic tipo = new TipoClassic();
      tipo.saveEnum(Classic.valueOf(e.getKey()));
      nueva.getCartas().add(new Carta(tipo, e.getValue()));
      nueva.setFlip(flip);
    }
    mr.addRegla(nueva);
  }

  @Override
  public void modificarRegla(String nombre, Map<String, Integer> cartas) {

    ManejadorReglas mr = ManejadorReglas.getInstance();
    Regla regla = mr.getReglas().get(nombre);
    List<Carta> lst = regla.getCartas();
    if (cartas.size() == 1 || cartas.size() == 2) {
      if (cartas.size() == 1) {
        if (cartas.containsKey("Wild 4") || cartas.containsKey("Wild")){
          Realm realm = Realm.getDefaultInstance();
          realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
              regla.setSoloWilds(true);
              realm.copyToRealmOrUpdate(regla);
            }
          });
        }
      }
      else {
        if (cartas.containsKey("Wild 4") && cartas.containsKey("Wild")) {
          Realm realm = Realm.getDefaultInstance();
          regla.setSoloWilds(true);
          realm.copyToRealmOrUpdate(regla);
        }
      }
    }
    for(Map.Entry<String, Integer> e : cartas.entrySet()) {
      regla.removerCarta(e.getKey());
      TipoClassic tipo = new TipoClassic();
      tipo.saveEnum(Classic.valueOf(traducirNombre(e.getKey())));
      Carta nueva = new Carta(tipo, e.getValue());

      Realm realm = Realm.getDefaultInstance();
      realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
          regla.getCartas().add(nueva);
          realm.copyToRealmOrUpdate(regla);
        }
      });
    }
  }

  @Override
  public DataRegla getDatosRegla(String nombre) {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Regla regla = mr.getReglas().get(nombre);
    DataCarta[] cartas = new DataCarta[regla.getCartas().size()];
    int i = 0;
    for (Carta carta : regla.getCartas()) {
      cartas[i] = new DataCarta(carta.getTipo().getEnum().toString(), carta.getValor());
      i++;
    }
    DataRegla res = new DataRegla(regla.getNombre(), cartas);
    res.setEditable(regla.getEditable());
    res.setFlip(regla.getFlip());
    return res;
  }



  @Override
  public List<String> listarReglas() {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Map<String, Regla> reglas = mr.getReglas();
    List<String> res = new ArrayList<String>();
    for (Map.Entry<String, Regla> entry : reglas.entrySet()) {
      res.add(entry.getKey());
    }
    return res;
  }

  @Override
  public List<String> listarReglas(Boolean flip) {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Map<String, Regla> reglas = mr.getReglas();
    List<String> res = new ArrayList<String>();
    for (Map.Entry<String, Regla> entry : reglas.entrySet()) {
      if (entry.getValue().getFlip() == flip) {
        res.add(entry.getKey());
      }
    }
    return res;
  }

  @Override
  public Boolean reglaTerminada(String nombre) {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Regla regla = mr.getReglas().get(nombre);
    List<Carta> cartas = regla.getCartas();
    return cartas.size() == 54;
  }

  @Override
  public HashMap<String, ArrayList<String>> getCartasSinPuntos(String nombre) {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Regla regla = mr.getReglas().get(nombre);
    HashMap<String, ArrayList<String>> cartasTotales = cargarCartas();
    List<Carta> cartas = regla.getCartas();
    for (Carta c : cartas){
      String nCarta = traducirTipo(c.getTipo().toString());
      for(Map.Entry<String, ArrayList<String>> e : cartasTotales.entrySet()){
        if (e.getValue().contains(nCarta)) {
          cartasTotales.get(e.getKey()).remove(nCarta);
          break;
        }
      }
    }
  return cartasTotales;
  }

  @Override
  public void completarReglas(String nombre) {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Regla regla = mr.getReglas().get(nombre);
    List<Carta> cargadas = regla.getCartas();
    List<String> auxCargadas = new ArrayList<String>();
    for (Carta c : cargadas) {
      auxCargadas.add(c.getTipo().toString());
    }

    Map<String, Integer> clasicas = reglasClasicas();
    for(Map.Entry<String, Integer> e : clasicas.entrySet()) {
      if (!auxCargadas.contains(e.getKey())) {
        TipoClassic tipo = new TipoClassic();
        tipo.saveEnum(Classic.valueOf(e.getKey()));
        Carta nueva = new Carta(tipo, e.getValue());
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
          @Override
          public void execute(Realm realm) {
            regla.getCartas().add(nueva);
            realm.copyToRealmOrUpdate(regla);
          }
        });
      }
    }
  }

  @Override
  public Map<String, Integer> reglasClasicas(){
    Map<String, Integer> res = new HashMap<String, Integer>();

    res.put(Classic.CEROR.toString(), 0);
    res.put(Classic.UNOR.toString(), 1);
    res.put(Classic.DOSR.toString(), 2);
    res.put(Classic.TRESR.toString(), 3);
    res.put(Classic.CUATROR.toString(), 4);
    res.put(Classic.CINCOR.toString(), 5);
    res.put(Classic.SEISR.toString(), 6);
    res.put(Classic.SIETER.toString(), 7);
    res.put(Classic.OCHOR.toString(), 8);
    res.put(Classic.NUEVER.toString(), 9);
    res.put(Classic.SKIPR.toString(), 20);
    res.put(Classic.REVERSER.toString(), 20);
    res.put(Classic.DRAW2R.toString(), 20);
    res.put(Classic.WILD.toString(), 50);
    res.put(Classic.WILD4.toString(), 50);

    return res;
  }

  @Override
  public Map<String, Integer> reglasFlip(){
    Map<String, Integer> res = reglasClasicas();
    res.put(Classic.DRAW1FL.toString(), 10);
    res.put(Classic.DRAW5FD.toString(), 20);
    res.put(Classic.REVERSED.toString(), 20);
    res.put(Classic.FLIPD.toString(), 20);
    res.put(Classic.FLIPL.toString(), 20);
    res.put(Classic.SKIPD.toString(), 30);
    res.put(Classic.DRAW1FL.toString(), 10);
    res.put(Classic.WILDF.toString(), 40);
    res.put(Classic.WILDDRAW2.toString(), 50);
    res.put(Classic.WILDDRAWC.toString(), 60);
    return res;
  }

  @Override
  public Boolean soloWilds(String nombre) {
    ManejadorReglas mr = ManejadorReglas.getInstance();
    Regla regla = mr.getReglas().get(nombre);
    return regla.getSoloWilds();
  }

  @Override
  public void editarRegla(String nombre, Map<String, Integer> cartasAgregadas) {
    Realm realm = Realm.getDefaultInstance();
    Factory f = Factory.getInstance();
    IPartidaController pc = f.getIPartidaController();
    ManejadorReglas mr = ManejadorReglas.getInstance();
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    List<Partida> lstParts = mp.getPartidas();
    Map<String, Regla> mapr = mr.getReglas();
    Regla r = mapr.get(nombre);
    for (Partida p : lstParts) {
      if (p.getReglas().getNombre().equals(nombre)) {
        pc.reiniciarPartida(p.getId());
      }
    }
    RealmList<Carta> cartas = new RealmList<Carta>();
    for(Map.Entry<String, Integer> e : cartasAgregadas.entrySet()) {
      TipoClassic tipo = new TipoClassic();
      tipo.saveEnum(Classic.valueOf(e.getKey()));
      cartas.add(new Carta(tipo, e.getValue()));
    }
    realm.beginTransaction();
    r.getCartas().clear();
    for (Carta c : cartas) {
      r.getCartas().add(c);
    }
    realm.copyToRealmOrUpdate(r);
    realm.commitTransaction();
  }

  @Override
  public void borrarRegla(String nombre) {
    Factory f = Factory.getInstance();
    ManejadorReglas mr = ManejadorReglas.getInstance();
    ManejadorPartidas mp = ManejadorPartidas.getInstance();
    Map<String, Regla> reglas = mr.getReglas();
    Regla r = reglas.get(nombre);
    IPartidaController pc = f.getIPartidaController();
    List<Partida> lstParts = mp.getPartidas();
    for (Partida p : lstParts) {
      if (p.getReglas().getNombre().equals(nombre)) {
        pc.borrarPartida(p.getId());
      }
    }
    mr.borrarRegla(r);
  }

  private String traducirTipo(String nombre) {
    switch (nombre){
      case "WILD":
        return "Wild";
      case "WILD4":
        return "Wild 4";

      case "CEROR":
        return "0 Rojo";
      case "CEROB":
        return "0 Azul";
      case "CEROG":
        return "0 Verde";
      case "CEROY":
        return "0 Amarillo";

      case "UNOR":
        return "1 Rojo";
      case "UNOB":
        return "1 Azul";
      case "UNOG":
        return "1 Verde";
      case "UNOY":
        return "1 Amarillo";

      case "DOSR":
        return "2 Rojo";
      case "DOSB":
        return "2 Azul";
      case "DOSG":
        return "2 Verde";
      case "DOSY":
        return "2 Amarillo";

      case "TRESR":
        return "3 Rojo";
      case "TRESB":
        return "3 Azul";
      case "TRESG":
        return "3 Verde";
      case "TRESY":
        return "3 Amarillo";

      case "CUATROR":
        return "4 Rojo";
      case "CUATROB":
        return "4 Azul";
      case "CUATROG":
        return "4 Verde";
      case "CUATROY":
        return "4 Amarillo";

      case "CINCOR":
        return "5 Rojo";
      case "CINCOB":
        return "5 Azul";
      case "CINCOG":
        return "5 Verde";
      case "CINCOY":
        return "5 Amarillo";

      case "SEISR":
        return "6 Rojo";
      case "SEISB":
        return "6 Azul";
      case "SEISG":
        return "6 Verde";
      case "SEISY":
        return "6 Amarillo";

      case "SIETER":
        return "7 Rojo";
      case "SIETEB":
        return "7 Azul";
      case "SIETEG":
        return "7 Verde";
      case "SIETEY":
        return "7 Amarillo";

      case "OCHOR":
        return "8 Rojo";
      case "OCHOB":
        return "8 Azul";
      case "OCHOG":
        return "8 Verde";
      case "OCHOY":
        return "8 Amarillo";

      case "NUEVER":
        return "9 Rojo";
      case "NUEVEB":
        return "9 Azul";
      case "NUEVEG":
        return "9 Verde";
      case "NUEVEY":
        return "9 Amarillo";

      case "REVERSER":
        return "Reverse Rojo";
      case "REVERSEB":
        return "Reverse Azul";
      case "REVERSEG":
        return "Reverse Verde";
      case "REVERSEY":
        return "Reverse Amarillo";

      case "DRAW2R":
        return "+2 Rojo";
      case "DRAW2B":
        return "+2 Azul";
      case "DRAW2G":
        return "+2 Verde";
      case "DRAW2Y":
        return "+2 Amarillo";

      case "SKIPR":
        return "Skip Rojo";
      case "SKIPB":
        return "Skip Azul";
      case "SKIPG":
        return "Skip Verde";
      case "SKIPY":
        return "Skip Amarillo";
      default:
        return null;
    }
  }

  private String traducirNombre(String nombre) {
    switch (nombre){
      case "Wild":
        return "WILD";
      case "Wild 4":
        return "WILD4";

      case "0 Rojo":
        return "CEROR";
      case "0 Azul":
        return "CEROB";
      case "0 Verde":
        return "CEROG";
      case "0 Amarillo":
        return "CEROY";

      case "1 Rojo":
        return "UNOR";
      case "1 Azul":
        return "UNOB";
      case "1 Verde":
        return "UNOG";
      case "1 Amarillo":
        return "UNOY";

      case "2 Rojo":
        return "DOSR";
      case "2 Azul":
        return "DOSB";
      case "2 Verde":
        return "DOSG";
      case "2 Amarillo":
        return "DOSY";

      case "3 Rojo":
        return "TRESR";
      case "3 Azul":
        return "TRESB";
      case "3 Verde":
        return "TRESG";
      case "3 Amarillo":
        return "TRESY";

      case "4 Rojo":
        return "CUATROR";
      case "4 Azul":
        return "CUATROB";
      case "4 Verde":
        return "CUATROG";
      case "4 Amarillo":
        return "CUATROY";

      case "5 Rojo":
        return "CINCOR";
      case "5 Azul":
        return "CINCOB";
      case "5 Verde":
        return "CINCOG";
      case "5 Amarillo":
        return "CINCOY";

      case "6 Rojo":
        return "SEISR";
      case "6 Azul":
        return "SEISB";
      case "6 Verde":
        return "SEISG";
      case "6 Amarillo":
        return "SEISY";

      case "7 Rojo":
        return "SIETER";
      case "7 Azul":
        return "SIETEB";
      case "7 Verde":
        return "SIETEG";
      case "7 Amarillo":
        return "SIETEY";

      case "8 Rojo":
        return "OCHOR";
      case "8 Azul":
        return "OCHOB";
      case "8 Verde":
        return "OCHOG";
      case "8 Amarillo":
        return "OCHOY";

      case "9 Rojo":
        return "NUEVER";
      case "9 Azul":
        return "NUEVEB";
      case "9 Verde":
        return "NUEVEG";
      case "9 Amarillo":
        return "NUEVEY";

      case "Reverse Rojo":
        return "REVERSER";
      case "Reverse Azul":
        return "REVERSEB";
      case "Reverse Verde":
        return "REVERSEG";
      case "Reverse Amarillo":
        return "REVERSEY";

      case "+2 Rojo":
        return "DRAW2R";
      case "+2 Azul":
        return "DRAW2B";
      case "+2 Verde":
        return "DRAW2G";
      case "+2 Amarillo":
        return "DRAW2Y";

      case "Skip Rojo":
        return "SKIPR";
      case "Skip Azul":
        return "SKIPB";
      case "Skip Verde":
        return "SKIPG";
      case "Skip Amarillo":
        return "SKIPY";
      default:
        return null;
    }
  }

  private HashMap<String, ArrayList<String>> cargarCartas() {

    //LISTA DE TODAS LAS CARTAS
    HashMap<String, ArrayList<String>> cartasTotales = new HashMap<String, ArrayList<String>>();
    ArrayList<String> azules = new ArrayList<String>();
    ArrayList<String> amarillas = new ArrayList<String>();
    ArrayList<String> rojas = new ArrayList<String>();
    ArrayList<String> verdes = new ArrayList<String>();
    ArrayList<String> wilds = new ArrayList<String>();

    azules.add("0 Azul");
    azules.add("1 Azul");
    azules.add("2 Azul");
    azules.add("3 Azul");
    azules.add("4 Azul");
    azules.add("5 Azul");
    azules.add("6 Azul");
    azules.add("7 Azul");
    azules.add("8 Azul");
    azules.add("9 Azul");
    azules.add("+2 Azul");
    azules.add("Reverse Azul");
    azules.add("Skip Azul");

    amarillas.add("0 Amarillo");
    amarillas.add("1 Amarillo");
    amarillas.add("2 Amarillo");
    amarillas.add("3 Amarillo");
    amarillas.add("4 Amarillo");
    amarillas.add("5 Amarillo");
    amarillas.add("6 Amarillo");
    amarillas.add("7 Amarillo");
    amarillas.add("8 Amarillo");
    amarillas.add("9 Amarillo");
    amarillas.add("+2 Amarillo");
    amarillas.add("Reverse Amarillo");
    amarillas.add("Skip Amarillo");

    rojas.add("0 Rojo");
    rojas.add("1 Rojo");
    rojas.add("2 Rojo");
    rojas.add("3 Rojo");
    rojas.add("4 Rojo");
    rojas.add("5 Rojo");
    rojas.add("6 Rojo");
    rojas.add("7 Rojo");
    rojas.add("8 Rojo");
    rojas.add("9 Rojo");
    rojas.add("+2 Rojo");
    rojas.add("Reverse Rojo");
    rojas.add("Skip Rojo");

    verdes.add("0 Verde");
    verdes.add("1 Verde");
    verdes.add("2 Verde");
    verdes.add("3 Verde");
    verdes.add("4 Verde");
    verdes.add("5 Verde");
    verdes.add("6 Verde");
    verdes.add("7 Verde");
    verdes.add("8 Verde");
    verdes.add("9 Verde");
    verdes.add("+2 Verde");
    verdes.add("Reverse Verde");
    verdes.add("Skip Verde");

    wilds.add("Wild");
    wilds.add("Wild 4");

    cartasTotales.put("Azul", azules);
    cartasTotales.put("Amarillo", amarillas);
    cartasTotales.put("Verde", verdes);
    cartasTotales.put("Rojo", rojas);
    cartasTotales.put("Wilds", wilds);

    return cartasTotales;
  }

}
