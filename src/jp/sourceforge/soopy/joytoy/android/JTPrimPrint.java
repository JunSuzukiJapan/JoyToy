package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;

public class JTPrimPrint extends JTPrim {
  
  public JTPrimPrint(){
    super(1);  // 引数の個数は１個
  }

  public JTCode exec(ArrayList<JTCode> params) throws Exception {
    JTCode code = (JTCode)params.get(0);
    JoyToy.out.println(code.run().toString());
    return null;
  }
}