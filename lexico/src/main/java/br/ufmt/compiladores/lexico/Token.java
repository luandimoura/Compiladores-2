package br.ufmt.compiladores.lexico;

public class Token {

  public static final int IDENT = 0;
  public static final int NUMERO_INTEIRO = 1;
  public static final int NUMERO_REAL = 2;
  public static final int SIMBOLO = 3;
  public static final int PALAVRA_RESERVADA = 4;
  public static final int ESPACO = 5;
  
  private int tipo;
  private String termo;

  public int getTipo() {
    return this.tipo;
  }

  public void setTipo(int tipo) {
    this.tipo = tipo;
  }

  public String getTermo() {
    return this.termo;
  }

  public void setTermo(String termo) {
    this.termo = termo;
  }

  @Override
  public String toString() {
    return "Token [" + tipo +", " + termo+"]";
  }
}
