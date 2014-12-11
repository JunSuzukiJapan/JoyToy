package jp.sourceforge.soopy.joytoy.android;
public class JTBool extends JTCode {
  private boolean p;
  public static JTBool True;   // 真を表すオブジェクト
  public static JTBool False;  // 偽を表すオブジェクト
  
  static {
    True = new JTBool(true);
    False = new JTBool(false);
  }

  private JTBool(boolean b) {  // private属性であることに注意
    p = b;
  }

  public String toString() {
    return Boolean.toString(p);
  }

  public boolean isTrue() {
    return p;
  }

  public JTCode and(JTCode code2) throws Exception {
    if(p){
      JTCode c2 = code2.run();
      if(c2 == JTBool.True){
        return JTBool.True;
      }else{
        return JTBool.False;
      }
    }else{
      return JTBool.False;
    }
  }

  public JTCode or(JTCode code2) throws Exception {
    if(p){
      return JTBool.True;
    }else{
      JTCode c2 = code2.run();
      if(c2 == JTBool.True){
        return JTBool.True;
      }else{
        return JTBool.False;
      }
    }
  }
}