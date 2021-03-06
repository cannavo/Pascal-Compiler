import java.io.*;
import java.util.*;
public class Scan {
 public int[][] fsm; 
 public Hashtable<String,String> reserved;
 public String filename;
 public String fileAsString = "";
 public SymbolTable st;
 public StringTable strTable;
 public PrintWriter pw;
 public int line = 1;
 public int state = 0;
 public char ch;
 public int charIndex = 0; 
 public Scan(String filename) throws IOException {
  this.filename=filename;
  st = new SymbolTable();
  strTable = new StringTable();  
  start();
 }
 public Scan(String filename, SymbolTable syt, StringTable stt) throws IOException {
  this.filename=filename;
  st = syt;
  strTable = stt;  
  start();
 }
 public void start() throws IOException {
   pw = new PrintWriter("output.txt");
   Scanner pass = new Scanner(new File(filename));
   while (pass.hasNext()) {
     pw.write(line + " " + pass.nextLine() + "\n");
     line++;
   }
   pass.close();
   line = 1; 
   removeSpacesAndComments(); 
   fsm = new int[15][11];
   Scanner input = new Scanner(new File("FSM.txt"));
   for (int r = 0; r < 15; r++)
     for (int c = 0; c < 11; c++)
       fsm[r][c]= input.nextInt();
   input.close(); 
   reserved=new Hashtable<String,String>();
   Scanner input2 = new Scanner(new File("reserved.txt"));
   for(int i=0;i<37;i++)
   {
     String[] temp = input2.nextLine().split(" ");
     reserved.put(temp[0],temp[1]);
   }
   input2.close();
   ch = fileAsString.charAt(charIndex);
 }
 public Token nextToken() throws IOException {
  int state = 0;
  String buf = "";
  while (ch == ' ' || ch == '\n') {
   if (ch == '\n')
    line++;
   charIndex++;
   ch = fileAsString.charAt(charIndex);
  }
  int charClass = getCharacterClass(ch);
  while (fsm[state][charClass]>0) {
   buf = buf + ch;
   state = fsm[state][charClass];
   charIndex++;
   
   if (charIndex == fileAsString.length())
    break;
    
   ch = fileAsString.charAt(charIndex);
   charClass = getCharacterClass(ch);
  }
  if (state == 13)
    error("Illegal charcter");
  if (state== 14)
    error("String not terminated");
  return finalState(buf, state);
 } 
 public Token finalState(String buf, int state) {
  String temp=reserved.get(buf.toUpperCase());
  //System.out.printf("Buf %s state %d \n", buf, state);
  int type=-1;
  int value = -1;
  if (temp == null) {
   if (state == 4) {
    type = T.STRING;
    value = strTable.insert(buf);
   }
   else if (state == 1) {
    type = T.IDENTIFIER;
    value = st.insert(buf, 0);
   }
   else if (state == 2) {
    type = T.NUMBER;
    value = Integer.parseInt(buf);
   }
  }
  else 
   type=Integer.parseInt(temp);
  Token token = new Token(type, value);
  return token;
 }
 public void error(String s) throws IOException {
   System.out.println(ch);
   System.out.println(s+" at line " + line + ".");
   pw.write(s+" at line " + line + ".");
   pw.close();
   System.exit(0);
 }
 public void removeSpacesAndComments() throws FileNotFoundException {
  Scanner one = new Scanner((new File(filename)));
  while(one.hasNext()) {
   String line = one.nextLine().replaceAll("[^\\S ]","").replaceAll("???","'").replaceAll("???","'");
   String[] split=line.split("'");
   for(int i=0;i<split.length;i++) {
    if((i%2)==0) {
     if(split[i].contains("!")) {
      fileAsString+=split[i].split("!")[0];
      break;
     }
     fileAsString+=split[i];
    }
    else if(i!=(split.length-1))
     fileAsString+="'"+split[i]+"'";
    else
     fileAsString+="'"+split[i];
   }
   fileAsString+="\n";
  }
  one.close();
 }
 public int getCharacterClass(char ch) {
  if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
   return 0; //letter
  }
  if (ch >= '0' && ch <= '9') {
   return 1; // num
  }
  if (ch == '\'') {
   return 2;
  }
  if (ch == ':') {
   return 3;
  }
  if (ch == '\n') {
   return 4;
  }
  if (ch == '>') {
   return 5;
  }
  if (ch == '<') {
   return 6;
  }
  if (ch == '=') {
   return 7;
  }
  if (ch == '(' || ch == ')' || ch == '+' || ch == '*' || ch == '/' || ch == '-' || ch == '%'
    || ch == '.' || ch == ',' || ch == ';') {
   return 8;
  }
  if (ch == ' ') {
   return 9;
  }
  else {
   return 10;
  }
 }
 public static void main(String args[]) throws IOException {
  Scanner input= new Scanner(System.in);
  System.out.print("Enter File name: ");
  String filename = input.nextLine();
  input.close();
  Scan s = new Scan(filename);
  Token t = new Token();
  while(t.tokenType != T.PERIOD) {
   t = s.nextToken();
   System.out.println(t);
  }
  s.pw.close();
  System.out.println("Symbol Table:");
  s.st.printTable();
  System.out.println("String Table:");
  s.strTable.printTable();
 }
}