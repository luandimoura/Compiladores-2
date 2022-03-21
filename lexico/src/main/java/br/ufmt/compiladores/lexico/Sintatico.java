
package br.ufmt.compiladores.lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;


public class Sintatico {
    private LexScanner scan;
    private String simbolo;
    private int tipo;
    private Map<String, Simbolo> tabelaSimbolos = new HashMap<>(); //Tabela de simbolos principal
    private Map<String, Simbolo> tabelaSimbolosP = new HashMap<>(); //Tabela de simbolos do procedimento
    private int verificador = 0; //Verificar se as variáveis são do principal ou do procedimento
    
    private Stack<String> C = new Stack<String>(); //pilha que armazenara o conjunto de instrucoes
    private ArrayList<Float> D = new ArrayList<>(); //pilha que armazenara os valores numericos
    private int var = -1; //conta as variaveis
    private int i = -1; //conta as linhas
    private int qtd_p = 0; //Contador de parametros e variaveis dentro do procedimento
    private String aux_var, aux_op = "teste";  //auxiliar para nome de variavel e para operadores
    
    public Sintatico(String arq){
        scan = new LexScanner(arq);
        
    }
    
    public void analise(){
        obtemToken();
        programa();
        if(simbolo.equals("")){
            System.out.println("Conjunto de instrucoes executado com sucesso!");
            System.out.println("Tamanho da pilha: "+C.size());
            System.out.println("Quantidade de linhas: "+i);
            System.out.println("Pilha gerada:");
            System.out.println(C);
            interpretador();
            System.out.println("Interpretador Executado com sucesso!");
        }else{
            throw new RuntimeException("Erro sintatico: era esperado um fim de cadeia");
        }
    }
    
    public void interpretador(){
        int s = 0;
        for(int a = 0; a < C.size(); a++){
            String codigo = C.get(a);
            String codigo0 = codigo;
            
            float arg = 0;
            
            if(codigo.contains(" ")){
                int fim_codigo0 = codigo.indexOf(" ");
                int inicio_arg = codigo.indexOf(" ") + 1;
                codigo0 = codigo.substring(0, fim_codigo0);
                arg = Float.parseFloat(codigo.substring(inicio_arg));
            }
            //Percorre o conjunto de instrucoes
            switch(codigo0){
                case "CRCT":
                    s++;
                    D.add((float) arg);
                    break;
                case "CRVL":
                    s++;
                    D.add(D.get((int) arg));
                    break;
                case "SOMA":
                    D.set(s-1, D.get(s-1) + D.get(s));
                    D.remove(s);
                    s--;
                    break;
                case "SUBT":
                    D.set(s-1, D.get(s-1) - D.get(s));
                    D.remove(s);
                    s--;
                    break;
                case "MULT":
                    D.set(s-1, D.get(s-1) * D.get(s));
                    D.remove(s);
                    s--;
                    break;
                case "DIVI":
                    D.set(s-1, D.get(s-1) / D.get(s));
                    D.remove(s);
                    s--;
                    break;
                case "INVE":
                    D.set(s, - D.get(s));
                    break;
                case "CPME":
                    if(D.get(s-1) < D.get(s)){
                        D.set(s-1, (float) 1);
                    }else{
                        D.set(s-1, (float) 0);
                    }
                    D.remove(s);
                    s--;
                    break;
                case "CPMA":
                    if(D.get(s-1) > D.get(s)){
                        D.set(s-1, (float) 1);
                    }else{
                        D.set(s-1, (float) 0);
                    }
                    D.remove(s);
                    s--;
                    break;
                case "CPIG":
                    if(D.get(s-1).equals(D.get(s))){
                        D.set(s-1, (float) 1);
                    }else{
                        D.set(s-1, (float) 0);
                    }
                    D.remove(s);
                    s--;
                    break;
                    
                case "CDES":
                    if(!D.get(s-1).equals(D.get(s))){
                        D.set(s-1, (float) 1);
                    }else{
                        D.set(s-1, (float) 0);
                    }
                    D.remove(s);
                    s--;
                    break;
                case "CPMI":
                    if(D.get(s-1) <= D.get(s) ){
                        D.set(s-1, (float) 1);
                    }else{
                        D.set(s-1, (float) 0);
                    }
                    D.remove(s);
                    s--;
                    break;
                case "CMAI":
                    if(D.get(s-1) >= D.get(s) ){
                        D.set(s-1, (float) 1);
                    }else{
                        D.set(s-1, (float) 0);
                    }
                    D.remove(s);
                    s--;
                    break;
                case "ARMZ":
                    D.set((int) arg, D.get(s));
                    D.remove(s);
                    s--;
                    break;
                case "DSVI":
                    a = (int) arg - 1;
                    break;
                case "DSVF":
                    if(D.get(s) == 0){
                        a = (int) arg - 1;
                    }
                    
                    D.remove(s);
                    s--;
                    break;
                case "LEIT":
                    s++;
                    Scanner ler = new Scanner(System.in);
                    float numero = ler.nextFloat();
                    D.add(numero);
                    break;
                case "IMPR":
                    System.out.println(D.get(s));
                    D.remove(s);
                    s--;
                    break;
                case "ALME":
                    for(int j = 0; j < arg; j++){
                        D.add((float) 0);
                        
                    }
                    s += arg;
                    break;
                case "PARAM":
                    s++;
                    D.add(D.get((int) arg));
                    break;
                case "PUSHER":
                    s++;
                    D.add((float) arg);
                    break;
                case "CHPR":
                    a = (int) arg - 1;
                    break;
                case "DESM":
                    for(; arg > 0; arg--){
                        D.remove(s);
                        s--;
                    }
                    break;
                case "RTPR":
                    a = (int) (float) D.get(s) - 1;
                    
                    D.remove(s);
                    s--;
                    break;
                case "INPP":
                    s = -1;
                    break;
                case "PARA":
                    return;
                
                default: 
                    throw new RuntimeException("Simbolo nao encontrado: " +simbolo);
                            
            }
            
        }
        
    }
    
