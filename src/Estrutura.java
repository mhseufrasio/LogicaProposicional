/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author PC
 */
public class Estrutura {
    private String[] dom;
    private List<String> consts = new ArrayList<String>();
    private List<String[]> funcoes = new ArrayList<String[]>();
    private List<List> predicados = new ArrayList<List>();
    private List<String> interpretacao = new ArrayList<String>();
    private List<String> variaveis = new ArrayList();
    private List<String> variaveisValores = new ArrayList();
    
    public Estrutura(String[] dominio){
        this.dom = dominio;
    }
//cria uma função para todos os valores possíveis baseados no domínio  
    public void criarFuncao(String nome, int aridade){
        int tamanhoFunc = (int)Math.pow(dom.length,aridade)+1;
        String[] funcao = new String[tamanhoFunc];
        funcao[0] = nome;
        List<String> args = new ArrayList<String>();
        gerarArranjos(aridade, args, 0, "");
        int i = 1;
        while(i < tamanhoFunc){
            funcao[i] = args.get(i - 1);
            i++;
        }
        funcoes.add(funcao);
    }

//gera arranjos com repetição de elementos do domínio
    private void gerarArranjos(int aridade, List<String> resultado, int nivel, String atual) {
        if (nivel == aridade) {
            resultado.add(atual);
            return;
        }

        for (String elemento : dom) {
            String aux = nivel == 0 ? elemento : atual + "," + elemento;
            gerarArranjos(aridade, resultado, nivel + 1, aux);
        }
    }

//cria uma variável e adiciona na interpretação
    public void criarVariavel(String x) {
        interpretacao.add(x);
    }
//cria uma constante valorada e adiciona na lista de constantes
    public void criarConstante(String c, int i) {
        consts.add(c+":"+i);
    }
//cria um predicado
    public void criarPredicado(String predicado, int aridade) {
        if(consultarPredicados(predicado) == null){
            List lista = new ArrayList();
            lista.add(predicado);
            lista.add(aridade);
            predicados.add(lista);
        }else{
            System.out.println("Já existe um predicado com este nome.");
        }
    }
//imprime a estrutura de forma ordenada
    public void imprimirEstrutura(){
        System.out.print("{");
        imprimirDom();
        if(predicados.size() != 0){
            System.out.print(",");
        }
        imprimirPredicados();
        if(funcoes.size() != 0){
            System.out.print(",");
        }
        imprimirFuncoes();
        if(consts.size() != 0){
            System.out.print(",");
        }
        imprimirConstantes();
        System.out.println("}");
    }
//imprime o domínio da estrutura de forma ordenada
    private void imprimirDom(){
        System.out.print("dom:[");
        int i = 0;
        while(i<dom.length){
            if(i+1 == dom.length){
                System.out.print(dom[i]);
            }else{
                System.out.print(dom[i]+",");
            }
            i++;
        }
        System.out.print("]");
    }
//imprime todas as funções de forma ordenada
    private void imprimirFuncoes(){
        Iterator interator = funcoes.iterator();
        while(interator.hasNext()){
            int a = 1;
            String[] funcao = (String[])interator.next();
            System.out.print(funcao[0]+":{");
            while(a<funcao.length){
                if(funcao[a].split(":").length == 2){
                    System.out.print("("+funcao[a].split(":")[0]+"):"+funcao[a].split(":")[1]);
                }else{
                    System.out.print("("+funcao[a]+")");
                }
                if(a+1 != funcao.length){
                    System.out.print(",");
                }
                a++;
            }
            System.out.print("}");
            if(interator.hasNext()){
                System.out.print(",");
            }
        }
    }

