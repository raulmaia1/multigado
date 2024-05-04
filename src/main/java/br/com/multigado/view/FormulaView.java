package br.com.multigado.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.multigado.bean.FormulaBean;
import br.com.multigado.bean.ProdutoBean;
import br.com.multigado.dao.MovimentoMultigadoFormulas;
import br.com.multigado.dao.ProdutoDAOSQLServerJTDS;

@ManagedBean
@ViewScoped
public class FormulaView {
	private String pesquisaProduto, pesquisaMateriaPrima;
	private Map<String, ProdutoBean> mapProdutos = new HashMap<String, ProdutoBean>();
	private Map<String, ProdutoBean> mapMateriaPrima = new HashMap<String, ProdutoBean>();
	private FormulaBean formulaBean = new FormulaBean();
	private List<FormulaBean> listaMateria = new ArrayList<>();

	public List<String> pesquisaProduto(String input) {
		mapProdutos.clear();
		List<String> l = new ArrayList<>();
		new ProdutoDAOSQLServerJTDS().procuraProdutos(input).forEach(r -> {
			l.add(r.getDescricaoProduto());
			mapProdutos.put(r.getDescricaoProduto(), r);
		});

		return l;
	}

	public List<String> pesquisaMateriaPrima(String input) {
		mapMateriaPrima.clear();
		List<String> l = new ArrayList<>();
		new ProdutoDAOSQLServerJTDS().procuraProdutos(input).forEach(r -> {
			l.add(r.getDescricaoProduto());
			mapMateriaPrima.put(r.getDescricaoProduto(), r);
		});

		return l;
	}

	public void adicionaFormula() {

		formulaBean.setMaterialPrima(mapMateriaPrima.get(pesquisaMateriaPrima));
		formulaBean.setProduto(mapProdutos.get(pesquisaProduto));

		new MovimentoMultigadoFormulas().adiciona(formulaBean);
		formulaBean = new FormulaBean();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Multigado", "Formula Adicionada com Sucesso"));
	}

	public void carregarFormulas() {
		listaMateria = new MovimentoMultigadoFormulas().carregaFormulaProdutos(mapProdutos.get(pesquisaProduto));
	}

	public void editarValor(FormulaBean materia) {
		new MovimentoMultigadoFormulas().editarValor(materia);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Multigado", "Valor Alterado com Sucesso"));
	}

	// getters
	public String getPesquisaProduto() {
		return pesquisaProduto;
	}

	public void setPesquisaProduto(String pesquisaProduto) {
		this.pesquisaProduto = pesquisaProduto;
	}

	public FormulaBean getFormulaBean() {
		return formulaBean;
	}

	public String getPesquisaMateriaPrima() {
		return pesquisaMateriaPrima;
	}

	public void setPesquisaMateriaPrima(String pesquisaMateriaPrima) {
		this.pesquisaMateriaPrima = pesquisaMateriaPrima;
	}

	public List<FormulaBean> getListaMateria() {
		return listaMateria;
	}

}
