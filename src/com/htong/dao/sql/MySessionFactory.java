package com.htong.dao.sql;

import java.sql.Connection;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.htong.util.SQLServerConstants;

public class MySessionFactory {
	private static final ThreadLocal threadLocal = new ThreadLocal();
//	private static final Configuration cfg = new Configuration();
	private static SessionFactory sessionFactory;
	
	public static Session currentSession() {
		Session session = (Session) threadLocal.get();
		if(session == null) {
			if(sessionFactory == null) {
				try {
					Properties jdbcPros = new Properties();
					jdbcPros.setProperty("hibernate.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
					jdbcPros.setProperty("hibernate.connection.url", "jdbc:sqlserver://" + SQLServerConstants.instance.getIp() + ":1433;databaseName="+SQLServerConstants.instance.getDatabase());
					jdbcPros.setProperty("hibernate.connection.username", SQLServerConstants.instance.getUsername());
					jdbcPros.setProperty("hibernate.connection.password", SQLServerConstants.instance.getPassword());
					jdbcPros.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
					jdbcPros.setProperty("hibernate.show_sql", "false");
					jdbcPros.setProperty("hibernate.hbm2ddl.auto", "update");

					Configuration cfg = new Configuration();
					cfg.setProperties(jdbcPros);

					try {
					    cfg.addResource("TMeter2.hbm.xml");
					} catch (MappingException e) {
					    e.printStackTrace();
					}

					sessionFactory = cfg.buildSessionFactory();
				} catch (HibernateException e) {
					
					e.printStackTrace();
				}
			}
			System.out.println(sessionFactory == null);
			session = sessionFactory.openSession();
			threadLocal.set(session);
		}
		return session;
	}
	public static void closeSession() {
		Session session = (Session) threadLocal.get();
		threadLocal.set(null);
		if(session != null) {
			session.close();
		}
	}

}
