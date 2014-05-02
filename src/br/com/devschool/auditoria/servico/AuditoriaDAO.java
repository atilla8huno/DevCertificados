package br.com.devschool.auditoria.servico;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.devschool.entidade.Auditoria;
import br.com.devschool.util.DAOGenerico;
import br.com.devschool.util.DataUtil;
import br.com.devschool.util.PersistenciaException;

@Repository
public class AuditoriaDAO extends DAOGenerico<Auditoria> {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Auditoria> pesquisar(Date data) throws PersistenciaException {
		try {
			Criteria criteria = createCriteria().addOrder(Order.desc("data"));

			if (data != null) {
				criteria.add(Restrictions.ge("data", DataUtil.removerHorario(data)));
				criteria.add(Restrictions.lt("data", DataUtil.removerHorario(DataUtil.adicionarPeriodo(data, Calendar.DAY_OF_MONTH, 1))));
			}

			return criteria.list();
		} catch (Exception e) {
			throw new PersistenciaException(e);
		}
	}
}
