package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class JTObject extends JTCode {
  private Hashtable<JTSymbol, JTCode> slots;
  private JTObject proto = null;      // プロトタイプを保持
  private JTObject proto2 = null;     // ２つめのプロトタイプ

  public JTObject(Hashtable<JTSymbol, JTCode> tbl){
    slots = tbl;
  }

  public JTObject(JTObject object) {  // プロトタイプを基にオブジェクトを作成する
    proto = object;
    slots = new Hashtable<JTSymbol, JTCode>();
  }

  // ２つのオブジェクトを基に新しいオブジェクトを作成する。
  public JTObject(JTObject obj1, JTObject obj2) {
    proto2 = obj1;
    proto  = obj2;
    slots  = new Hashtable<JTSymbol, JTCode>();
  }
  
  public String toString() {
    return "<object>";
  }

  public boolean hasSymbol(JTSymbol sym) {
    if(slots.containsKey(sym)){
      return true;
    }
    if((proto != null) && proto.hasSymbol(sym)){
      return true; 
    }
    if((proto2 != null) && proto2.hasSymbol(sym)){ 
      return true;
    }
    return false;
  }

  public JTCode getSymbolValue(JTSymbol sym) {
    JTCode code = null;
    if(slots.containsKey(sym)){
      code = (JTCode)slots.get(sym);
    }else if((proto != null) && proto.hasSymbol(sym)){
      code = proto.getSymbolValue(sym);
    }else if((proto2 != null) && proto2.hasSymbol(sym)){ 
      code = proto2.getSymbolValue(sym);
    }
    return code;
  }

  public void set(JTSymbol sym, JTCode code) throws Exception {
    if(slots.containsKey(sym)){
      slots.put(sym, code);
    }else if((proto != null) && proto.hasSymbol(sym)){
      slots.put(sym, code);
    }else if((proto2 != null) && proto2.hasSymbol(sym)){ 
      slots.put(sym, code);
    }else{
      throw new Exception("対応するフィールドがありません。");
    }
  }

  public JTCode run() throws Exception {
    Enumeration<JTSymbol> e = slots.keys();
    while(e.hasMoreElements()){  // slotsのキーが存在する限り繰り返す。
      JTSymbol sym = (JTSymbol)e.nextElement();
      JTCode c = (JTCode)slots.get(sym);
      if(c instanceof JTDefVar){
        // JTDefVarのインスタンであれば初期値を評価して
        // 評価後の値を、改めて登録する。
        JTDefVar def = (JTDefVar)c;
        c = def.getCode();
        if(c != null){
          c = c.run();
        }
        slots.put(sym, c);
      }
    }
    return this;
  }

  public JTCode message(JTSymbol symbol) throws Exception {
    if( ! hasSymbol(symbol)){
      throw new Exception(symbol.toString() + "という名前のフィールドはありません。");
    }
    JTCode c = getSymbolValue(symbol);
    if(c instanceof JTFun){
      throw new Exception(symbol.toString() + "はメソッドです。フィールドではありません。");
    }
    return c;
  }

  public JTCode message(JTSymbol symbol, ArrayList<JTCode> params) throws Exception {
    if(!hasSymbol(symbol)){
      throw new Exception(symbol.toString() + "という名前のメソッドはありません。");
    }
    JTCode c = getSymbolValue(symbol);
    if(!(c instanceof JTFun)){
      throw new Exception(symbol.toString() + "はメソッドではありません。");
    }
    JTFun f = (JTFun) c;
    JoyToy.pushObject(this);  // メッセージのレシーバになるオブジェクトを登録しておく。
    c = f.call(params);
    JoyToy.popObject();       // 登録したオブジェクトを削除。
    return c;
  }

  public void assign(JTSymbol symbol, JTCode c) throws Exception {
    if( ! hasSymbol(symbol)){
      throw new Exception(symbol.toString() + "という名前のフィールドはありません。");
    }
    JTCode c2 = getSymbolValue(symbol);
    if(c2 instanceof JTFun){
      throw new Exception(symbol.toString() + "はメソッドです。フィールドではありません。");
    }
    set(symbol, c);
  }

  public JTCode add(JTCode c2) throws Exception {
    if( ! (c2 instanceof JTObject)){
      throw new Exception("演算子'+'が定義されていない組み合わせです。");
    }
    JTObject obj = new JTObject(this, (JTObject)c2);
    return obj;
  }

}