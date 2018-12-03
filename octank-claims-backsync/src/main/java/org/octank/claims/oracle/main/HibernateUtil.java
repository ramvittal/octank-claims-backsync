package org.octank.claims.oracle.main;

/**
 * @author rvvittal
 */
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.octank.claims.oracle.model.Claim;
import org.octank.claims.oracle.model.InsuranceCompany;
import org.octank.claims.oracle.model.MedicalProvider;
import org.octank.claims.oracle.model.Patient;
import org.octank.claims.oracle.model.Staff;

//import com.fasterxml.classmate.AnnotationConfiguration;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
    	
        if (null != sessionFactory) {
        	System.out.println("hibernate session NOT NULL");
            return sessionFactory;
        }
        
        System.out.println("hibernate session configure");
        
        Configuration configuration = new Configuration();
      
        configuration.configure("hibernate.cfg.xml");
      
        
        configuration.addAnnotatedClass(Claim.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(InsuranceCompany.class);
        configuration.addAnnotatedClass(MedicalProvider.class);
        configuration.addAnnotatedClass(Staff.class);
        
        System.out.println("hibernate session set serviceRegistry");
       
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        
        try {
        	System.out.println("hibernate session factory build begin");
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            System.out.println("hibernate session factory build end");
        } catch (HibernateException e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
        return sessionFactory;
    }
}
