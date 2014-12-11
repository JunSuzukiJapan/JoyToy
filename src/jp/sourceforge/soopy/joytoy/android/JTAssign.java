package jp.sourceforge.soopy.joytoy.android;
public class JTAssign extends JTBinExpr {

  public JTAssign(JTSymbol symbol, JTCode code) {
    super('=', symbol, code);
  }

  public JTCode run() throws Exception {
    JTSymbol sym = (JTSymbol)code1;
    JTCode c = code2.run();
    JoyToy.set(sym, c);      // (B)symで表す変数に値cをセット
    return c;
  }
}