package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;

public abstract class JTFun extends JTCode {
  protected int arg_count = 0;  // 引数の個数

  public JTCode call(ArrayList<JTCode> params) throws Exception {
    if(params == null){
      if(0 != arg_count){
        throw new Exception("引数の個数が違います。");
      }
    }else{
      if(params.size() != arg_count){
        throw new Exception("引数の個数が違います。");
      }
    }
    return exec(params);
  }

  protected abstract JTCode exec(ArrayList<JTCode> params) throws Exception; 
}