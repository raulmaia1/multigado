package br.com.multigado.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.primefaces.model.DefaultStreamedContent;

public class FuncionarioBean {
	private String ordem, codigo, nome;
	private String apelido;
	private String localFoto = "//10.0.0.70/";

	public String getOrdem() {
		return ordem;
	}

	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getApelido() {
		return apelido;
	}

	@Override
	public String toString() {
		return "FuncionarioBean [codigo=" + codigo + ", nome=" + nome + ", apelido=" + apelido + " localFoto = "
				+ localFoto + "]";
	}

	public void setLocalFoto(String localFoto) {
		if (localFoto != null) {
			this.localFoto = this.localFoto + localFoto;
		}
	}

	public DefaultStreamedContent getLocalFoto() {		
		DefaultStreamedContent build = DefaultStreamedContent.builder().name(localFoto).contentType("image/jpg")
				.stream(() -> {
					try {
						return new ByteArrayInputStream(Files.readAllBytes(Paths.get("localFoto")));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}).build();
		
		return build;
		

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuncionarioBean other = (FuncionarioBean) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	

}
