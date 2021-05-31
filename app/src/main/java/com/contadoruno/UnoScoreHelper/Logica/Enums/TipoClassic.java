package com.contadoruno.UnoScoreHelper.Logica.Enums;

import io.realm.RealmObject;

public class TipoClassic extends RealmObject {
  private String enumDescription;

  public void saveEnum(Classic val) {
    this.enumDescription = val.toString();
  }

  public Classic getEnum() {
    return Classic.valueOf(enumDescription);
  }
}
