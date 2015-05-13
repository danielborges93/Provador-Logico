package model.regrasInferencias;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author danielborges93
 */
public class Absorcao extends Regra {

    public Absorcao() {
	this.entrada1 = "^([A-Z])>([A-Z])$";
	this.saida = "$1>($1^$2)";
    }
    
    @Override
    public void usar(List<String> proposicoes) {
	
	// posição da proposição
	int posicao = -1;
	
	// Proposição de entrada
	String prop = null;
	
	// Percorrer a lista
	for (String proposicao : proposicoes) {
	    
	    // Detectar o padrão
	    Pattern pattern = Pattern.compile(this.entrada1);
	    Matcher matcher = pattern.matcher(proposicao);
	    
	    // Se foi detectado o padrão...
	    if(matcher.find()) {
		posicao = proposicoes.indexOf(proposicao);
		prop = proposicao;
		break;
	    }
	}
	
	// Verifica se achou algo
	if (posicao > -1) {
	    // Se sim...
	    System.out.println("Utilizada a absorção com " + prop);
	    
	    // Remove a entrada da lista
	    proposicoes.remove(prop);
	    
	    // Cria a nova proposição
	    Pattern pattern = Pattern.compile(this.entrada1);
	    Matcher matcher = pattern.matcher(prop);
	    
	    prop = matcher.replaceAll(this.saida);
	    proposicoes.add(prop);
	}
	
    }
    
}