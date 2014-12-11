package jp.sourceforge.soopy.joytoy.android;
public abstract class JTPrim extends JTFun {

  public JTPrim(int n){
    arg_count = n;
  }
  
  public String toString() {
    return "<prim>";
  }
}