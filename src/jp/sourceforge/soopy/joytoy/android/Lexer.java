package jp.sourceforge.soopy.joytoy.android;
import java.io.Reader;
import java.util.Hashtable;

public class Lexer {
  private int tok;
  private Object val;
  private LexerReader reader;

  private static Hashtable<String, Integer> reserved = new Hashtable<String, Integer>();  // 予約語を保持する

  static {  // 予約語を登録
    reserved.put("true", new Integer(TokenType.TRUE));
    reserved.put("false", new Integer(TokenType.FALSE));
    reserved.put("if", new Integer(TokenType.IF));
    reserved.put("else", new Integer(TokenType.ELSE));
    reserved.put("while", new Integer(TokenType.WHILE));  // while
    reserved.put("fun", new Integer(TokenType.FUN));
    reserved.put("def", new Integer(TokenType.DEF));
    reserved.put("object", new Integer(TokenType.OBJECT));  // トークン'object'
    reserved.put("new", new Integer(TokenType.NEW));  // 予約語に'new'を追加
  }

  public Lexer(Reader r){
    reader = new LexerReader(r);  // readerにはLexerReaderをセットする！
  }
  
  public int token(){
    return tok;
  }

  public Object value(){
    return val;
  }

  public boolean advance(){
    try {
      skipWhiteSpace();
      int c = reader.read();
      if(c < 0){
        return false;
      }
      switch(c){
      case ';':
      case '+':
      case '-':
      case '*':
      case '(':
      case ')':
      case '{':    // '{'
      case '}':    // '}'
      case ',':    // ','
      case '.':    // '.'
        tok = c;
        break;
      case '/':              // '/'は特別に扱う。
        c = reader.read();   // 次の文字が
        if(c == '/'){        // '/'だったら
          skipLineComment(); //   １行コメントとして読み飛ばし
          return advance();  //   次のトークンを読みにいく。
        }else if(c == '*'){  // '*'だったら
          skipComment();     //   複数行コメントとして読み飛ばし
          return advance();  //   次のトークンを読みにいく。
        }else{               // それ以外なら
          reader.unread(c);  // １文字戻しておいて
          tok = '/';         // 普通に演算子'/'として処理する。
        }
        break;
      case '"':        // (1)
        lexString();
        tok = TokenType.STRING;
        break;
      case '=':
        c = reader.read();
        if(c == '='){
          tok = TokenType.EQ;    // '=='
        }else{
          reader.unread(c);
          tok = '=';             // '='
        }
        break;
      case '!':
        c = reader.read();
        if(c == '='){
          tok = TokenType.NE;    // '!='
        }else{
          reader.unread(c);
          tok = '!';             // '!'
        }
        break;
      case '<':
        c = reader.read();
        if(c == '='){
          tok = TokenType.LE;    // '<='
        }else{
          reader.unread(c);
          tok = '<';             // '<'
        }
        break;
      case '>':
        c = reader.read();
        if(c == '='){
          tok = TokenType.GE;    // '>='
        }else{
          reader.unread(c);
          tok = '>';             // '>'
        }
        break;
      case '&':
        c = reader.read();
        if(c == '&'){
          tok = TokenType.AND;   // '&&'
        }else{
          throw new Exception("演算子'&'は使えません。");
        }
        break;
      case '|':
        c = reader.read();
        if(c == '|'){
          tok = TokenType.OR;    // '||'
        }else{
          throw new Exception("演算子'|'は使えません。");
        }
        break;
      default:
        if(Character.isDigit((char)c)){
          reader.unread(c);
          lexDigit();
          tok = TokenType.INT;
        }else if(Character.isJavaIdentifierStart((char) c)){  // (B)
          reader.unread(c);  // 読み込んだ文字を１度戻しておく。
          lexSymbol();       // シンボルを解析。
        }else{
          throw new Exception("数字じゃないです。");
        }
        break;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private void lexDigit() throws Exception {
    int num = 0;
    while(true){
      int c = reader.read();
      if(c < 0){
        break;
      }
      if(!Character.isDigit((char)c)){
        reader.unread(c);         // returnする前にunread()する！
        break;
      }
      num = (num * 10) + (c - '0');
    }
    val = new Integer(num);
  }

  private void lexSymbol() throws Exception {
    tok = TokenType.SYMBOL;
    StringBuffer buf = new StringBuffer();
    while(true){
      int c = reader.read();
      if(c < 0){
        throw new Exception("ファイルの終わりに到達しました。");
      }
      if( ! Character.isJavaIdentifierPart((char) c)){
        reader.unread(c);
        break;
      }
      buf.append((char)c);
    }
    String s = buf.toString(); 
    val = JTSymbol.intern(s);

    if(reserved.containsKey(s)){          // (A)
      tok = ((Integer)reserved.get(s)).intValue();
    }
  }
 
  private void lexString() throws Exception {
    StringBuffer buf = new StringBuffer();
    while(true){
      int c = reader.read();
      if(c < 0){
        throw new Exception("文字列中でファイルの終わりに到達しました。");
      }
      if(c == '"'){
        break;
      }else if(c == '\\'){    // (A)
        c = reader.read();
        if(c < 0){
          throw new Exception("文字列中でファイルの終わりに到達しました。");
        }
      }
      buf.append((char)c);
    }
    val = buf.toString();
  }
  
  private void skipWhiteSpace() throws Exception {
    int c = reader.read();
    while((c != -1) && Character.isWhitespace((char)c)){
      c = reader.read();
    }
    reader.unread(c);
  }

  private void skipLineComment() throws Exception {
    int c;
    while((c = reader.read()) != '\n'){
      if(c < 0){
        throw new Exception("コメント中にファイルの終端に到達しました。");
      }
    }
    reader.unread(c);
  }

  private void skipComment() throws Exception {
    int c = '\0';
    while(true){
      c = reader.read();
      if(c < 0){
        throw new Exception("コメント中にファイルの終端に到達しました。");
      }
      if(c == '*'){
        c = reader.read();
        if(c == '/'){
          break;
        }
      }
    }
  }
}