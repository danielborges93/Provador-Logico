package questao1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Expressao;
import model.TipoBusca;
import model.questao1.No;

/**
 * Resolução da questão 1:<br/>
 * a) Converter todas as proposições para a notação posfixa e criar a tabela
 * verdade.
 *
 * @author danielborges93
 */
public class Questao1 {

    /**
     * Executa a questão.
     *
     * @param proposicoesString <code>String</code> com todas as preposições
     * separadas por vírgula.
     * @param consequenciaStr <code>String</code> com a consequência.
     */
    public void exec(String proposicoesString, String consequenciaStr) {
	// Executar usando a tabela verdade
	comTabelaVerdade(proposicoesString, consequenciaStr);

	// Executar sem usar a tabela verdade
	semTabelaVerdade(proposicoesString, consequenciaStr);
    }

    /**
     * Executa a atividade com a tabela verdade.
     */
    private void comTabelaVerdade(String proposicoesString, String consequenciaStr) {

	// Guardar as proposições em um array
	String[] proposicoesArray = proposicoesString.split(", ");

//	Converter as proposições para o formato posfixo e salvar em uma lista
//	Aproveitar o 'for' para guardar os operandos
	List<Expressao> proposicoesList = new ArrayList<>();
	Set<Character> operandosSet = new HashSet<>();

	for (String proposicao : proposicoesArray) {
	    // Converter
	    Expressao expressao = new Expressao(proposicao, null);
	    proposicoesList.add(expressao);

	    // Recuperar os operandos
	    for (char c : proposicao.toCharArray()) {
		/*
		 * Verificar se o caractere está no intervalo [A-Z] e
		 * é diferente de 'v'
		 */
		if (Character.isAlphabetic(c) && c != 'v') {
		    operandosSet.add(c);
		}
	    }
	}

	// Fazer o mesmo com a consequência
	for (char c : consequenciaStr.toCharArray()) {
	    /*
	     * Verificar se o caractere está no intervalo [A-Z] e
	     * é diferente de 'v'
	     */
	    if (Character.isAlphabetic(c) && c != 'v') {
		operandosSet.add(c);
	    }
	}

	// Converter de Set para List
	List<Character> operandosList = new ArrayList<>(operandosSet);

	// Criar a tabela verdade
	int quantOperandos = operandosList.size();
	int quantProposicoes = proposicoesArray.length;

	int quantLinhas = (int) Math.pow(2, quantOperandos);
	int quantColunas = quantOperandos + quantProposicoes + 5;

	/*
	 Formato geral da tabela:
	
	 | operador1 | operador2 | ... | propo1 | propo2 | ... | conjunção | consequência1 | teorema1 | consequência2 | teorema2 |
	 */
	boolean[][] tabelaVerdade = new boolean[quantLinhas][quantColunas];
	preencherTabelaVerdade(tabelaVerdade, quantOperandos);

//	imprimirTabelaVerdade(tabelaVerdade);
//	System.exit(0);
	// Completar o resto da tabela verdade linha por linha
	for (boolean[] linha : tabelaVerdade) {
	    // Map com os valores dos operandos
	    Map<Character, Boolean> valores = new HashMap<>();
	    for (int i = 0; i < quantOperandos; i++) {
		char operando = operandosList.get(i);
		boolean valor = linha[i];
		valores.put(operando, valor);
	    }

	    // Resolver as proposições
	    // Aproveitar o for para formar a expressão da conjunção
	    StringBuilder conjuncaoStr = new StringBuilder();

	    int colunaAtual = quantOperandos;

	    for (; colunaAtual < quantOperandos + quantProposicoes; colunaAtual++) {
		Expressao proposicao = proposicoesList.get(colunaAtual - quantOperandos);
		boolean res = proposicao.resolver(valores);
		linha[colunaAtual] = res;

		char c = res ? 't' : 'f';
		conjuncaoStr.append(c);

		if (colunaAtual < quantOperandos + quantProposicoes - 1) {
		    conjuncaoStr.append("^");
		}
	    }

	    // Resolver a conjunção
	    Expressao conjuncao = new Expressao(conjuncaoStr.toString(), null);
	    linha[colunaAtual++] = conjuncao.resolver(valores);

	    // Resolver a consequência do teorema 1
	    Expressao consequencia = new Expressao(consequenciaStr, null);
	    linha[colunaAtual++] = consequencia.resolver(valores);

	    // Resolver o implica do teorema 1
	    String teorema1Str = ""
		    + (linha[colunaAtual - 2] ? 't' : 'f')
		    + ">"
		    + (linha[colunaAtual - 1] ? 't' : 'f');
	    Expressao teorema1 = new Expressao(teorema1Str, null);
	    linha[colunaAtual++] = teorema1.resolver(valores);

	    // Resolver a consequência do teorema 2
	    String consequencia2Str = "¬(" + consequenciaStr + ")";
	    Expressao consequencia2 = new Expressao(consequencia2Str, null);
	    linha[colunaAtual++] = consequencia2.resolver(valores);

	    // Resolver o implica do teorema 1
	    String teorema2Str = ""
		    + (linha[colunaAtual - 4] ? 't' : 'f')
		    + "^"
		    + (linha[colunaAtual - 1] ? 't' : 'f');
	    Expressao teorema2 = new Expressao(teorema2Str, null);
	    linha[colunaAtual++] = teorema2.resolver(valores);

	}

	// Imprimir a tabela verdade
	imprimirTabelaVerdade(tabelaVerdade, operandosList, proposicoesList, consequenciaStr, ("¬(" + consequenciaStr + ")"));

	// Dizer se é consequência lógica...
	boolean peloTeorema1 = true;
	boolean peloTeorema2 = true;

	// Percorrer todas as linhas da tabela verdade e verificar a coluna do teorema 1 e do teorema 2
	for (boolean[] linha : tabelaVerdade) {
	    // Valor do teorema 1 e do teorema 2 na tabela
	    boolean teorema1Resolvido = linha[quantColunas - 3];
	    boolean teorema2Resolvido = linha[quantColunas - 1];

	    // Se o valor resolvido do teorema 1 for falso...
	    if (teorema1Resolvido == false) {
		// ...quer dizer que pelo teorema 1 não é consequência lógica
		peloTeorema1 = false;
	    }

	    // Se o valor resolvido do teorema 2 for falso...
	    if (teorema2Resolvido == true) {
		// ...quer dizer que pelo teorema 2 não é consequência lógica
		peloTeorema2 = false;
	    }
	}

	// Imprimir a conclusão retirada da tabela verdade
	String nao = peloTeorema1 ? " " : " não ";
	System.out.println("De acordo com a tabela verdade, pelo teorema 1," + nao + "é consequência lógica.");

	nao = peloTeorema2 ? " " : " não ";
	System.out.println("De acordo com a tabela verdade, pelo teorema 2," + nao + "é consequência lógica.");

	System.out.println();
    }

