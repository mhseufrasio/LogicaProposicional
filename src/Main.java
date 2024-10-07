/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        String[] dom = {"1","2","3"};
        Estrutura a = new Estrutura(dom);
        a.criarFuncao("f", 1);
        a.criarFuncao("g",2);
        a.criarConstante("c",1);
        a.criarConstante("d",2);
        a.criarVariavel("y");
        a.criarVariavel("x");
        a.criarVariavel("z");
        a.inserirValor("f(1)", 2);
        a.inserirValor("f(2)", 3);
        a.inserirValor("f(3)", 3);
        a.inserirValor("g(2,1)", 3);
        a.inserirValor("g(1,1)", 3);
        a.inserirValor("g(1,2)", 1);
        a.inserirValor("g(2,2)", 2);
        a.imprimirEstrutura();
        a.inserirValor("I(x)", 1);
        a.interpretarTermo("f(c)");
        a.criarPredicado("F", 1);
        a.criarPredicado("P", 2);
        a.inserirValorPredicado("F(1)");
        a.inserirValorPredicado("F(3)");
        a.inserirValorPredicado("P(1,2)");
        a.inserirValorPredicado("P(2,1)");
        a.inserirValorPredicado("P(1,3)");
        System.out.println(a.interpretaFormula("F(c)"));
        System.out.println(a.interpretaFormula("F(c) e P(d,c)"));
        System.out.println(a.interpretaFormula("F(c) e F(d)"));
        System.out.println(a.interpretaFormula("nao(F(c))"));
        System.out.println(a.interpretaFormula("para todo x(existe y(P(y,x)))"));
        a.imprimirEstrutura();
    }
}