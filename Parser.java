import java.io.*;
import java.util.*;
public class Parser{
  public Scan scanner;
  public SymbolTable st;
  public StringTable stringTable;
  public Token token;
  public Parser(String filename) throws IOException{
    st=new SymbolTable();
    stringTable=new StringTable();
    scanner=new Scan(filename,st,stringTable);
    token=next();
  }
  public void parse() throws IOException {
    program();
  }
  public Token next() throws IOException {
    return scanner.nextToken();
  }
  public void error(String s) throws IOException{
    scanner.error("Syntax Error: "+s+" expected");
  }
  public boolean isRelop(int token){
    return (token==T.EQUAL || token==T.LT || token==T.GT || token==T.GE ||token==T.LE || token==T.NE);
  }
  public boolean isAddop(int token){
    return (token==T.PLUS || token==T.MINUS || token==T.OR);
  }
  public boolean isMulop(int token){
    return (token==T.DIV || token==T.TIMES || token==T.MOD || token==T.AND);
  }
  public void program() throws IOException{
    System.out.println("program");
    if(token.tokenType==T.PROGRAM)
      token=next();
    else
      error("Program");
    if(token.tokenType==T.IDENTIFIER)
      token=next();
    else
      error("Identifier");
    if(token.tokenType==T.SEMI)
      token=next();
    else
      error("Semi-Colon");
    variable_declarations();
    subprogram_declarations();
    compound_statement();
    if(token.tokenType!=T.PERIOD)
      error("Period");
  }
  public void variable_declarations() throws IOException {
    System.out.println("variable_declarations");
    if (token.tokenType==T.VAR){
      token=next();
      variable_declaration();
      if (token.tokenType==T.SEMI)
        token=next();
      else
        error("Semi-Colon");
      while(token.tokenType==T.IDENTIFIER){
        variable_declaration();
        if (token.tokenType==T.SEMI)
          token=next();
        else
          error("Semi-Colon");
      }
    }
  }
  public void variable_declaration() throws IOException {
    System.out.println("variable_delaration");
    identifier_list();
    if (token.tokenType==T.COLON)
          token=next();
    else
      error("Colon");
    type();
  }
  public void identifier_list() throws IOException {
    System.out.println("identifier_list");
    if(token.tokenType==T.IDENTIFIER)
      token=next();
    else
      error("Identifier");
    while(token.tokenType==T.COMMA){
      token=next();
      if(token.tokenType==T.IDENTIFIER)
        token=next();
      else
        error("Identifier");
    }
  }
  public void type() throws IOException {
    System.out.println("type");
    if(token.tokenType==T.INTEGER)
      token=next();
    else
      error("Integer");
  }
 public void subprogram_declarations()  throws IOException {
   System.out.println("subprogram_declarations");
   if(token.tokenType==T.PROCEDURE){
     subprogram_declaration();
     if (token.tokenType==T.SEMI)
       token=next();
     else
       error("Semi-Colon");
     subprogram_declarations();
   }
 }
 public void subprogram_declaration()  throws IOException {
   System.out.println("subprogram_declaration"); 
   subprogram_head();
   variable_declarations();
   compound_statement();
 }
 public void subprogram_head() throws IOException {
   System.out.println("subprogram_head"); 
   if (token.tokenType==T.PROCEDURE)
     token=next();
   else
     error("Procedure");
   if(token.tokenType==T.IDENTIFIER)
     token=next();
   else
     error("Identifier");
   arguments();
   if (token.tokenType==T.SEMI)
       token=next();
   else
     error("Semi-Colon");
 }
 public void arguments() throws IOException {
   System.out.println("arguments"); 
   if (token.tokenType==T.LPAREN)
       token=next();
   else
     error("Left Parenthesis");
   parameter_list();
   if (token.tokenType==T.RPAREN)
       token=next();
   else
     error("Right Parenthesis");
 }
 public void parameter_list() throws IOException {
   System.out.println("parameter_list");
   identifier_list();
   if (token.tokenType==T.COLON)
     token=next();
   else
     error("Colon");
   type();
   while(token.tokenType==T.SEMI) {
     token=next();
     identifier_list();
     if (token.tokenType==T.COLON)
       token=next();
     else
       error("Colon");
     type();
   }
 }
 public void compound_statement() throws IOException {
   System.out.println("compound_statement");
   if (token.tokenType==T.BEGIN)
     token=next();
   else
     error("Begin");
   statement_list();
   if (token.tokenType==T.END)
     token=next();
   else
     error("End");
 }
 public void statement_list() throws IOException {
   System.out.println("statement_list");
   statement();
   while(token.tokenType==T.SEMI){
     token=next();
     statement();
   }
 }
 public void statement() throws IOException {
   System.out.println("statement");
   if (token.tokenType==T.IDENTIFIER){
     assignment_statement();
   }
   if (token.tokenType==T.CALL){
     procedure_statement();
   }
   if (token.tokenType==T.BEGIN){
     compound_statement();
   }
   if (token.tokenType==T.IF){
     if_statement();
   }
   if (token.tokenType==T.WHILE){
     while_statement();
   }
   if (token.tokenType==T.READ) {
     read_statement();
   }
   if (token.tokenType==T.WRITE) {
     write_statement();
   }
   if (token.tokenType==T.WRITELN) {
     writeln_statement();
   }
 }
 public void assignment_statement() throws IOException {
   System.out.println("assignment_statement");
   if (token.tokenType==T.IDENTIFIER)
     token=next();
   else
     error("IDENTIFIER");
   if (token.tokenType==T.ASSIGN)
     token=next();
   else
     error("ASSIGN");
   expression();
 }
 public void if_statement() throws IOException {
   System.out.println("if_statement");
   if (token.tokenType==T.IF)
     token=next();
   else
     error("IF");
   expression();
   if (token.tokenType==T.THEN)
     token=next();
   else
     error("THEN");
   statement();
   if (token.tokenType==T.ELSE){
     token=next();
     statement();
   }
 }
 public void while_statement() throws IOException {
   System.out.println("while_statement");
   if (token.tokenType==T.WHILE)
     token=next();
   else
     error("While");
   expression();
   if (token.tokenType==T.DO)
     token=next();
   else
     error("Do");
   statement();
 }
 public void procedure_statement() throws IOException {
   System.out.println("procedure_statement");
   if (token.tokenType==T.CALL)
     token=next();
   else
     error("Call");
   if (token.tokenType==T.IDENTIFIER)
     token=next();
   else
     error("Identifier");
   if (token.tokenType==T.LPAREN)
     token=next();
   else
     error("Left Parenthesis");
   expression_list();
   if (token.tokenType==T.RPAREN)
     token=next();
   else
     error("Right Parenthesis");
 }
  public void expression_list() throws IOException {
   System.out.println("expression_list");
   expression();
   while(token.tokenType==T.COMMA){
     token=next();
     expression();
   }
  }
  public void expression() throws IOException {
   System.out.println("expression");
   simple_expression();
   if (isRelop(token.tokenType)){
     token=next();
     simple_expression();
   }
  }
  public void simple_expression() throws IOException {
   System.out.println("simple_expression");
   if(token.tokenType==T.MINUS){
     token=next();
   }
   term();
   while(isAddop(token.tokenType)){
     token=next();
     term();
   }
  }
  public void term() throws IOException {
   System.out.println("term");
   factor();
   while(isMulop(token.tokenType)){
     token=next();
     factor();
   }
  }
  public void factor() throws IOException {
   System.out.println("factor");
   if (token.tokenType==T.IDENTIFIER){
     token=next();
   }
   else if (token.tokenType==T.NUMBER){
     token=next();
   }
   else if (token.tokenType==T.TRUE){
     token=next();
   }
   else if (token.tokenType==T.FALSE){
     token=next();
   }
   else if (token.tokenType==T.LPAREN){
     token=next();
     expression();
     if (token.tokenType==T.RPAREN){
       token=next();
     }
     else
       error("Right Parenthesis");
   }
   else if (token.tokenType==T.NOT){
     token=next();
     factor();
   }
   else 
     error("Identifier,number,true,false,left parenthesis or not");
  }
  public void read_statement() throws IOException {
   System.out.println("read_statement");
   if (token.tokenType==T.READ)
     token=next();
   else
     error("Read");
   if (token.tokenType==T.LPAREN)
     token=next();
   else
     error("left Parenthesis");
   identifier_list();
   if (token.tokenType==T.RPAREN)
     token=next();
   else
     error("Right Parenthesis");
  }
  public void write_statement() throws IOException {
   System.out.println("write_statement");
   if (token.tokenType==T.WRITE)
     token=next();
   else
     error("Write");
   if (token.tokenType==T.LPAREN)
     token=next();
   else
     error("left Parenthesis");
   output_item();
   if (token.tokenType==T.RPAREN)
     token=next();
   else
     error("Right Parenthesis");
  }
  public void writeln_statement() throws IOException {
   System.out.println("writeln_statement");
   if (token.tokenType==T.WRITELN)
     token=next();
   else
     error("Write");
   if (token.tokenType==T.LPAREN)
     token=next();
   else
     error("left Parenthesis");
   output_item();
   if (token.tokenType==T.RPAREN)
     token=next();
   else
     error("Right Parenthesis");
  }
  public void output_item() throws IOException {
   System.out.println("output_item");
   if (token.tokenType==T.STRING)
     token=next();
   else
     expression();
  }  
  public static void main(String []args) throws IOException {
    Scanner input= new Scanner(System.in);
    System.out.print("Enter File name: ");
    String filename = input.nextLine();
    input.close();
    Parser p=new Parser(filename);
    p.parse();
    System.out.println("Success!");
  }
}
    