    private void obtemToken(){
        Token token = scan.nextToken();
        simbolo ="";
        if(token != null){
            simbolo = token.getTermo();
            System.out.println(simbolo);
            tipo = token.getTipo();
        }
        
    }
    
    private void programa(){
        if(simbolo.equals("program")){
            obtemToken();
            i += 1;
            C.push("INPP");
            
            if (tipo == Token.IDENT){
                obtemToken();
                corpo();
                if(simbolo.equals(".")){
                    obtemToken();
                }else{
                  throw new RuntimeException("Erro sintatico: era esperado um ponto (.)");  
                }
            }else{
                throw new RuntimeException("Erro sintatico: era esperado um identificador");
            }
            
        }else{
            throw new RuntimeException("Erro sintatico: era esperado 'program'");
        }
    }
    
    private void corpo(){
        dc();
        if(simbolo.equals("begin")){
            obtemToken();
            comandos();
            if(simbolo.equals("end")){
                i += 1;
                C.push("PARA");
                
                /*
                for(String key: tabelaSimbolosP.keySet()){
                    int valor1 = tabelaSimbolosP.get(key).getTipo();
                    String valor2 = tabelaSimbolosP.get(key).getNome();
                    int valor3 = tabelaSimbolosP.get(key).getEnd_rel();
                    int valor4 = tabelaSimbolosP.get(key).getPrim_instr();
                    System.out.println(key + " | Tipo: "+valor1+" Nome: "+valor2+" EndRel: "+valor3+
                            " PrimInstr: "+valor4);
                }
                */
     
                obtemToken();
            }else{
              throw new RuntimeException("Erro sintatico: era esperado 'end'");  
            }
        }else{
           throw new RuntimeException("Erro sintatico: era esperado 'begin'"); 
        }
    }
    
    private void dc(){
        if(simbolo.equals("real") || simbolo.equals("integer")){
            dc_v();
            mais_dc();
        }else if(simbolo.equals("procedure")){
            i += 1;
            C.push("DSVI linhaxprocedure"); //INSERIR O NUMERO DA LINHA QUE DEVERA APONTAR
            dc_p();
            
            reescrita("DSVI linhaxprocedure", "DSVI "+Integer.toString(i+1)); //Coloca a linha do começo do principal
            }
            
       }
    
    
    private void mais_dc(){
        if(simbolo.equals(";")){
            obtemToken();
            dc();
        }
    }
    
    private void dc_v(){ 
        tipo_var();
        if(simbolo.equals(":")){
            obtemToken();
            variaveis();
        }else{
            throw new RuntimeException("Erro sintatico: era esperado ':'");
        }
    }
    
    private void tipo_var(){
        if(simbolo.equals("real") || simbolo.equals("integer")){
            obtemToken();
        }else{
            throw new RuntimeException("Erro sintatico: era esperado 'real' ou 'integer'");
        }
    }
    
