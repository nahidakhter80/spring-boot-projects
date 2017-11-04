package com.schedule.generator.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.schedule.generator.entity.ScheduledTrain;
import com.schedule.generator.exception.ExcelException;

@Repository
public class ScheduleDao {
	@PersistenceContext
	EntityManager entityManager;
	
	protected final Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	public void insertScheduledTrains(List<ScheduledTrain> list) throws ExcelException {
		getSession().createSQLQuery("truncate table SCHEDULED_TRAIN").executeUpdate();
		
		for (ScheduledTrain st : list) {
			try {
				getSession().save(st);
			} catch (NonUniqueObjectException e) {
				System.out.println("Duplicate train found " + st.getTrainNo());
				throw new ExcelException("Duplicate train found" + st.getTrainNo(), e);
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ScheduledTrain> getStbList() {
		return getSession().createCriteria(ScheduledTrain.class)
				.add(Restrictions.in("destination", new Object[] {"ADH", "BVI", "VR"}))
				.add(Restrictions.eq("nextTrainNo", "STB"))
				.addOrder(Order.asc("destination"))
				.list();
	}
	
	public void updateEtyRemark(List<ScheduledTrain> list) {
		for (ScheduledTrain st : list) {
			getSession().saveOrUpdate(st);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ScheduledTrain> getTrainList() {
		return getSession().createCriteria(ScheduledTrain.class)
				.list();
	} 

}
