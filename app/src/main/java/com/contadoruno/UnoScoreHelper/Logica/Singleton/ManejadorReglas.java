package com.contadoruno.UnoScoreHelper.Logica.Singleton;

import com.contadoruno.UnoScoreHelper.Logica.Classes.Regla;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class ManejadorReglas {
  private static ManejadorReglas instance = null;
  private Map<String, Regla> reglas;

  public ManejadorReglas(){
    this.reglas = new HashMap<String, Regla>();
  }

  public static ManejadorReglas getInstance() {
    if (instance == null)
      instance = new ManejadorReglas();
    return instance;
  }

  public Map<String, Regla> getReglas() {
    this.reglas.clear();
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Regla> lst = realm.where(Regla.class).findAll();
    for (Regla r : lst) {
      this.reglas.put(r.getNombre(), r);
    }
    return this.reglas;
  }

  public void addRegla(Regla regla){
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.copyToRealm(regla);
    realm.commitTransaction();
  }

  public void borrarRegla(Regla r) {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    r.deleteFromRealm();
    realm.commitTransaction();
  }
}
