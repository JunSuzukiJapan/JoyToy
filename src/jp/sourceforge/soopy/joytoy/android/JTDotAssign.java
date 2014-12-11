package jp.sourceforge.soopy.joytoy.android;
public class JTDotAssign extends JTCode {
  
  private JTCode code1;
  private JTSymbol symbol;
  private JTCode code2;

  public JTDotAssign(JTCode c, JTSymbol sym, JTCode c2) {
    code1 = c;
    symbol = sym;
    code2 = c2;
  }

  public JTCode run() throws Exception {
    JTCode c1 = code1.run();
    JTCode c2 = code2.run();
    c1.assign(symbol, c2);
    return c2;
  }
}