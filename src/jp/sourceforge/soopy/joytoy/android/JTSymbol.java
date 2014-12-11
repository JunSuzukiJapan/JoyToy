﻿package jp.sourceforge.soopy.joytoy.android;
import java.util.Hashtable;

public class JTSymbol extends JTCode {
  private static Hashtable<String, JTSymbol> table = new Hashtable<String, JTSymbol>();  // すでに作成済みのシンボルを記録しておく。
  private String name;  // シンボルの名前
  
  private JTSymbol(String s) {
    name = s;
    table.put(s, this);  // 作成したシンボルは記録しておく。
  }

  public static JTSymbol intern(String s) {
    if(table.containsKey(s)){          // すでに同じ名前のシンボルが存在していれば
      return (JTSymbol) table.get(s);  // 　　それを返す。
    }else{                             // 存在していなければ
      return  new JTSymbol(s);         // 　　新しく作成して返す
    }
  }

  public JTCode run() throws Exception {   // シンボルが指す変数の値を返す。
    JTCode c = JoyToy.getSymbolValue(this);    // (A)
    if(c == null){
      throw new Exception("シンボル" + name + "は定義されていません。");
    }
    return c;
  }

  public String toString() {  // シンボルの名前を返す。
    return name;
  }

}