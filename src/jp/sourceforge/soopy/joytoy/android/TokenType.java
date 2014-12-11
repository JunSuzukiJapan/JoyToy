﻿package jp.sourceforge.soopy.joytoy.android;
class TokenType {
  public static final int EOS = -1;  // 文の終わりを表す。
  public static final int INT = 257;
  public static final int SYMBOL = 258;
  public static final int STRING = 259;  // トークンが文字列であることを表す定数
  public static final int TRUE = 260;    // 「真」を表す
  public static final int FALSE = 261;   // 「偽」を表す
  public static final int EQ = 262;    // '=='
  public static final int NE = 263;    // '!='
  public static final int LE = 264;    // '<='
  public static final int GE = 265;    // '>='
  public static final int AND = 266;   // '&&'
  public static final int OR  = 267;   // '||'
  public static final int IF  = 268;
  public static final int ELSE  = 269;
  public static final int WHILE  = 270;
  public static final int FUN  = 271;    // 'fun'
  public static final int DEF  = 272;    // 'def'
  public static final int OBJECT = 273;  // トークン'object'を表す定数
  public static final int NEW = 274;    // 'new'というトークンを表す定数
}
