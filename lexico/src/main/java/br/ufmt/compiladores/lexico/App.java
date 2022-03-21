package br.ufmt.compiladores.lexico;


public class App {
  public static void main(String[] args) {
    
    //TESTE SINTATICO
    Sintatico sintatico = new Sintatico("input.txt");
    sintatico.analise();
    


    //TESTE LEXICO
    /*LexScanner scan = new LexScanner("input.txt");
    Token token = null;
    do {
      token = scan.nextToken();
      System.out.println(token);
    } while (token != null);
    */
    
  }
}
