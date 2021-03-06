/* Generated By:JavaCC: Do not edit this line. CatBotConstants.java */
package Rwb.Parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface CatBotConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int T_SEMI = 5;
  /** RegularExpression Id. */
  int T_COMMA = 6;
  /** RegularExpression Id. */
  int T_LCURL = 7;
  /** RegularExpression Id. */
  int T_RCURL = 8;
  /** RegularExpression Id. */
  int T_LPAREN = 9;
  /** RegularExpression Id. */
  int T_RPAREN = 10;
  /** RegularExpression Id. */
  int T_PLUS = 11;
  /** RegularExpression Id. */
  int T_MINUS = 12;
  /** RegularExpression Id. */
  int T_ORGANIZE = 13;
  /** RegularExpression Id. */
  int T_RPIPE = 14;
  /** RegularExpression Id. */
  int T_LPIPE = 15;
  /** RegularExpression Id. */
  int T_LETTER = 16;
  /** RegularExpression Id. */
  int T_INT = 17;
  /** RegularExpression Id. */
  int T_PUNCT = 18;
  /** RegularExpression Id. */
  int T_DIGIT = 19;
  /** RegularExpression Id. */
  int T_CMD = 20;
  /** RegularExpression Id. */
  int T_SQUOTE = 21;
  /** RegularExpression Id. */
  int T_SCQUOTE = 22;
  /** RegularExpression Id. */
  int T_EQUOTE = 23;
  /** RegularExpression Id. */
  int T_CHAR = 24;
  /** RegularExpression Id. */
  int T_ECQUOTE = 25;
  /** RegularExpression Id. */
  int T_CCHAR = 26;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int STRING_STATE = 1;
  /** Lexical state. */
  int CAT_STATE = 2;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\";\"",
    "\",\"",
    "\"{\"",
    "\"}\"",
    "\"(\"",
    "\")\"",
    "\"+\"",
    "\"-\"",
    "\"organize\"",
    "\"-->\"",
    "\"<--\"",
    "<T_LETTER>",
    "<T_INT>",
    "<T_PUNCT>",
    "<T_DIGIT>",
    "<T_CMD>",
    "\"\\\"\"",
    "\"[[\"",
    "\"\\\"\"",
    "<T_CHAR>",
    "\"]]\"",
    "<T_CCHAR>",
  };

}