    /**
     * Preenche os valores da tabela verdade.
     *
     * @param tabelaVerdade Tabela verdade.
     * @param quantOperandos Quantidade de operandos.
     */
    private void preencherTabelaVerdade(boolean[][] tabelaVerdade, int quantOperandos) {
	int qtd = quantOperandos;
	int qtdNumeros = (int) Math.pow(2, qtd);

	for (int j = 0; j < qtd; j++) {
	    int passo = (int) Math.pow(2, j);
	    boolean zero = false;

	    for (int i = 0; i < qtdNumeros; i++) {
		if (i % passo == 0) {
		    zero = !zero;
		}

		tabelaVerdade[i][j] = !zero;
	    }
	}

//	for (boolean[] linha : tabelaVerdade) {
//	    for (int i = 0; i < linha.length / 2; i++) {
//		boolean temp = linha[i];
//		linha[i] = linha[linha.length - i - 1];
//		linha[linha.length - i - 1] = temp;
//	    }
//	}
    }

    /**
     * Imprime a tabela verdade.
     *
     * @param tabelaVerdade Tabela verdade.
     */
    private void imprimirTabelaVerdade(boolean[][] tabelaVerdade, List<Character> operandosList, List<Expressao> proposicoes, String consequencia1, String consequencia2) {

	System.out.println();
	System.out.println("Tabela verdade:");
	for (int i = 0; i < tabelaVerdade[0].length; i++) {
	    System.out.print("++++++++");
	}
	System.out.println("|");
	for (char c : operandosList) {
	    System.out.print("|");
	    System.out.print(c);
	    System.out.print("\t");
	}
	for (Expressao expressao : proposicoes) {
	    System.out.print("|");
	    System.out.print(expressao.getInfixa());
	    System.out.print("\t");
	}
	System.out.print("|conj.\t");
	System.out.print("|" + consequencia1 + "\t");
	System.out.print("|teo. 1\t");
	System.out.print("|" + consequencia2 + "\t");
	System.out.print("|teo. 2\t");
	System.out.println("|");
	System.out.print("+");
	for (int i = 0; i < tabelaVerdade[0].length; i++) {
	    System.out.print("++++++++");
	}
	System.out.println();
	for (boolean[] linha : tabelaVerdade) {
	    for (boolean coluna : linha) {
		System.out.print("|");
		System.out.print(coluna);
		System.out.print("\t");
	    }
	    System.out.println("|");
	}
	System.out.print("+");
	for (int i = 0; i < tabelaVerdade[0].length; i++) {
	    System.out.print("++++++++");
	}
	System.out.println();
    }

