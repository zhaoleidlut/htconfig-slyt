package com.htong.dao.sql;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TMeter2Dao {

	public TMeter2 saveOrUpdate(TMeter2 tmeter2) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();

			session.saveOrUpdate(tmeter2);
			session.flush();

			ts.commit();
		} catch (HibernateException e) {
			if (ts != null) {
				ts.rollback();
			}
			e.printStackTrace();
		} finally {
			MySessionFactory.closeSession();
		}
		return tmeter2;

	}

	public void deleteAll() {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();

			Query query = session.createQuery("delete from TMeter2");
			System.out.println("删除数据个数：" + query.executeUpdate());

			ts.commit();
		} catch (HibernateException e) {
			if (ts != null) {
				ts.rollback();
			}
			e.printStackTrace();
		} finally {
			MySessionFactory.closeSession();
		}

	}

	public void saveOrUpdate(List<TMeter2> tmeter2List) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();

			if (tmeter2List != null & !tmeter2List.isEmpty()) {
				for (TMeter2 tm2 : tmeter2List) {
					session.saveOrUpdate(tm2);
				}
			}
			session.flush();

			ts.commit();
		} catch (HibernateException e) {
			if (ts != null) {
				ts.rollback();
			}
			e.printStackTrace();
		} finally {
			MySessionFactory.closeSession();
		}

	}

	public TMeter2 getByDtuIdAndDeviceId(int channelId, int deviceId) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		TMeter2 tm = null;
		try {
			ts = session.beginTransaction();

			Query query = session
					.createQuery("from TMeter2 as a where a.dtuId='"
							+ channelId + "' and a.deviceId='" + deviceId + "'");
			tm = (TMeter2) query.uniqueResult();

			ts.commit();
		} catch (HibernateException e) {
			if (ts != null) {
				ts.rollback();
			}
			e.printStackTrace();
		} finally {
			MySessionFactory.closeSession();
		}
		return tm;
	}

	public TMeter2 getByMId(int mid) {
		MySessionFactory msf = new MySessionFactory();
		Session session = msf.currentSession();
		Transaction ts = null;
		TMeter2 tm = null;
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery("from TMeter2 as a where a.id='"
					+ mid + "'");
			tm = (TMeter2) query.uniqueResult();
			ts.commit();
		} catch (HibernateException e) {
			if (ts != null) {
				ts.rollback();
			}
			e.printStackTrace();
		} finally {
			MySessionFactory.closeSession();
		}
		return tm;
	}

}
