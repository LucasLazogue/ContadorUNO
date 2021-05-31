package com.contadoruno.UnoScoreHelper.Logica.Enums;

import com.contadoruno.UnoScoreHelper.R;

public enum Classic {

  WILD (R.drawable.wild),
  WILD4 (R.drawable.wild4),

  CEROR (R.drawable.redzero),
  UNOR (R.drawable.blueone),
  DOSR (R.drawable.yellowtwo),
  TRESR (R.drawable.greenthree),
  CUATROR (R.drawable.redfour),
  CINCOR (R.drawable.bluefive),
  SEISR (R.drawable.yellowsix),
  SIETER (R.drawable.greenseven),
  OCHOR (R.drawable.redeight),
  NUEVER (R.drawable.bluenine),
  REVERSER (R.drawable.yellowreverse),
  SKIPR (R.drawable.redskip),
  DRAW2R (R.drawable.greendraw2),


  DRAW1FL (R.drawable.draw1flip),
  DRAW5FD(R.drawable.darw5flip),
  FLIPD(R.drawable.flipdark),
  FLIPL(R.drawable.fliplight),
  REVERSED(R.drawable.reverseflip),
  SKIPD(R.drawable.skipdark),
  WILDDRAW2(R.drawable.wilddraw2),
  WILDF(R.drawable.wildflip),
  WILDDRAWC(R.drawable.wilddrawflip);

  private final int imagen;

  Classic(int imagen) {
    this.imagen = imagen;
  }

  public int getImagen() {
    return this.imagen;
  }

}