    /**
     * Resolve a questão sem o uso da tabela verdade.
     *
     * @param proposicoesString
     * @param consequenciaStr
     */
    private void semTabelaVerdade(String proposicoesString, String consequenciaStr) {

	// Guardar as proposições em um array
	String[] proposicoesArray = proposicoesString.split(", ");

	// Formar a expressão do teorema 1 sem ser no formato do teorema,
	// e sim depois de realizar a equivalência da eliminação da implicação
	StringBuilder teoremaBuilder = new StringBuilder();

	teoremaBuilder.append("¬(");
	for (String proposicao : proposicoesArray) {
	    teoremaBuilder.append("(").append(proposicao).append(")");
	    teoremaBuilder.append("^");
	}
	teoremaBuilder.replace(teoremaBuilder.length() - 1, teoremaBuilder.length(), ")");

	teoremaBuilder.append("v(").append(consequenciaStr).append(")");

	String teorema1 = teoremaBuilder.toString();

	// Começar a busca do teorema 1...
	Busca buscaTeorema1 = new Busca(teorema1);
	No no = buscaTeorema1.buscar(TipoBusca.LARGURA);

	conclusaoTeorema(1, no);

	// Formar a expressão do teorema 2
	teoremaBuilder = new StringBuilder();

	for (String proposicao : proposicoesArray) {
	    teoremaBuilder.append("(").append(proposicao).append(")");
	    teoremaBuilder.append("^");
	}

	teoremaBuilder.append("¬(").append(consequenciaStr).append(")");

	String teorema2 = teoremaBuilder.toString();

	// Começar a busca do teorema 1...
	Busca buscaTeorema2 = new Busca(teorema2);
	No no2 = buscaTeorema2.buscar(TipoBusca.LARGURA);

	conclusaoTeorema(2, no2);
    }

    /**
     * Imprime a conclusão tirada atravé da utilização das equivalências.
     *
     * @param teorema Teorema a ser avaliado.
     * @param no Nó final.
     */
    private void conclusaoTeorema(int teorema, No no) {
	// Se o nó final não for nulo, quer dizer que conseguiu chegar em
	// uma tautologia ou em uma contradição
	if (no != null) {

	    System.out.print("Utilizando equivalências com o teorema " + teorema + ", ");

	    // Se for o teorema 1
	    if (teorema == 1) {
		// Se for uma contradição
		if (no.getExpressao().equals("f")) {
		    System.out.print("não ");
		}
	    } // Se for o teorema 2
	    else {
		// Se for uma tautologia
		if (no.getExpressao().equals("t")) {
		    System.out.print("não ");
		}
	    }

	    // Se for uma contradição...
	    System.out.println("é uma consequência lógica.");

	    System.out.println("Caminho até chegar a esta conclusão:");
	    imprimirCaminho(no);

	} else {
	    System.out.println("Não pôde chegar a uma conclusão com o teorema " + teorema + "...");
	}

	System.out.println();
    }

    /**
     * Imprime o caminho que chegou até o nó especificado.
     *
     * @param no Último nó gerado.
     */
    private void imprimirCaminho(No no) {
	// Se o nó não for nulo (não é a raiz), chama recursivamente com o pai
	// como parámetro
	if (no != null) {
	    imprimirCaminho(no.getPai());

	    System.out.print(no);
	    
	    if(no.getEquivalenciaUtilizada() != null) {
		System.out.print("\t\t [" + no.getEquivalenciaUtilizada() + "]");
	    }
	    
	    System.out.println();
	}

    }

}
