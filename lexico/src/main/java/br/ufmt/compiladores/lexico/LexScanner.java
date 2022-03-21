package br.ufmt.compiladores.lexico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LexScanner {

  private char[] conteudo;
  private int estado;
  private int pos;
  
  //Lista de palavras reservadas
  private String palavras[] = {"program", "begin", "end", "real", "integer", "read",
  "write", "if", "then", "else", "while", "do", "procedure"};

  public LexScanner(String arq) {
    try {
      byte[] bytes = Files.readAllBytes(Paths.get(arq));
      conteudo = (new String(bytes)).toCharArray();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
  

  public Token nextToken() {
    if (isEOF()) {
      return null;
    }
    estado = 0;
    char c;
    Token token = null;
    String termo = "";
    while (true) {
      if(isEOF()){
          pos = conteudo.length + 1;
      }
      c = nextChar();
      switch (estado) {
        //Estados do automato
        case 0:
          if (isLetra(c)) {
            termo += c;
            estado = 6;
          } else if (isDigito(c)) {
            termo += c;
            estado = 1;
          } else if (isEspaco(c)) {
            estado = 0;
          } else if (isSimboloexcetoBarra(c)){
            termo += c;
            estado = 8;
          }else if(isPonto(c)){
            termo += c;
            estado = 10;
          }else if(isCaracMaior(c)){
            termo += c;
            estado = 12;
          }else if(isCaracMenor(c)){
            termo += c;
            estado = 16;
          }else if(isCaracdoisPontos(c)){
            termo += c;
            estado = 20;
          }else if(isAbrechave(c)){
             estado = 24; 
          }else if(isBarra(c)){
             termo += c;
             estado = 25;
          }else if(c == 0){
              return null;
          }else {
            throw new RuntimeException("Token não reconhecido!");
          }
          break;
        case 1:
          if (isDigito(c)) {
            estado = 1;
            termo += c;
          } else if (isEspaco(c) || isSimbolosexcetoPonto(c)) {
            estado = 5;
          }else if (isPonto(c)){
            estado = 2;
            termo += c;
          } else{
            throw new RuntimeException("Número não reconhecido!");
          }
          break;
        case 2:
          if(isDigito(c)){
            estado = 3;
            termo += c;
          }else{
            throw new RuntimeException("Número não reconhecido!");  
          }
          break;
        case 3:
          if (isDigito(c)) {
            estado = 3;
            termo += c;
          } else if (isEspaco(c) || isSimbolosexcetoPonto(c)) {
            estado = 4;
          } else {
            throw new RuntimeException("Número não reconhecido!");
          }
          break;
        case 4:
          token = new Token();
          token.setTipo(Token.NUMERO_REAL);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 5:
          token = new Token();
          token.setTipo(Token.NUMERO_INTEIRO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 6:
            if (isDigito(c) || isLetra(c)){
                estado = 6;
                termo += c;
            }else if (isEspaco(c) || isTodososSimbolos(c)){
             estado = 7;   
            }else{
               throw new RuntimeException("Identificador não reconhecido!"); 
            }
            break;
        case 7:
          int v = 0;
          for(int i=0; i<palavras.length;i++){
              if(termo.equals(palavras[i]))
                  v = 1; 
          }
          token = new Token();
          
          if(v == 0)
            token.setTipo(Token.IDENT);
          if(v == 1)
            token.setTipo(Token.PALAVRA_RESERVADA);
          
          token.setTermo(termo);
          back();
          back();
          return token;
        case 8:
          if( isEspaco(c) || isLetra(c) || isDigito(c) || isTodososSimbolos(c)){
              estado = 9;
          }else{
              throw new RuntimeException("Simbolo não reconhecido!");
          }
          break;
        case 9:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 10:
          if(isLetra(c) || isEspaco(c) || isDigito(c) || isSimbolosexcetoPonto(c)){
            estado = 11;
          }
          else if(c == 0){
            estado = 11;  
          }else{
            throw new RuntimeException("Simbolo não reconhecido!");
          }
          break;
        case 11:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          return token;
        case 12:
          if(isCaracIgual(c)){
              termo += c;
              estado = 13;
          }else if(isEspaco(c) || isDigito(c) || isLetra(c)){
              estado = 15;
          }else{
              throw new RuntimeException("Simbolo não reconhecido!"); 
          }
          break;
        case 13:
          if(isEspaco(c) || isDigito(c) || isLetra(c)){
              estado = 14;
          }else{
              throw new RuntimeException("Simbolo não reconhecido!"); 
          }
          break;
        case 14:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 15:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 16:
          if(isCaracMaior(c) || isCaracIgual(c)){
              termo += c;
              estado = 17;
          }else if(isEspaco(c) || isDigito(c) || isLetra(c)){
              estado = 19;
          }else{
              throw new RuntimeException("Simbolo não reconhecido!");
          }
          break;
        case 17:
          if(isEspaco(c) || isDigito(c) || isLetra(c)){
             estado = 18;
          }else{
             throw new RuntimeException("Simbolo não reconhecido!"); 
          }
          break;
        case 18:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 19:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 20:
          if(isCaracIgual(c)){
              termo += c;
              estado = 21;
          }else if(isEspaco(c) || isDigito(c) || isLetra(c)){
              estado = 23;
          }else{
              throw new RuntimeException("Simbolo não reconhecido!");
          }
          break;
        case 21:
          if(isEspaco(c) || isDigito(c) || isLetra(c)){
              estado = 22;
          }else{
              throw new RuntimeException("Simbolo não reconhecido!");
          }
          break;
        case 22:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 23:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 24:
          if(isFechachave(c)){
              estado = 0;
          }else if(isLetra(c) || isEspaco(c) || isDigito(c) || isTodososSimbolos(c) ){
              estado = 24;
          }else{
              throw new RuntimeException("Caractere nao valido");
          }
          break;
        case 25:
          if(isEspaco(c) || isDigito(c) || isLetra(c)){
              estado = 26;
          }else if (isAsterisco(c)){
              estado = 27;
          }else{
             throw new RuntimeException("Simbolo não reconhecido!");
          }
          break;
        case 26:
          token = new Token();
          token.setTipo(Token.SIMBOLO);
          token.setTermo(termo);
          back();
          back();
          return token;
        case 27:
          if(isLetra(c) || isEspaco(c) || isDigito(c) || isTodosSimbolosExcetoAsterisco(c)){
              estado = 27;
          }else if(isAsterisco(c)){
              estado = 28;
          }else{
              throw new RuntimeException("Caractere nao valido");
          }
          break;
        case 28:
          if(isAsterisco(c)){
              estado = 28;
          }else if(isBarra(c)){
              termo = "";
              estado = 0;
          }else{
            estado = 27;
          }
          break; 
    }
      
    }
  }
  //Funcoes uteis para a implementacao do automato
  private boolean isLetra(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  private boolean isDigito(char c) {
    return c >= '0' && c <= '9';
  }
  

  private boolean isEspaco(char c) {
    return c == ' ' || c == '\n' || c == '\t';
  }
  
  private boolean isPonto(char c){
      return c == '.';
  }
  
  private boolean isCaracMaior(char c){
      return c == '>';
  }
  
  private boolean isCaracMenor(char c){
      return c == '<';
  }
  
  private boolean isCaracdoisPontos(char c){
      return c == ':';
  }
  
  private boolean isCaracIgual(char c){
      return c == '=';
  }
  
  private boolean isSimbolo(char c){
      return c == ';' || c == '$' || c == ',' || c == '(' || c == ')'|| c == '=' || c == '-' || c == '+' || c == '*' || c == '/';
  }
  
  private boolean isSimboloexcetoBarra(char c){
      return c == ';' || c == '$' || c == ',' || c == '(' || c == ')'|| c == '=' || c == '-' || c == '+' || c == '*';
  }
  
  private boolean isTodososSimbolos(char c){
     return c == ';' || c == '$' || c == ',' || c == '(' || c == ')' || c == ':'
              || c == '=' || c == '-' || c == '+' || c == '*' || c == '/' || c == '<'
              || c == '>' || c == '.'; 
  }
  
  private boolean isTodosSimbolosExcetoAsterisco(char c){
      return c == ';' || c == '$' || c == ',' || c == '(' || c == ')' || c == ':'
              || c == '=' || c == '-' || c == '+' || c == '/' || c == '<'
              || c == '>' || c == '.';
  }
  
  
  private boolean isSimbolosexcetoPonto(char c){
     return c == ';' || c == '$' || c == ',' || c == '(' || c == ')' || c == ':'
              || c == '=' || c == '-' || c == '+' || c == '*' || c == '/' || c == '<'
              || c == '>'; 
  }
  
  private boolean isAbrechave(char c){
      return c == '{';
  }
  
  private boolean isFechachave(char c){
      return c == '}';
  }
  
  private boolean isBarra(char c){
      return c == '/';
  }
  
  private boolean isAsterisco(char c){
      return c == '*';
  }

  private boolean isEOF() {
    return pos >= conteudo.length;
  }

  private char nextChar() {
    if(isEOF()){
      return 0;
    }
    return conteudo[pos++];
  }

  private void back() {
        pos--;
  }
}