    private void variaveis(){
        if(tipo != Token.IDENT){
            throw new RuntimeException("Erro sintatico: era esperado um identificador");
        }
        if(verificador == 0){
            if(tabelaSimbolos.containsKey(simbolo)){
                throw new RuntimeException("Erro semantico: identificador ja encontrado "+simbolo);
            }else{
                var += 1;
                i += 1;
                C.push("ALME "+var);
                tabelaSimbolos.put(simbolo, new Simbolo(this.tipo, simbolo, var));
                
            }
        }else if(verificador == 1){
            if(tabelaSimbolosP.containsKey(simbolo)){
                throw new RuntimeException("Erro semantico: identificador ja encontrado "+simbolo);
            }else{
                qtd_p++;
                var += 1;
                i += 1;
                C.push("ALME "+var);
                tabelaSimbolosP.put(simbolo, new Simbolo(this.tipo, simbolo, var));
            }
        }
        
        obtemToken();
        mais_var();
    }
    
    private void mais_var(){ 
        if(simbolo.equals(",")){
           obtemToken();
           variaveis();
        }

    }
    
    private void dc_p(){
        if(simbolo.equals("procedure")){
            obtemToken();
            verificador = 1;
            if(tipo == Token.IDENT){
                tabelaSimbolosP.put(simbolo, new Simbolo(this.tipo, simbolo, -1, i+1));
                tabelaSimbolos.put(simbolo, new Simbolo(this.tipo, simbolo, -1, i+1));
                
                obtemToken();
                parametros();
                corpo_p();
                
                
            }else{
                throw new RuntimeException("Erro sintatico: era esperado um identificador");
            }
        }else{
            throw new RuntimeException("Erro sintatico: era esperado 'procedure'");
        }
    }
    
    private void parametros(){
        if(simbolo.equals("(")){
            obtemToken();
            lista_par();
            if(simbolo.equals(")")){
                obtemToken();
            }else{
              throw new RuntimeException("Erro sintatico: era esperado ')'");  
            }
        }
    }
    
    private void lista_par(){
        tipo_var();
        if(simbolo.equals(":")){
            obtemToken();
            variaveis();
            mais_par();
        }else{
            throw new RuntimeException("Erro sintatico: era esperado ':'");
        }
    }
    
    private void mais_par(){
        if(simbolo.equals(";")){
            obtemToken();
            lista_par();   
        }
    }
    
    private void corpo_p(){
        dc_loc();
        if(simbolo.equals("begin")){
            obtemToken();
            comandos();
            if(simbolo.equals("end")){
                i += 1;
                C.push("DESM "+Integer.toString(qtd_p));
                qtd_p = 0;
                i += 1;
                C.push("RTPR");
                obtemToken();
                verificador = 0;
            }else{
                throw new RuntimeException("Erro sintatico: era esperado 'end'");
            }
        }else{
           throw new RuntimeException("Erro sintatico: era esperado 'begin'"); 
        }
    }
    
    private void dc_loc(){
        if(simbolo.equals("real") || simbolo.equals("integer")){
            dc_v();
            mais_dcloc();
        }
    }
    
    private void mais_dcloc(){
        if(simbolo.equals(";")){
            obtemToken();
            dc_loc();
        }
    }
    
    private void lista_arg(){
        if(simbolo.equals("(")){
            
            obtemToken();
            argumentos();
            if(simbolo.equals(")")){
                obtemToken();
            }else{
                throw new RuntimeException("Erro sintatico: era esperado ')'");
            }
        }
    }
    
    private void argumentos(){
        if(tipo == Token.IDENT){
            if(verificador == 0){
                if(tabelaSimbolos.containsKey(simbolo)){
                    String guarda_nome = simbolo;
                    obtemToken();
                    i += 1;
                    C.push("PARAM "+tabelaSimbolos.get(guarda_nome).getEnd_rel());
                    mais_ident();
                }else{
                    throw new RuntimeException("Erro semantico: identificador nao declarado");
                }
            }
            else if (verificador == 1){
                if(tabelaSimbolos.containsKey(simbolo)){
                    String guarda_nome = simbolo;
                    obtemToken();
                    i += 1;
                    C.push("PARAM "+tabelaSimbolosP.get(guarda_nome).getEnd_rel());
                    mais_ident();
                }else{
                    throw new RuntimeException("Erro semantico: identificador nao declarado");
                }
            }
        }else{
           throw new RuntimeException("Erro sintatico: era esperado um identificador"); 
        }
    }
    
