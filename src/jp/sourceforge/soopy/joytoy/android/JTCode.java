package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;

public abstract class JTCode {
  public JTCode run() throws Exception{
    return this;
  }
 
  public JTCode add(JTCode code) throws Exception{
    throw new Exception("このオブジェクトに演算子'+'は使えません。");
  }
  
  public JTCode sub(JTCode code) throws Exception{
    throw new Exception("このオブジェクトに演算子'-'は使えません。");
  }

  public JTCode multiply(JTCode code) throws Exception{
    throw new Exception("このオブジェクトに演算子'*'は使えません。");
  }

  public JTCode devide(JTCode code) throws Exception{
    throw new Exception("このオブジェクトに演算子'/'は使えません。");
  }

  public JTCode less(JTCode c2) throws Exception {
    throw new Exception("このオブジェクトに演算子'<'は使えません。");
  }

  public JTCode greater(JTCode c2) throws Exception {
    throw new Exception("このオブジェクトに演算子'>'は使えません。");
  }

  public JTCode le(JTCode c2) throws Exception {
    throw new Exception("このオブジェクトに演算子'<='は使えません。");
  }

  public JTCode ge(JTCode c2) throws Exception {
    throw new Exception("このオブジェクトに演算子'>='は使えません。");
  }

  public JTCode and(JTCode c2) throws Exception {
    throw new Exception("このオブジェクトに演算子'&&'は使えません。");
  }

  public JTCode or(JTCode c2) throws Exception {
    throw new Exception("このオブジェクトに演算子'||'は使えません。");
  }

  public JTCode message(JTSymbol symbol) throws Exception {
    throw new Exception(symbol.toString() + "というメッセージはありません。");
  }

  public JTCode message(JTSymbol symbol, ArrayList<JTCode> list) throws Exception {
    throw new Exception(symbol.toString() + "というメソッドはありません。");
  }

  public void assign(JTSymbol symbol, JTCode c2) throws Exception {
    throw new Exception(symbol.toString() + "というフィールドはありません。");
  }

}
