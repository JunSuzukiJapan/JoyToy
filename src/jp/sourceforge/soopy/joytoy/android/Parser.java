package jp.sourceforge.soopy.joytoy.android;
import java.util.ArrayList;
import java.util.Hashtable;

class Parser {
  private Lexer lex;
  private int token;         // 先読みしたトークン

  private void getToken(){   // トークンを先読みする
    if(lex.advance()){
      token = lex.token();
    }else{
      token = TokenType.EOS;           // 次のトークンが存在しないときにはEOSを設定しておく。
    }
  }

  public JTCode parse(Lexer lexer){
    JTCode code = null;
    lex = lexer;
    getToken();              // あらかじめトークンを先読みしておく。
    try {
      code = program();
    } catch (Exception e){
      e.printStackTrace();
    }
    return code;
  }

  private JTCode program() throws Exception {
    JTCode code = stmt();
    if (code != null) {
      switch(token){
      case ';':
        break;
      default:
        throw new Exception("文法エラーです。");
      }
    }
    return code;
  }

  private JTCode stmt() throws Exception {
    JTCode code = null;
    switch(token){
    case TokenType.IF:
      code = if_stmt();
      break;
    case TokenType.WHILE:    // 'while'
      code = while_stmt();
      break;
    case '{':          // ブロック文の始まり
      code = block();
      break;
    case TokenType.FUN:    // 関数定義
      code = fun();
      break;
    case TokenType.DEF:    // 変数宣言
      code = def();
      break;
    default:
      code = expr();
    }
    return code;
  }
  
  private JTCode expr() throws Exception {
    JTCode code = simpleExpr();
    switch(token){
    case '<':
    case '>':
    case TokenType.EQ:    // '=='
    case TokenType.NE:    // '!='
    case TokenType.LE:    // '<='
    case TokenType.GE:    // '>='
      code = expr2(code);
      break;
    }
    return code;
  }
  
  private JTBinExpr expr2(JTCode code) throws Exception {
    JTBinExpr result = null;
    while((token == '<') ||
          (token == '>') ||
          (token == TokenType.EQ) ||    // '=='
          (token == TokenType.NE) ||    // '!='
          (token == TokenType.LE) ||    // '<='
          (token == TokenType.GE))      // '>='
    {
      int op = token;
      getToken();
      JTCode code2 = simpleExpr();
      if(result == null){
        result = new JTBinExpr(op, code, code2); 
      }else{
        result = new JTBinExpr(op, result, code2); 
      }
    }
    return result;
  }

  private JTCode simpleExpr() throws Exception {
    JTCode code = term();
    switch(token){
    case '+':
    case '-':
    case TokenType.OR:          // '||'
      code = simpleExpr2(code);
      break;
    }
    return code;
  }
  
  private JTBinExpr simpleExpr2(JTCode code) throws Exception {
    JTBinExpr result = null;
    while((token == '+') || (token == '-') || (token == TokenType.OR)){ // +,-,||
      int op = token;
      getToken();
      JTCode code2 = term();
      if(result == null){
        result = new JTBinExpr(op, code, code2); 
      }else{
        result = new JTBinExpr(op, result, code2); 
      }
    }
    return result;
  }
  
  private JTCode term() throws Exception {
    JTCode code = factor();
    switch(token){
    case '*':
    case '/':
    case TokenType.AND:    // '&&'
      code = term2(code);
      break;
    }
    return code;
  }
  
  private JTCode term2(JTCode code) throws Exception {
    JTBinExpr result = null;
    while((token == '*') || (token == '/') || (token == TokenType.AND)){  // *,/,&&
      int op = token;
      getToken();
      JTCode code2 = term();
      if(result == null){
        result = new JTBinExpr(op, code, code2); 
      }else{
        result = new JTBinExpr(op, result, code2); 
      }
    }
    return result;
  }

  private JTCode factor() throws Exception {
    JTCode code = null;
    switch(token){
    case TokenType.STRING:
      code = new JTString((String) lex.value());
      getToken();
      break;
    case TokenType.TRUE:
      code = JTBool.True;
      getToken();
      break;
    case TokenType.FALSE:
      code = JTBool.False;
      getToken();
      break;
    case '!':
      getToken();
      code = new JTNot(factor());
      break;
    case TokenType.OBJECT:    // 「オブジェクト」の解析へ
      code = object();
      break;
    case TokenType.NEW:    // 「クローン作成」
      code = newExpr();
      break;
    default:
      code = first();
    }
    // メッセージ式の処理
    while(token == '.'){
      getToken();  // skip '.'
      if(token != TokenType.SYMBOL){
        throw new Exception("文法エラーです。");
      }
      JTSymbol sym = (JTSymbol)lex.value();
      getToken();  // skip SYMBOL
      if(token == '('){
        getToken(); // skip '('
        ArrayList<JTCode> list = args();
        if(token != ')'){
          throw new Exception("文法エラーです。");
        }
        getToken(); // skip ')'
        code = new JTDotCall(code, sym, list);
      }else if(token == '='){                      // 代入の処理
        getToken();  // skip '='
        JTCode c = expr();
        code = new JTDotAssign(code, sym, c);
      }else{
        code = new JTDotExpr(code, sym);
      }
    }
    return code;
  }

  private JTCode first() throws Exception {
    JTCode code = null;
    switch(token){
    case TokenType.EOS:    // 空のプログラム
      break;
    case TokenType.INT:
      code = new JTInt((Integer)lex.value());
      getToken();
      break;
    case '-':
      getToken();
      code = new JTMinus(first());
      break;
    case '(':
      getToken();
      code = expr();
      if(token != ')'){
        throw new Exception("文法エラー：対応する括弧が有りません。");
      }
      getToken();
      break;
    case TokenType.SYMBOL:
      JTSymbol sym = (JTSymbol)lex.value();
      getToken();
      if(token == '='){
        getToken(); // skip '='
        code = new JTAssign(sym, expr());
      }else if(token == '('){
        code = methodCall(sym);
      }else{
        code = sym;
      }
      break;
    default:
      throw new Exception("文法エラーです。");
    }
    return code;
  }
  