    private void mais_ident(){
        if(simbolo.equals(",")){
            obtemToken();
            argumentos();
        }
    }
    
    private void comandos(){
        comando();
        mais_comandos();
    }
    
    private void mais_comandos(){
        if(simbolo.equals(";")){
            obtemToken();
            comandos();
        }
        
    }
    
    private void comando(){
        if(simbolo.equals("read")){
            i += 1;
            C.push("LEIT");
            obtemToken();
            if(simbolo.equals("(")){
                obtemToken();
                if(tipo == Token.IDENT && verificador == 0){
                    if(tabelaSimbolos.containsKey(simbolo)){
                        i += 1;
                        C.push("ARMZ "+tabelaSimbolos.get(simbolo).getEnd_rel());
                        obtemToken();
                        if(simbolo.equals(")")){
                            obtemToken();
                        }else{
                            throw new RuntimeException("Erro sintatico: era esperado ')'");
                        }
                    }else{
                       throw new RuntimeException("Erro semantico: identificador nao declarado"); 
                    }
                }else if(tipo == Token.IDENT && verificador == 1){
                    if(tabelaSimbolosP.containsKey(simbolo)){
                        i += 1;
                        C.push("ARMZ "+tabelaSimbolosP.get(simbolo).getEnd_rel());
                        obtemToken();
                        if(simbolo.equals(")")){
                            obtemToken();
                        }else{
                            throw new RuntimeException("Erro sintatico: era esperado ')'");
                        }
                    }else{
                       throw new RuntimeException("Erro semantico: identificador nao declarado"); 
                    }
                }else{
                    throw new RuntimeException("Erro sintatico: era esperado um identificador");
                }

            }else{
                throw new RuntimeException("Erro sintatico: era esperado '('");
            }
        }else if(simbolo.equals("write")){
            obtemToken();
            if(simbolo.equals("(")){
                obtemToken();
                if(tipo == Token.IDENT && verificador == 0){
                    if(tabelaSimbolos.containsKey(simbolo)){
                        i += 1;
                        C.push("CRVL "+tabelaSimbolos.get(simbolo).getEnd_rel());
                        i += 1;
                        C.push("IMPR");
                        obtemToken();
                        if(simbolo.equals(")")){
                            obtemToken();
                        }else{
                            throw new RuntimeException("Erro sintatico: era esperado ')'");
                        }
                    }else{
                        throw new RuntimeException("Erro semantico: identificador nao declarado");
                    }
                }else if(tipo == Token.IDENT && verificador == 1){
                    if(tabelaSimbolosP.containsKey(simbolo)){
                        i += 1;
                        C.push("CRVL "+tabelaSimbolosP.get(simbolo).getEnd_rel());
                        i += 1;
                        C.push("IMPR");
                        
                        obtemToken();
                        if(simbolo.equals(")")){
                            obtemToken();
                        }else{
                            throw new RuntimeException("Erro sintatico: era esperado ')'");
                        }
                    }else{
                        throw new RuntimeException("Erro semantico: identificador nao declarado");
                    }  
                }else{
                    throw new RuntimeException("Erro sintatico: era esperado um identificador");
                }

            }else{
                throw new RuntimeException("Erro sintatico: era esperado '('");
            }
        }else if(tipo == Token.IDENT){
            if(verificador == 0){
                if(tabelaSimbolos.containsKey(simbolo)){
                    aux_var = simbolo;
                    obtemToken();
                    restoIdent();
                    
                    int num = tabelaSimbolos.get(aux_var).getEnd_rel();
                    if(num >= 0){
                        i += 1;
                        C.push("ARMZ "+tabelaSimbolos.get(aux_var).getEnd_rel());
                    }else{
                        i += 1;
                        C.push("CHPR "+tabelaSimbolos.get(aux_var).getPrim_instr());
                    }
                    
                    
                }else{
                    throw new RuntimeException("Erro semantico: identificador nao declarado");
                }
            }else if(verificador == 1){
                if(tabelaSimbolosP.containsKey(simbolo)){
                    aux_var = simbolo;
                    obtemToken();
                    restoIdent();
                    
                    int num = tabelaSimbolosP.get(aux_var).getEnd_rel();
                    if(num >= 0){
                        i += 1;
                        C.push("ARMZ "+tabelaSimbolosP.get(aux_var).getEnd_rel());
                    }else{
                        i += 1;
                        C.push("CHPR "+tabelaSimbolosP.get(aux_var).getPrim_instr());
                    }
                    
                }else{
                    throw new RuntimeException("Erro semantico: identificador nao declarado");
                }
            }
        }else if(simbolo.equals("if")){
            obtemToken();
            condicao();
            if(simbolo.equals("then")){
                i += 1;
                C.push("DSVF linhaxif");
                obtemToken();
                
                comandos();
                
                i += 1;
                C.push("DSVI linhaxif");
                int if_fim = i + 1;
                pfalsa();
                
                int else_fim = i + 1;
                
                reescrita("DSVF linhaxif", "DSVF "+Integer.toString(if_fim));
                reescrita("DSVI linhaxif", "DSVI "+Integer.toString(else_fim));
                
                if(simbolo.equals("$")){
                    obtemToken();
                }else{
                    throw new RuntimeException("Erro sintatico: era esperado '$'");
                }
                
            }else{
                throw new RuntimeException("Erro sintatico: era esperado 'then'");  
            }
        }else if(simbolo.equals("while")){
            obtemToken();
            int aux_desvio = i+1;
            condicao();
            
            if(simbolo.equals("do")){
                C.push("DSVF desvio");
                obtemToken();
                comandos();
                C.push("DSVI "+aux_desvio);
                reescrita("DSVF desvio", "DSVF "+Integer.toString(i+1));
               
                if(simbolo.equals("$")){
                    obtemToken();
                }else{
                  throw new RuntimeException("Erro sintatico: era esperado '$'");  
                }
            }else{
                throw new RuntimeException("Erro sintatico: era esperado 'do'");
            }
            
        }else{
            throw new RuntimeException("Erro sintatico: era esperado 'read' ou"
                    + "'write' ou 'if' ou 'while' ou um identificador valido");
        }
    
    }
    
