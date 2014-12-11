package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;

class primAbs extends JTPrim {

  public primAbs(){
    super(1);  // 引数は１個（メソッドを呼び出したオブジェクトのみ）
  }
  
  public JTCode exec(ArrayList<JTCode> params) throws Exception {
    JTInt i1 = (JTInt)params.get(0);
    return new JTInt(Math.abs(i1.getValue()));
  }
}

public class JTInt extends JTHandler {  // 親クラスをJTHandlerに変更
  private int value;
  
  static {
    table.put(JTSymbol.intern("abs"), new primAbs());  // メソッドabsの登録
  }
  
  public JTInt(Integer integer){
    value = integer.intValue();
  }

  public JTInt(int i){
    value = i;
  }

  public int getValue(){  // 記憶している数値を返す。
    return value;
  }

  public String toString(){  // 数値を文字列に変換したものを返す。
    return Integer.toString(value);
  }

  /** 足し算 */
  public JTCode add(JTCode code) throws Exception {
    JTCode result = null;
    if(code.getClass() != JTInt.class){
      throw new Exception("数値以外のものを足そうとしました。");
    }
    JTInt i = (JTInt)code;
    result = new JTInt(value + i.getValue());
    return result;
  }

  /** 引き算 */
  public JTCode sub(JTCode code) throws Exception {
    JTCode result = null;
    if(code.getClass() != JTInt.class){
      throw new Exception("数値以外のものを引こうとしました。");
    }
    JTInt i = (JTInt)code;
    result = new JTInt(value - i.getValue());
    return result;
  }

  /** かけ算 */
  public JTCode multiply(JTCode code) throws Exception {
    JTCode result = null;
    if(code.getClass() != JTInt.class){
      throw new Exception("数値以外のものを掛けようとしました。");
    }
    JTInt i = (JTInt)code;
    result = new JTInt(value * i.getValue());
    return result;
  }

  /** 割り算 */
  public JTCode devide(JTCode code) throws Exception {
    JTCode result = null;
    if(code.getClass() != JTInt.class){
      throw new Exception("数値以外のものを割ろうとしました。");
    }
    JTInt i = (JTInt)code;
    result = new JTInt(value / i.getValue());
    return result;
  }

  public JTCode less(JTCode code) throws Exception {
    JTCode result = null;
    if(code.getClass() != JTInt.class){
      throw new Exception("数値以外のものを比較しようとしました。");
    }
    JTInt i = (JTInt)code;
    if(value < i.getValue()){
      result = JTBool.True;
    }else{
      result = JTBool.False;
    }
    return result;
  }

  public JTCode le(JTCode code) throws Exception {
    JTCode result = null;
    if(code.getClass() != JTInt.class){
      throw new Exception("数値以外のものを比較しようとしました。");
    }
    JTInt i = (JTInt)code;
    if(value <= i.getValue()){
      result = JTBool.True;
    }else{
      result = JTBool.False;
    }
    return result;
  }
  
  public JTCode greater(JTCode code) throws Exception {
    JTCode result = null;
    if(code.getClass() != JTInt.class){
      throw new Exception("数値以外のものを比較しようとしました。");
    }
    JTInt i = (JTInt)code;
    if(value > i.getValue()){
      result = JTBool.True;
    }else{
      result = JTBool.False;
    }
    return result;
  }

  public JTCode ge(JTCode code) throws Exception {
    JTCode result = null;
    if(code.getClass() != JTInt.class){
      throw new Exception("数値以外のものを比較しようとしました。");
    }
    JTInt i = (JTInt)code;
    if(value >= i.getValue()){
      result = JTBool.True;
    }else{
      result = JTBool.False;
    }
    return result;
  }

  public boolean equals(Object code) {
    if(code.getClass() != JTInt.class){
      return false;
    }
    JTInt i = (JTInt)code;
    return value == i.getValue();
  }

}