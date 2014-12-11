package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;

public class JTPrimMax extends JTPrim {
  
  public JTPrimMax(){
    super(2);  // 引数の個数は２個
  }

  public JTCode exec(ArrayList<JTCode> params) throws Exception {
    JTCode code1 = (JTCode)params.get(0);  // １番目の引数を取り出す。
    JTCode code2 = (JTCode)params.get(1);  // ２番目の引数を取り出す。
    if(code1.getClass() != JTInt.class){
      throw new Exception("maxの引数は数値でなければいけません。");
    }    
    if(code2.getClass() != JTInt.class){
      throw new Exception("maxの引数は数値でなければいけません。");
    }
    JTInt i1 = (JTInt)code1;
    JTInt i2 = (JTInt)code2;
    return new JTInt(Math.max(i1.getValue(), i2.getValue()));
  }
}