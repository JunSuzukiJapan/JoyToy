package jp.sourceforge.soopy.joytoy.android;
public class JTDefVar extends JTCode {
  private JTSymbol symbol;
  private JTCode code;

  public JTDefVar(JTSymbol sym, JTCode c) {
    symbol = sym;
    code = c;
  }

  public JTSymbol getName(){
    return symbol;
  }
  
  public JTCode getCode() {
    return code;
  }
  
  public JTCode run() throws Exception {
    JTCode val = null;
    if(code != null){
      val = code.run();
    }
    JoyToy.def(symbol, val);
    return val;
  }
}