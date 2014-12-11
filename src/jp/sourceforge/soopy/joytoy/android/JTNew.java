package jp.sourceforge.soopy.joytoy.android;
public class JTNew extends JTCode {

  private JTSymbol symbol;
  private JTBlock block;

  public JTNew(JTSymbol sym, JTBlock blk) {
    symbol = sym;
    block  = blk;
  }

  public JTCode run() throws Exception {
    JTCode c = JoyToy.getSymbolValue(symbol);
    if(c == null){
      throw new Exception("オブジェクト" + symbol.toString() + "が存在しません。");
    }
    if( ! (c instanceof JTObject)){
      throw new Exception("変数" + symbol.toString() + "はオブジェクトではありません。");
    }
    // オブジェクトのクローンを作成
    JTObject obj = new JTObject((JTObject)c);
    // ブロック文があれば実行
    if(block != null){
      JoyToy.pushObject(obj);
      block.run();
      JoyToy.popObject();
    }
    return obj;
  }

}