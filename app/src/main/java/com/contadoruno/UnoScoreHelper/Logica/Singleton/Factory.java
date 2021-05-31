package com.contadoruno.UnoScoreHelper.Logica.Singleton;

import com.contadoruno.UnoScoreHelper.Logica.Controllers.JugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Controllers.PartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Controllers.ReglaController;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IJugadorController;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IPartidaController;
import com.contadoruno.UnoScoreHelper.Logica.Interfaces.IReglaController;

public class Factory {

  private static Factory instance = null;


  public static Factory getInstance() {
    if (instance == null) {
      instance = new Factory();
    }
    return instance;
  }

  public IPartidaController getIPartidaController() {
    return new PartidaController();
  }

  public IReglaController getIReglaController() {
    return new ReglaController();
  }

  public IJugadorController getIJugadorController() {
    return new JugadorController();
  }
}