    private void restoIdent(){
        if(simbolo.equals(":=")){
            obtemToken();
            expressao();
            
            if(aux_op.equals("-")){
                i += 1;
                C.push("SUBT");
            }else if(aux_op.equals("+")){
                i += 1;
                C.push("SOMA");
            }else if(aux_op.equals("*")){
                i += 1;
                C.push("MULT");
            }else if(aux_op.equals("/")){
                i += 1;
                C.push("DIVI");
            }else if(aux_op.equals("=")){
                i += 1;
                C.push("CPIG");
            }else if(aux_op.equals("<>")){
                i += 1;
                C.push("CDES");
            }else if(aux_op.equals(">=")){
                i += 1;
                C.push("CMAI");
            }else if(aux_op.equals("<=")){
                i += 1;
                C.push("CPMI");
            }else if(aux_op.equals(">")){
                i += 1;
                C.push("CPMA");
            }else if(aux_op.equals("<")){
                i += 1;
                C.push("CPME");
            }
            aux_op = ""; //Limpa o valor da variavel
            
            
        }else{
            i += 1;
            C.push("PUSHER linhaxpusher");
            
            lista_arg();
            
            reescrita("PUSHER linhaxpusher", "PUSHER "+Integer.toString(i+1));
            
        }
           
    }
    
    private void condicao(){
        expressao();
        relacao();
        expressao();
       
    }
    
    private void relacao(){
        switch(simbolo){
            case "=":
                aux_op = simbolo;
                obtemToken();
                break;
            case "<>":
                aux_op = simbolo;
                obtemToken();
                break;
            case ">=":
                aux_op = simbolo;
                obtemToken();
                break;
            case "<=":
                aux_op = simbolo;
                obtemToken();
                break;
            case ">":
                aux_op = simbolo;
                obtemToken();
                break;
            case "<":
                aux_op = simbolo;
                obtemToken();
                break;
            default:
                throw new RuntimeException("Erro sintatico: era esperado '=' ou '<>' ou '>="
                        + "ou '<=' ou '>' ou '<'");
        }
    }
    
    private void expressao(){
        termo();
        
        outros_termos();
    }
    
    private void termo(){
        op_un();
        fator();
        mais_fatores();
    }
    
    private void op_un(){
        if(simbolo.equals("-")){
            i += 1;
            C.push("INVE");
            obtemToken();
        }
        
    }
    