  private JTCode if_stmt() throws Exception {
    getToken();  // skip 'if'
    if(token != '('){
      throw new Exception("文法エラーです。");
    }
    getToken(); // skip '('
    JTCode cond = expr();
    if(token != ')'){
      throw new Exception("文法エラーです。");
    }
    getToken(); // skip ')'
    JTCode st1 = stmt();
    JTCode st2 = null;
    if(token == TokenType.ELSE){
      getToken();  // skip 'else'
      st2 = stmt();
    }
    return new JTIf(cond, st1, st2);
  }

  private JTCode while_stmt() throws Exception {
    getToken();  // skip 'while'
    if(token != '('){
      throw new Exception("文法エラーです。");
    }
    getToken(); // skip '('
    JTCode cond = expr();
    if(token != ')'){
      throw new Exception("文法エラーです。");
    }
    getToken(); // skip ')'
    JTCode st = stmt();
    return new JTWhile(cond, st);
  }

  private JTCode block() throws Exception {
    ArrayList<JTCode> list = null;
    getToken();  // skip '{'
    while(token != '}'){
      JTCode c = stmt();
      if(token != ';'){
        throw new Exception("文法エラーです。");
      }
      getToken();  // skip ';'
      if(list == null){
        list = new ArrayList<JTCode>();
      }
      list.add(c);
    }
    getToken();  // skip '}'
    return new JTBlock(list);
  }

  private JTCode methodCall(JTSymbol sym) throws Exception {
    getToken(); // skip '('
    ArrayList<JTCode> list = args();
    if(token != ')'){
      throw new Exception("文法エラーです。");
    }
    getToken(); // skip ')'
    return new JTFuncall(sym, list);
  }

  private ArrayList<JTCode> args() throws Exception {
    ArrayList<JTCode> list = null;
    if(token != ')'){
      list = new ArrayList<JTCode>();
      list.add(expr());
      while(token != ')'){
        if(token != ','){
          throw new Exception("文法エラーです。");
        }
        getToken();  // skip ','
        list.add(expr());
      }
    }
    return list;
  }

  private JTCode fun() throws Exception {
    getToken();  // skip 'fun'
    if(token != TokenType.SYMBOL){
      throw new Exception("文法エラーです。");
    }
    JTSymbol sym = (JTSymbol)lex.value();
    getToken();  // skip Symbol
    if(token != '('){
      throw new Exception("文法エラーです。");
    }
    getToken();  // skip '('
    ArrayList<JTCode> list = symbols();
    if(token != ')'){
      throw new Exception("文法エラーです。");
    }
    getToken();  // skip ')'
    JTBlock blk = (JTBlock) block();
    return new JTUserFun(sym, list, blk);
  }

  private ArrayList<JTCode> symbols() throws Exception {
    ArrayList<JTCode> list = null;
    if(token != ')'){
      list = new ArrayList<JTCode>();
      list.add(expr());
      while(token != ')'){
        if(token != ','){
          throw new Exception("文法エラーです。");
        }
        getToken();  // skip ','
        if(token != TokenType.SYMBOL){
          throw new Exception("文法エラーです。");
        }
        list.add((JTCode) lex.value());
        getToken();
      }
    }
    return list;
  }

  private JTCode def() throws Exception {
    getToken();  // skip 'def'
    if(token != TokenType.SYMBOL){
      throw new Exception("文法エラーです。");
    }
    JTSymbol sym = (JTSymbol)lex.value();
    getToken();
    JTCode code = null;
    if(token == '='){
      getToken();  // skip '='
      code = expr();
    }
    return new JTDefVar(sym, code);
  }

  private JTCode object() throws Exception {
    JTSymbol sym;
    getToken();  // skip 'object'
    if(token != '{'){
      throw new Exception("文法エラーです。");
    }
    getToken();  // skip '{'
    Hashtable<JTSymbol, JTCode> table = new Hashtable<JTSymbol, JTCode>();
    while(token != '}'){
      switch(token){
      case TokenType.FUN:
        JTUserFun fun = (JTUserFun)fun();
        sym = fun.getName();
        if(table.containsKey(sym)){
          throw new Exception("変数" + sym.toString() + "は定義済みです。");
        }
        table.put(sym, fun);
        if(token != ';'){
          throw new Exception("文法エラーです。");
        }
        getToken();  // skip ';'
        break;
      case TokenType.DEF:
        JTDefVar def = (JTDefVar)def();
        sym = def.getName();
        if(table.containsKey(sym)){
          throw new Exception("変数" + sym.toString() + "は定義済みです。");
        }
        table.put(sym, def);
        if(token != ';'){
          throw new Exception("文法エラーです。");
        }
        getToken();  // skip ';'
        break;
      default:
        throw new Exception("文法エラーです。");
      }
    }
    if(token != '}'){
      throw new Exception("文法エラーです。");
    }
    getToken();  // skip '}'
    return new JTObject(table);
  }

  private JTCode newExpr() throws Exception {
    getToken();  // skip 'new'
    if(token != TokenType.SYMBOL){
      throw new Exception("文法エラーです。");
    }
    JTSymbol sym = (JTSymbol)lex.value();
    getToken();  // skip SYMBOL
    JTBlock block = null;
    if(token == '{'){
      block = (JTBlock) block();
    }
    return new JTNew(sym, block);
  }

}