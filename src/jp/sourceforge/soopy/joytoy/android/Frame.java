package jp.sourceforge.soopy.joytoy.android;
import java.util.Hashtable;
import java.util.Stack;

public class Frame {
  
  private Stack<Hashtable<JTSymbol, JTCode>> locals = new Stack<Hashtable<JTSymbol, JTCode>>();  // フレームのスタック
  private JTObject object;             // 現在のオブジェクト

  public Frame(){
    object = null;
  }
  
  public Frame(JTObject o){
    object = o;
  }

  // 記録している引数と変数の個数を得る
  public int size(){
    return locals.size();
  }

  // 新しいフレームの作成
  public void pushLocals(Hashtable<JTSymbol, JTCode> table){
    locals.push(table);
  }
  
  // フレームの破棄
  public void popLocals(){
    locals.pop();
  }
  
  // 指定された名前の引数やローカル変数やインスタンス変数が存在するかどうかの判定
  public boolean hasSymbol(JTSymbol sym){
    if(locals.size() != 0){
      Hashtable<JTSymbol, JTCode> table = locals.peek();
      if(table.containsKey(sym)){
        return true;
      }
    }
    if(object != null){
      return object.hasSymbol(sym);
    }
    return false;
  }

  // 指定された名前の引数やローカル変数やインスタンス変数の値を得る
  public JTCode getSymbolValue(JTSymbol sym){
    if(locals.size() != 0){
      Hashtable<JTSymbol, JTCode> table = locals.peek();
      if(table.containsKey(sym)){
        return table.get(sym);
      }
    }
    if(object != null){
      return object.getSymbolValue(sym);
    }
    return null;
  }

  // 指定された名前の引数やローカル変数やインスタンス変数に値をセットする
  public void set(JTSymbol sym, JTCode c){
    if(locals.size() != 0){
      Hashtable<JTSymbol, JTCode> table = locals.peek();
      if(table.containsKey(sym)){
        table.put(sym, c);
        return;
      }
    }
    if(object != null){
      try {
        object.set(sym, c);
      } catch (Exception e) {
        // 何もしない。
      }
    }
  }

  // ローカル変数を定義
  public void def(JTSymbol sym, JTCode c) throws Exception{
    Hashtable<JTSymbol, JTCode> table = locals.peek();
    if(table.containsKey(sym)){
      throw new Exception("変数" + sym.toString() + "は定義済みです。");
    }
    table.put(sym, c);
  }
}