    private void fator(){
        if(tipo == Token.IDENT){
            if(verificador == 0){
                if(tabelaSimbolos.containsKey(simbolo)){
                    i += 1;
                    C.push("CRVL "+tabelaSimbolos.get(simbolo).getEnd_rel());
                    
                    obtemToken();
                }else{
                    throw new RuntimeException("Erro semantico: identificador nao declarado");
                } 
            }else if(verificador == 1){
               if(tabelaSimbolosP.containsKey(simbolo)){
                    i += 1;
                    C.push("CRVL "+tabelaSimbolosP.get(simbolo).getEnd_rel());
                    
                    obtemToken();
                }else{
                    throw new RuntimeException("Erro semantico: identificador nao declarado");
                } 
            }
            
        }else if(tipo == Token.NUMERO_INTEIRO){
            i += 1;
            C.push("CRCT "+simbolo);
            
            if(aux_op.equals("-")){
                i += 1;
                C.push("SUBT");
            }else if(aux_op.equals("+")){
                i += 1;
                C.push("SOMA");
            }else if(aux_op.equals("*")){
                i += 1;
                C.push("MULT");
            }else if(aux_op.equals("/")){
                i += 1;
                C.push("DIVI");
            }else if(aux_op.equals("=")){
                i += 1;
                C.push("CPIG");
            }else if(aux_op.equals("<>")){
                i += 1;
                C.push("CDES");
            }else if(aux_op.equals(">=")){
                i += 1;
                C.push("CMAI");
            }else if(aux_op.equals("<=")){
                i += 1;
                C.push("CPMI");
            }else if(aux_op.equals(">")){
                i += 1;
                C.push("CPMA");
            }else if(aux_op.equals("<")){
                i += 1;
                C.push("CPME");
            }
            aux_op = ""; //Limpa o valor da variavel
            
            obtemToken();
        }else if(tipo == Token.NUMERO_REAL){
            i += 1;
            C.push("CRCT "+simbolo);
            
            if(aux_op.equals("-")){
                i += 1;
                C.push("SUBT");
            }else if(aux_op.equals("+")){
                i += 1;
                C.push("SOMA");
            }else if(aux_op.equals("*")){
                i += 1;
                C.push("MULT");
            }else if(aux_op.equals("/")){
                i += 1;
                C.push("DIVI");
            }else if(aux_op.equals("=")){
                i += 1;
                C.push("CPIG");
            }else if(aux_op.equals("<>")){
                i += 1;
                C.push("CDES");
            }else if(aux_op.equals(">=")){
                i += 1;
                C.push("CMAI");
            }else if(aux_op.equals("<=")){
                i += 1;
                C.push("CPMI");
            }else if(aux_op.equals(">")){
                i += 1;
                C.push("CPMA");
            }else if(aux_op.equals("<")){
                i += 1;
                C.push("CPME");
            }
            aux_op = ""; //Limpa o valor da variavel
            
            obtemToken();
        }else if(simbolo.equals("(")){
            obtemToken();
            expressao();
            if(simbolo.equals(")")){
                obtemToken();
            }else{
                throw new RuntimeException("Erro sintatico: era esperado ')'");
            }
        }else{
            throw new RuntimeException("Erro sintatico: era esperado um identificador ou"
                    + " um inteiro ou um real ou '('");
        }
    }
    
    private void outros_termos(){
        if(simbolo.equals("+") || simbolo.equals("-")){
            op_ad();
            termo();
            outros_termos();
        }
    }
    
    private void op_ad(){
        switch(simbolo){
            case "+":
                aux_op= simbolo;
                obtemToken();
                break;
            case "-":
                aux_op = simbolo;
                obtemToken();
                break;
            default:
                throw new RuntimeException("Erro sintatico: era esperado '+' ou '-'");
        }
    }
    
    private void mais_fatores(){
        if(simbolo.equals("*") || simbolo.equals("/")){
            op_mul();
            fator();
            mais_fatores();
        }
    }
    
    private void op_mul(){
        switch(simbolo){
            case "*":
                aux_op = simbolo;
                obtemToken();
                break;
            case "/":
                aux_op = simbolo;
                obtemToken();
                break;
            default:
                throw new RuntimeException("Erro sintatico: era esperado '*' ou '/'");
        }
    }
    
    private void pfalsa(){
        if(simbolo.equals("else")){
            obtemToken();
            comandos();
        }
    }
    
    //FUNCAO AUXILIAR PARA A INSERCAO DA LINHA ESPECIFICA NO CODIGO
    private void reescrita(String atual, String addlinha){
        for (int a = C.size() - 1; a >= 0; a--) {
            String conteudo = C.get(a);
                if (conteudo.contains(atual)) {
                    C.set(a, conteudo.replace(atual, addlinha));
                  
                }
        }
        
    }
}
      


