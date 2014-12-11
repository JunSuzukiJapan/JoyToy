package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;

public class JTDotCall extends JTCode {

  private JTCode code;
  private JTSymbol symbol;
  private ArrayList<JTCode> list;

  public JTDotCall(JTCode c, JTSymbol sym, ArrayList<JTCode> l) {
    code = c;
    symbol = sym;
    list = l;
  }

  public JTCode run() throws Exception {
    JTCode c;
    c = code.run();

    // 新たなローカル変数を作成する前に引数を評価しておかなければいけない。
    if(list != null){
      ArrayList<JTCode> list2 = new ArrayList<JTCode>();
      for(int i=0; i < list.size(); i++){
        JTCode c2 = list.get(i).run();
        list2.add(c2);
      }
      list = list2;
    }

    c = c.message(symbol, list);  // (B)
    return c;
  }
}