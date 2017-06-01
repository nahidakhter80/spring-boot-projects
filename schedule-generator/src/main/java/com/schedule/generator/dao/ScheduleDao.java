package com.schedule.generator.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.schedule.generator.entity.ScheduledTrain;

@Repository
public class ScheduleDao {
	@PersistenceContext
	EntityManager entityManager;
	
	protected final Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	public void insertScheduledTrains(List<ScheduledTrain> list) {
		getSession().createSQLQuery("truncate table SCHEDULED_TRAIN").executeUpdate();
		
		for (ScheduledTrain st : list) {
			getSession().save(st);
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
