package br.com.multigado.view.relatorios;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;

import br.com.multigado.bean.OrdemServicoBean;
import br.com.multigado.dao.OrdemServicoDAOSQLServerJTDS;
import br.com.multigado.util.DataUtil;

@ViewScoped
@ManagedBean
public class RelatorioProducaoView {
	private Date mesPesquisa;
	private List<OrdemServicoBean> list = new ArrayList<OrdemServicoBean>();
	
	@PostConstruct
	private void init() {
		list.clear();
		mesPesquisa = GregorianCalendar.getInstance().getTime();
		
		list.addAll(new OrdemServicoDAOSQLServerJTDS().carregaQuantidadeDeProducoesPorData(new DateTime(mesPesquisa).dayOfMonth().withMinimumValue(), new DateTime(DataUtil.ultimoDiaDoMes(mesPesquisa))));
	}
	
	public Integer getQuantProducoes() {
		return list.size();
	}
	
	public void procuraDados() {
		System.out.println(DataUtil.ultimoDiaDoMes(mesPesquisa));
		
		init();
		
	}
	
	public Date getMesPesquisa() {
		return mesPesquisa;
	}
	
	public void setMesPesquisa(Date mesPesquisa) {
		this.mesPesquisa = mesPesquisa;
	}
	
	
}
