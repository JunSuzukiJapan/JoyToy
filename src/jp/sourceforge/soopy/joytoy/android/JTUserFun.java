package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;
import java.util.Hashtable;

public class JTUserFun extends JTFun {

  JTSymbol symbol = null;
  private ArrayList<JTCode> params;
  private JTCode body;

  public JTUserFun(JTSymbol sym, ArrayList<JTCode> l, JTCode code) {
    symbol = sym;
    params = l;
    body = code;
    if(params != null){
      arg_count = params.size();
    }
  }

  public JTSymbol getName(){
    return symbol;
  }
  
  public String toString() {
    return "<fun>";
  }

  public JTCode run() throws Exception {
    if(symbol != null){
      JoyToy.set(symbol, this);
    }
    return this;
  }

  public JTCode exec(ArrayList<JTCode> params) throws Exception {
    JTCode c = null;
    setArgs(params);     // 引数と変数領域の確保や、引数に値をセットする
    if(body != null){
      c = body.run();
    }
    removeArgs();      // 引数領域の解放
    return c;
  }

  private void setArgs(ArrayList<JTCode> args) throws Exception {
    // 引数が０個の場合でも、ローカル変数用にフレームは確保しておく。
    if((params == null) && (args == null)){
      JoyToy.pushLocals(new Hashtable<JTSymbol, JTCode>());
      return;
    }
    
    Hashtable<JTSymbol, JTCode> table = new Hashtable<JTSymbol, JTCode>();  // フレームの実体(Hashtable<JTSymbol, JTCode>)
    JoyToy.pushLocals(table);           // フレームを登録

    // 評価した引数をフレームに登録
    for(int i=0; i < args.size(); i++){
      JTSymbol sym = (JTSymbol)params.get(i);
      JTCode c = (JTCode)args.get(i);
      table.put(sym, c);
    }
  }

  private void removeArgs() {
    JoyToy.popLocals();    // フレームを削除
  }

}