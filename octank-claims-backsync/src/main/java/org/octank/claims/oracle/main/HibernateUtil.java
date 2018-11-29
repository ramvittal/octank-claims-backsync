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
        if (null != sessionFactory)
            return sessionFactory;
        
        Configuration configuration = new Configuration();
      
        configuration.configure("hibernate.cfg.xml");
      
        
        configuration.addAnnotatedClass(Claim.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(InsuranceCompany.class);
        configuration.addAnnotatedClass(MedicalProvider.class);
        configuration.addAnnotatedClass(Staff.class);
       
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        
        try {
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
        return sessionFactory;
    }
}
