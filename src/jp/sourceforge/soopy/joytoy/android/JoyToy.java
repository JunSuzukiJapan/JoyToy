package jp.sourceforge.soopy.joytoy.android;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Stack;

import jp.sourceforge.soopy.joytoy.android.Frame;
import jp.sourceforge.soopy.joytoy.android.JTCode;
import jp.sourceforge.soopy.joytoy.android.JTObject;
import jp.sourceforge.soopy.joytoy.android.JTPrimMax;
import jp.sourceforge.soopy.joytoy.android.JTPrimPrint;
import jp.sourceforge.soopy.joytoy.android.JTSymbol;
import jp.sourceforge.soopy.joytoy.android.Lexer;
import jp.sourceforge.soopy.joytoy.android.Parser;
import android.app.Activity;



public class JoyToy extends Activity {
    public static Hashtable<JTSymbol, JTCode> Globals = new Hashtable<JTSymbol, JTCode>();
    public static Stack<Frame> Frames = new Stack<Frame>();    // FrameのスタックFrames
    public static PrintStream out;
    
    static {
      Frames.push(new Frame());                  // スタックFramesの初期化
      Globals.put(JTSymbol.intern("max"), new JTPrimMax());
      Globals.put(JTSymbol.intern("print"), new JTPrimPrint());
      
      JTPrimAndroid android = new JTPrimAndroid();
      Globals.put(JTSymbol.intern("pwd"), android.new JTPrimPWD());
      Globals.put(JTSymbol.intern("ls"), android.new JTPrimLS());
    }

    // クラスFrameのインスタンスを新たに作成してスタックに追加
    public static void pushObject(JTObject obj){
      Frame frame = new Frame(obj);
      Frames.push(frame);
    }
    
    // クラスFrameのインスタンスをスタックから削除
    public static void popObject(){
      Frames.pop();
    }

    public static void pushLocals(Hashtable<JTSymbol, JTCode> table){
      Frame frame = (Frame)Frames.peek();
      frame.pushLocals(table);
    }
    
    public static void popLocals(){
      Frame frame = (Frame)Frames.peek();
      frame.popLocals();
    }

    public static boolean hasSymbol(JTSymbol sym){
      Frame frame = (Frame)Frames.peek();
      if(frame.hasSymbol(sym)){
        return true;
      }
      return Globals.contains(sym);
    }

    public static JTCode getSymbolValue(JTSymbol sym){
      Frame frame = (Frame)Frames.peek();
      if(frame.hasSymbol(sym)){
        return frame.getSymbolValue(sym);
      }
      return (JTCode) Globals.get(sym);
    }
    
    public static void set(JTSymbol sym, JTCode code) {
      Frame frame = (Frame)Frames.peek();
      if(frame.hasSymbol(sym)){
        frame.set(sym, code);
        return;
      }
      Globals.put(sym, code);
    }  
    
    public static void def(JTSymbol sym, JTCode code) throws Exception {
      Frame frame = (Frame)Frames.peek();
      if(frame.size() != 0){
        frame.def(sym, code);
        return;
      }
      if(Globals.containsKey(sym)){
        throw new Exception("変数" + sym.toString() + "は定義済みです。");
      }
      Globals.put(sym, code);
    }

    /*
    static void usage(){
      System.out.println("usage: java JoyToy [source_filename]");
    }
    */
    
    public JoyToy(OutputStream writer){
//    	if(JoyToy.out == null){
    		JoyToy.out = new PrintStream(writer);
//    	}
    }
    
    /*
    private static JoyToy joytoy = null;
    public static JoyToy getInstance(OutputStream writer){
      if(joytoy == null){
        joytoy = new JoyToy(writer);
      }
      return joytoy;
    }
    */
    
    public void run(Reader reader){
        try {
            BufferedReader in;

            in = new BufferedReader(reader);

            Lexer lex = new Lexer(in);                  // スキャナを作成。
            Parser parser = new Parser();               // パーサーを作成。
            while(true){
              JTCode code = (JTCode) parser.parse(lex);  // 実際に構文解析する。
              if(code == null) break;                    // プログラムの終わり。
              try{
            	  code.run();    // (A)
              }catch(Exception e){
            	  JoyToy.out.println("Error: " + e.toString());
              }
            }
            in.close();                                  // 使い終わったストリームは閉じる。

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    public static void main(String[] args){
      boolean interactive = false;           // 標準入力から読み込んでいるときにはtrue

      if(args.length >= 2){
        // 引数の個数が変なときには使い方を表示して終了。
        usage();
        return;
      }

      try {
        BufferedReader in;

        if(args.length == 0){
          // 引数がないときには、標準入力から読み込む。
          in = new BufferedReader(new InputStreamReader(System.in));
          interactive = true;
        } else {
          // 引数で指定されたファイルから読み込む。
          in = new BufferedReader(new FileReader(args[0]));
        }

        Lexer lex = new Lexer(in);                  // スキャナを作成。
        Parser parser = new Parser();               // パーサーを作成。
        while(true){
          if(interactive){
            // 標準入力から読み込んでいるときにはプロンプトを表示。
            System.out.print("JoyToy: ");
          }
          JTCode code = (JTCode) parser.parse(lex);  // 実際に構文解析する。
          if(code == null) break;                    // プログラムの終わり。
          code.run();    // (A)
        }
        in.close();                                  // 使い終わったストリームは閉じる。

      } catch (FileNotFoundException e){             // ここから下はエラーのときの処理。
        if(args.length > 0){
          System.out.println("can't open file '" + args[0] + "'");
        } else {
          System.out.println("can't open file");
        }
      } catch (Exception e){
        e.printStackTrace();
      }
    }
    */
}