    private void imprimirPredicados(){
        int a,b=0;
        while(b < predicados.size()){
            System.out.print(predicados.get(b).get(0)+":{");
            a = 2;
            while(a < predicados.get(b).size()){
                System.out.print("("+predicados.get(b).get(a)+")");
                if(a+1 < predicados.get(b).size()){
                    System.out.print(",");
                }
                a++;
            }
            System.out.print("}");
            if(b+1<predicados.size()){
                System.out.print(",");
            }
            b++;
        }
    }

//imprime todas as constantes com seus valores de forma ordenada
    private void imprimirConstantes(){
        Iterator i = consts.iterator();
        int a = 0;
        while(i.hasNext()){
            if(a+1 != consts.size()){
                System.out.print(i.next()+",");
            }else{
               System.out.print(i.next());
            }
            a++;
        }
    }

//recebe uma função de um elemento e um valor para ela
    public void inserirValor(String string, int valor){
        if(consultarDominio(""+valor)){
//caso a função seja I, interpreta-se como sendo a função interpretação
            if(string.split("\\(")[0].equals("I")){
                inserirValorInterpretacao(string, valor);
                return;
            }
            Iterator i = funcoes.iterator();
//checa os nomes da funções até achar a função do termo
            while(i.hasNext()){
                String[] str = (String[])i.next();
                if(str[0].equals(string.split("\\(")[0])){
                    int a = 1;
//depois de achar o nome da função, procura o termo para atribuir o valor
                    while(a<str.length){
                        if(string.split("\\(")[1].split("\\)")[0].equals(str[a].split(":")[0])){
                           str[a]=str[a].split(":")[0]+":"+valor;
                           break;
                        }
                        a++;
                    }
                    break;
                }
            }
        }else{
            System.out.println("Valor fora do domínio da estrutura.");
        }
    }
    
    public void interpretarTermo(String string){
        if(contar(string)){
            String mensagem = interpretaTermo(string);
            if(mensagem == null){
                System.out.println("Há uma falha no termo.");
            }else{
                System.out.println(mensagem);
            }
        }else{
            System.out.println("Há uma falha no termo.");
        }
    }
    
//usa a interpretação da estrutura para 'resolver' o termo
    private String interpretaTermo(String termo){
        String e = null;
//se houver funções no termo
        if(termo.split("\\(").length >= 2){
            Iterator i = funcoes.iterator();
            while(i.hasNext()){
                String[] string = (String[])i.next();
                if(termo.split("\\(")[0].equals(string[0])){
                    String resultado = "",termoFormado="";
                    int a = 0;
                    String[] b = termo.substring(termo.split("\\(")[0].length()+1,termo.length()-1).split(",");
                    while(a<b.length){
                        if(contar(b[a])){
                            resultado = resultado + interpretaTermo(b[a])+",";
                            termoFormado = "";
                        }else if(contar(termoFormado)){
                            resultado = resultado + interpretaTermo(termoFormado.substring(0,termoFormado.length()-1))+",";
                            termoFormado = "";
                        }else{
                            termoFormado = termoFormado + b[a]+",";
                        }
                        a++;
                    }
                    a = 0;
                    if(contar(termoFormado)){
                        termoFormado = termoFormado.substring(0,termoFormado.length()-1);
                        resultado = resultado + interpretaTermo(termoFormado);
                    }else if(resultado != ""){
                        resultado = resultado.substring(0,resultado.length()-1);
                    }
                    while(a<string.length){
                        if(string[a].split(":")[0].equals(resultado)){
                            if(string[a].split(":").length != 1){
                                e = string[a].split(":")[1];
                                break;
                            }else{
                                System.out.println("Valor do termo "+string[0]+"("+resultado+") não está definido.");
                                exit(0);
                            }
                        }
                        a++;
                    }
                }
            }
        }else if(termo.split("\\(").length == 1){
//se houver somente constantes e/ou variáveis
            Iterator i = variaveisValores.iterator();
            if(variaveis.contains(termo)){
                if(variaveisValores.get(variaveis.indexOf(termo)).split(":").length > 1){
                    e = variaveisValores.get(variaveis.indexOf(termo)).split(":")[1];
                    return e;
                }
            }
                
            i = consts.iterator();
            while(i.hasNext()){
                String constante = (String)i.next();
                if(constante.split(":")[0].equals(termo)){
                    e = constante.split(":")[1];
                    return e;
                }
            }
            i = interpretacao.iterator();
            while(i.hasNext()){
                String inter = (String)i.next();
                if(inter.split(":")[0].equals(termo)){
                    if(inter.split(":").length == 1){
                        System.out.println("A interpretação de "+termo+" não foi definida.");
                        exit(0);
                    }else{
                        e = inter.split(":")[1];
                        return e;
                    }
                }
            }
            if(e == null){
                System.out.println("Erro no termo.");
                exit(0);
            }else{
                return termo;
            }
        }
        return e;
    }
//insere um valor de interpretacao
    private void inserirValorInterpretacao(String string, int valor) {
        int a = 0;
        while(a<interpretacao.size()){
            String str = interpretacao.get(a);
            if(string.split("\\(")[1].split("\\)")[0].equals(str.split(":")[0])){
                interpretacao.set(a,str.split(":")[0]+":"+valor);
                break;
            }
            a++;
        }
    }
/*
conta quantos '(' e quantos ')' tem na string
serve pra saber se a string é um termo completo ou apenas parte dele
seguindo a lógica de que um termo completo possui o mesmo número de parenteses abertos e fechados
*/
    private boolean contar(String string){
        boolean result = false;
        int a = 0,b=0,c=0;
        while(a<string.length()){
            if(string.charAt(a) == '('){
                b++;
            }else if(string.charAt(a) == ')'){
                c++;
            }
            a++;
        }
        if(b == c && string != ""){
            result = true;
        }
        return result;
    }
//checa se o valor escolhido pertece ao domínio da estrutura
    private boolean consultarDominio(String valor){
        int a = 0;
        while(a<dom.length){
            if(dom[a].equals(valor)){
                return true;
            }
            a++;
        }
        return false;
    }
//checa se já existe um predicado com o nome solicitado
    private Integer consultarPredicados(String predicado){
        int i = 0;
        while(i < predicados.size()){
            String nome = predicados.get(i).get(0).toString();
            if(nome.equals(predicado)){
                return i;
            }
            i++;
        }
        return null;
    }
//checa se o valor para aquele predicado já existe
    private Integer consultarValorPredicado(String valor, int indice){
        int i = 2;
        while(i < predicados.get(indice).size()){
            String nome = predicados.get(indice).get(i).toString();
            if(nome.equals(valor)){
                return i;
            }
            i++;
        }
        return null;
    }
    
