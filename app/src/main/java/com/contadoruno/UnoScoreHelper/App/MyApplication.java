package com.contadoruno.UnoScoreHelper.App;

import android.app.Application;

import com.contadoruno.UnoScoreHelper.Logica.Classes.Jugador;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Partida;
import com.contadoruno.UnoScoreHelper.Logica.Classes.Regla;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application {

  public static AtomicInteger jugadorId = new AtomicInteger();
  public static AtomicInteger partidaId = new AtomicInteger();
  public static AtomicInteger reglaId = new AtomicInteger();

  @Override
  public void onCreate() {
    super.onCreate();
    setUpRealmConfig();
    Realm realm = Realm.getDefaultInstance();
    jugadorId = getIdByTable(realm, Jugador.class);
    partidaId = getIdByTable(realm, Partida.class);
    reglaId = getIdByTable(realm, Regla.class);
    realm.close();
  }

  private void setUpRealmConfig() {
    Realm.init(getApplicationContext());
    RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
    builder.allowWritesOnUiThread(true);
    RealmConfiguration config = builder.deleteRealmIfMigrationNeeded().build();
    Realm.setDefaultConfiguration(config);
  }

  private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> tipo){
    RealmResults<T> results = realm.where(tipo).findAll();
    if (results.size() > 0){
      return new AtomicInteger(results.max("id").intValue());
    }
    return new AtomicInteger();
  }
}
