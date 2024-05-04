package br.com.multigado.bean;

import java.util.HashMap;
import java.util.Map;

public class FormaPagamento {
	
	Map<String, String> mapa = new HashMap<String, String>();
	
	public FormaPagamento() {
		mapa.put("1","Dinheiro");

		mapa.put("2","Cheque");

		mapa.put("3","Cartão de Crédito");

		mapa.put("4","Cartão de Débito");

		mapa.put("5","Crédito Loja");

		mapa.put("10","Vale Alimentação");

		mapa.put("11","Vale Refeição");

		mapa.put("12","Vale Presente");

		mapa.put("13","Vale Combustível");

		mapa.put("15","Boleto Bancário");

		mapa.put("16","Depósito Bancário");

		mapa.put("17","Pagamento Instantâneo (PIX)");

		mapa.put("18","Transferência bancária, Carteira Digital");

		mapa.put("19","Programa de fidelidade, Cashback, Crédito Virtual");

		mapa.put("90","Sem pagamento");

		mapa.put("99","Outros");

	}
	
	public String getFormaPagamento(String id) {
		return mapa.get(id.replace("<tPag>", "").replace("</tPag>", ""));
	}
	
	
}