    public void inserirValorPredicado(String predicado){
        if(contar(predicado)){
            Integer i = consultarPredicados(predicado.split("\\(")[0]);
            if(i != null && predicado.split(",").length == Integer.parseInt(predicados.get(i).get(1).toString())){
                if(consultarValorPredicado(predicado.substring(predicado.split("\\(")[0].length()+1,predicado.length()-1),i) == null){
                    predicados.get(i).add(predicado.substring(predicado.split("\\(")[0].length()+1,predicado.length()-1));
                }else{
                    System.out.println("Este valor já está no predicado.");
                }
            }else{
                System.out.println("O predicado não existe ou a aridade está incorreta.");
            }
        }else{
            System.out.println("Digite o predicado corretamente.");
        }
    }
    
    public boolean interpretaFormula(String formula){
        boolean resposta = false;
        if(contar(formula)){
            if(descobrirParametros(" implica ", formula).size() > 1){
                List<String> implica = descobrirParametros(" implica ", formula);
                boolean dois = interpretaFormula(implica.get(1));
                if(dois){
                        resposta = true;
                }else{
                    boolean um = interpretaFormula(implica.get(0));
                    if(!um){
                        resposta = true;
                    }
                }
            }else if(descobrirParametros(" e ", formula).size() > 1){
                List<String> e = descobrirParametros(" e ", formula);
                int i = 0;
                resposta = true;
                while(i<e.size() && resposta){
                    resposta = interpretaFormula(e.get(i));
                    i++;
                }
            }else if(descobrirParametros(" ou ", formula).size() > 1){
                List<String> ou = descobrirParametros(" ou ", formula);
                int i = 0;
                while(i<ou.size() && !resposta){
                    resposta = interpretaFormula(ou.get(i));
                    i++;
                }
            }else if(!formula.split("\\(")[0].equals("")){
                if(formula.split("\\(")[0].split("para todo ").length > 1){
                    resposta = true;
                    String vari = formula.split("\\(")[0].split("para todo ")[1];
                    String[] formQuebrada = formula.split(vari);
                    int a = 0;
                    boolean tem = false;
                    while (a < formQuebrada.length-1 && !tem){
                        if(((formQuebrada[a].endsWith("(") || formQuebrada[a].endsWith(",")) && (formQuebrada[a+1].startsWith(")") || formQuebrada[a+1].startsWith(",")))){
                            tem = true;
                            variavelLigada(vari);
                        }
                        a++;
                    }
                    a = 0;
                    if(tem){
                        String valorAnterior = variaveisValores.get(variaveis.indexOf(vari));
                        while(a < dom.length && resposta){
                            variaveisValores.set(variaveis.indexOf(vari),vari+":"+dom[a]);
                            resposta = interpretaFormula(formula.substring(formula.split("\\(")[0].length() + 1,formula.length()-1));
                            a++;
                        }
                        variaveisValores.set(variaveis.indexOf(vari),valorAnterior);
                    }else{
                        resposta = interpretaFormula(formula.substring(formula.split("\\(")[0].length() + 1,formula.length()-1));
                    }
                }else if(formula.split("\\(")[0].split("existe ").length > 1){
                    resposta = false;
                    String vari = formula.split("\\(")[0].split("existe ")[1];
                    String[] formQuebrada = formula.split(vari);
                    int a = 0;
                    boolean tem = false;
                    while (a < formQuebrada.length-1 && !tem){
                        if(((formQuebrada[a].endsWith("(") || formQuebrada[a].endsWith(",")) && (formQuebrada[a+1].startsWith(")") || formQuebrada[a+1].startsWith(",")))){
                            tem = true;
                            variavelLigada(vari);
                        }
                        a++;
                    }
                    a = 0;
                    if(tem){
                        String valorAnterior = variaveisValores.get(variaveis.indexOf(vari));
                        while(a < dom.length && !resposta){
                            variaveisValores.set(variaveis.indexOf(vari),vari+":"+dom[a]);
                            resposta = interpretaFormula(formula.substring(formula.split("\\(")[0].length() + 1,formula.length()-1));
                            a++;
                        }
                        variaveisValores.set(variaveis.indexOf(vari),valorAnterior);
                    }else{
                        resposta = interpretaFormula(formula.substring(formula.split("\\(")[0].length() + 1,formula.length()-1));
                    }
                }else if(formula.split("\\(")[0].equals("nao")){
                    resposta = !interpretaFormula(formula.substring(formula.split("\\(")[0].length() + 1,formula.length()-1));
                }else{
                    Integer i = consultarPredicados(formula.split("\\(")[0]);
                    if(i != null){
                        List<String> termos = descobrirParametros(",", formula.substring(formula.split("\\(")[0].length()+1,formula.length()-1));
                        int aridade = Integer.parseInt(predicados.get(i).get(1).toString());
                        if(termos.size() == aridade){
                            int a = 0;
                            if(aridade > 1){
                                String resultado = "";
                                while(a < termos.size()){
                                    resultado = resultado + interpretaTermo(termos.get(a));
                                    if(a+1 < termos.size()){
                                        resultado = resultado + ",";
                                    }
                                    a++;
                                }
                                if(consultarValorPredicado(resultado, i) != null){
                                    resposta = true;
                                }
                            }else{
                                String termo_unario = interpretaTermo(formula.substring(formula.split("\\(")[0].length()+1,formula.length()-1));
                                if(termo_unario.isEmpty()){
                                    System.out.println("Erro na fórmula.");
                                    exit(0);
                                }
                                if(consultarValorPredicado(termo_unario,i) != null){
                                    resposta = true;
                                }
                            }
                        }else{
                            System.out.println("Fórmula incorreta.");
                            exit(0);
                        }
                    }else{
                        System.out.println("Fórmula incorreta.");
                        exit(0);
                    }
                }
            }else{
                resposta = interpretaFormula(formula.substring(1,formula.length()-1));
            }
        }else{
            System.out.println("Fórmula incorreta.");
            exit(0);
        }
        return resposta;
    }
    
    private List<String> descobrirParametros(String separacao, String formula){
        String[] termos = null;
        List<String> termos_completos = new ArrayList();
        termos = formula.split(separacao);
        String termo_incompleto = "";
        int i = 0;
        if(termos.length >= 1){
            while(i < termos.length){
                if(contar(termos[i]) && termo_incompleto == ""){
                    termos_completos.add(termos[i]);
                }else{
                    termo_incompleto = termo_incompleto + termos[i] + separacao;
                }
                if(contar(termo_incompleto)){
                    termo_incompleto = termo_incompleto.substring(0, termo_incompleto.length()-separacao.length());
                    termos_completos.add(termo_incompleto);
                    termo_incompleto = "";
                }
                i++;
            }
        }
        return termos_completos;
    }
    
    private void variavelLigada(String variavel){
        if(!variaveis.contains(variavel)){
            variaveis.add(variavel);
            variaveisValores.add(variavel);
        }
    }
}
