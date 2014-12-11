package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;
import java.util.Hashtable;

public class JTHandler extends JTCode {

  // シンボルと値のペアを保存するテーブル
  protected static Hashtable<JTSymbol, JTCode> table = new Hashtable<JTSymbol, JTCode>();

  // 「テーブルにシンボルが存在するかどうか」を判定するメソッド
  public boolean hasSymbol(JTSymbol sym) {
    if(table.containsKey(sym)){
      return true;
    }
    return false;
  }

  // テーブルから、シンボルに対応する値を取り出すメソッド
  public JTCode getSymbolValue(JTSymbol sym) {
    if(table.containsKey(sym)){
      return (JTCode)table.get(sym);
    }
    return null;
  }

  // メッセージ式（フィールド用）のメソッドmessage()
  public JTCode message(JTSymbol symbol) throws Exception {
    if( ! hasSymbol(symbol)){
      return super.message(symbol);
    }
    JTCode c = getSymbolValue(symbol);
    if(c instanceof JTFun){
      throw new Exception(symbol.toString() + "はメソッドです。フィールドではありません。");
    }
    return c;
  }

  // メッセージ式（メソッド用）のメソッドmessage()
  public JTCode message(JTSymbol symbol, ArrayList<JTCode> params) throws Exception {
    if(!hasSymbol(symbol)){
      return super.message(symbol, params);
    }
    JTCode c = getSymbolValue(symbol);
    if(!(c instanceof JTFun)){
      throw new Exception(symbol.toString() + "はメソッドではありません。");
    }
    JTFun f = (JTFun) c;
    if(params == null){
      params = new ArrayList<JTCode>();
    }
    params.add(0, this);
    return f.call(params);    // (A)
  }
}