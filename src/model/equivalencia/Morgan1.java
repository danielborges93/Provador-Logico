package model.equivalencia;

import java.util.HashMap;

/**
 *
 * @author danielborges93
 */
public class Morgan1 extends Equivalencia {

    /**
     * As duas partes com duas variáveis.
     */
    private final String padraoPosfixa2;
    private final String substituicaoPosfixa2;

    /**
     * As primeira parte com duas variáveis.
     */
    private final String padraoPosfixa3;
    private final String substituicaoPosfixa3;

    /**
     * As segunda parte com duas variáveis.
     */
    private final String padraoPosfixa4;
    private final String substituicaoPosfixa4;
    
    public Morgan1() {
	nome = "De Morgan";
	
//	padrao = "¬[(]([A-Z])\\^([A-Z])[)]";
//	substituicao = "(¬$1^¬$2)";
	
	padraoPosfixa = "(t|f|[A-Z]¬?)(t|f|[A-Z]¬?)\\^¬";
	substituicaoPosfixa = "$1¬$2¬v";
	
	padraoPosfixa2 = "([tfA-Z]¬?)([tfA-Z]¬?)([v|\\^|>])"
		+ "([tfA-Z]¬?)([tfA-Z]¬?)([v|\\^|>])\\^¬";
	substituicaoPosfixa2 = "$1$2$3¬$4$5$6¬v";
	
	padraoPosfixa3 = "([tfA-Z]¬?)([tfA-Z]¬?)([v|\\^|>])"
		+ "([tfA-Z]¬?)\\^¬";
	substituicaoPosfixa3 = "$1$2$3¬$4¬v";
	
	padraoPosfixa4 = "([tfA-Z]¬?)"
		+ "([tfA-Z]¬?)([tfA-Z]¬?)([v|\\^|>])\\^¬";
	substituicaoPosfixa4 = "$1¬$2$3$4¬v";
	
	this.padroesPosfixos = new HashMap<>();
	this.padroesPosfixos.put(padraoPosfixa, substituicaoPosfixa);
	this.padroesPosfixos.put(padraoPosfixa2, substituicaoPosfixa2);
	this.padroesPosfixos.put(padraoPosfixa3, substituicaoPosfixa3);
	this.padroesPosfixos.put(padraoPosfixa4, substituicaoPosfixa4);
    }
    
}
