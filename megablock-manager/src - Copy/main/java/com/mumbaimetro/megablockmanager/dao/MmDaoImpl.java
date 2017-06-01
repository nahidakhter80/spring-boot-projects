package com.mumbaimetro.megablockmanager.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mumbaimetro.megablockmanager.dto.Train;
import com.mumbaimetro.megablockmanager.dto.TrainSetMap;

@Repository
public class MmDaoImpl implements MmDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	/*@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;*/
	
	protected Session getCurrentSession()  {
		return entityManager.unwrap(Session.class);
	}
	
	public void writeToDb(List<TrainSetMap> trainSets) {
		Session session = getCurrentSession();		
		for (TrainSetMap ts : trainSets) {
			session.save(ts);
		}		
	}
	
	public int updateCancellationStatus(List<String> cancelledTrains) {
		Query q = getCurrentSession().createQuery( "update TrainSetMap set isCancelled = :isCancelled where trainNumber in (:trainNumber)")
				.setBoolean("isCancelled", Boolean.TRUE)
				.setParameterList("trainNumber", cancelledTrains);
		
		int updatedRows = q.executeUpdate();
		return updatedRows;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCancelledSetNumbers() {
		
		return getCurrentSession()
				.createCriteria(TrainSetMap.class)
				.setProjection(Projections.distinct(
						Projections.projectionList().add(Projections.property("setNumber"))))
				.add(Restrictions.eq("isCancelled", Boolean.TRUE))			    
			    .list();
		
		/*String sql = "SELECT DISTINCT SET_NUMBER " + 
				"FROM TRAIN_SET " + 
				"WHERE IS_CANCELLED = :isCancelled ";
		Map<String, Boolean> paramMap = new HashMap<String, Boolean>();
		paramMap.put("isCancelled", Boolean.TRUE);
		return namedParameterJdbcTemplate.queryForList(sql, paramMap, String.class);*/
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<TrainSetMap> getCancelledTrainsBySetNumber(String setNumber) {
		return getCurrentSession()
				.createCriteria(TrainSetMap.class)
				.add(Restrictions.eq("setNumber", setNumber))
				.add(Restrictions.eq("isCancelled", Boolean.TRUE))
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<Train> getNonCancelledTrainsBySetNumber(String setNumber) {
		
		ProjectionList p=Projections.projectionList();
        p.add(Projections.property("trainNumber"), "trainNumber");
        p.add(Projections.property("isNos"), "isNos");	
        
        List<Train> trains = getCurrentSession()
				.createCriteria(TrainSetMap.class)
				.setProjection(p)
				.add(Restrictions.eq("setNumber", setNumber))
				.add(Restrictions.ne("isCancelled", Boolean.TRUE))
				.setResultTransformer(Transformers.aliasToBean(Train.class))
			    .list();
        return trains;
		
		/*String sql = "SELECT TRAIN_NUMBER FROM TRAIN_SET WHERE SET_NUMBER= :setNumber AND IS_CANCELLED IS NOT TRUE";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("setNumber",setNumber);
		return namedParameterJdbcTemplate.queryForList(sql, paramMap, String.class);*/
	}
}
