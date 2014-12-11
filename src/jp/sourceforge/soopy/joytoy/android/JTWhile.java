package jp.sourceforge.soopy.joytoy.android;
public class JTWhile extends JTCode {

  private JTCode cond;
  private JTCode body;

  public JTWhile(JTCode code, JTCode code2) {
    cond = code;
    body = code2;
  }

  public JTCode run() throws Exception {
    JTCode c = null;
    while(cond.run() != JTBool.False){
      c = body.run();
    }
    return c;
  }
}