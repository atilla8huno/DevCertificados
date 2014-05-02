package br.com.devschool.util;

import javax.persistence.MappedSuperclass;

/**
 * @author	ATILLA
 * @date	10/09/2012
 * 
 */
@MappedSuperclass
public abstract class EntidadeGeral {
	
	/**
	 * Este método retorna o código do objeto
	 * 
	 * @return	Integer
	 */
	public abstract Integer getId();
	
	/**
	 * Este método verifica se objeto é novo (se ainda não tem código), 
	 * e retorna verdadeiro ou falso.
	 * 
	 * @author 	ATILLA
	 * @date	12/09/2012
	 * @return	Boolean
	 */
	public boolean isTransient(){
		return getId() == null || getId() <= 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		EntidadeGeral other = (EntidadeGeral) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}
