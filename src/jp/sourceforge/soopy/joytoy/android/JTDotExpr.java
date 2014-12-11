package jp.sourceforge.soopy.joytoy.android;
public class JTDotExpr extends JTCode {

  private JTCode code;
  private JTSymbol symbol;
  public JTDotExpr(JTCode c, JTSymbol sym) {
    code = c;
    symbol = sym;
  }

  public JTCode run() throws Exception {
    JTCode c;
    c = code.run();
    c = c.message(symbol);    // (A)
    return c;
  }

}