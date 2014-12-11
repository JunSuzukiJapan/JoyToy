package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;

class primSubstring extends JTPrim {

  public primSubstring(){
    super(3);   // 引数はthis（メソッドを呼び出したオブジェクト）を含めて３個
  }
  
  public JTCode exec(ArrayList<JTCode> params) throws Exception {
    JTString s = (JTString)params.get(0);
    JTCode c1 = (JTCode)params.get(1);
    JTCode c2 = (JTCode)params.get(2);
    if( ! (c1 instanceof JTInt)){
      throw new Exception("substringの引数は数値でなければいけません。");
    }
    if( ! (c2 instanceof JTInt)){
      throw new Exception("substringの引数は数値でなければいけません。");
    }
    JTInt i1 = (JTInt)c1;
    JTInt i2 = (JTInt)c2;
    int start = i1.getValue();
    int end = i2.getValue();
    return new JTString(s.getStr().substring(start, end));
  }
}

public class JTString extends JTHandler {  // 親クラスをJTHandlerに変更
  private String str;
  
  static {
    // メソッドsubstringを登録
    table.put(JTSymbol.intern("substring"), new primSubstring());
  }
  
  public JTString(String string) {
    str = string;
  }

  public String getStr() {
    return str;
  }

  public String toString() {
    return '"' + str + '"';
  }

  public JTCode add(JTCode c2) throws Exception {
    if(c2.getClass() != JTString.class){
      throw new Exception("not string");
    }
    JTString s2 = (JTString)c2;
    return new JTString(str + s2.str);
  }
}