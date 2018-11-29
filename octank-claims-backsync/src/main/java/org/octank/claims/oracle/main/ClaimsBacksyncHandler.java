package org.octank.claims.oracle.main;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.octank.claims.oracle.model.Claim;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


/**
 * @author rvvittal
 */
public class ClaimsBacksyncHandler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String request, Context context) {
    	
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        
        
        System.out.println("request:" +request);
        
        try (Session session = sessionFactory.openSession()) {
        	
            session.beginTransaction();
            
            //1 . parse request and get list of claims ids
            
            List<String> claims = new ArrayList<String>(Arrays.asList(request.split(",")));

	         
	         //2. for each submitted claim, perform edits, create ClaimProcessing record, update oracle claim status, and send log record to elastic search
	         
	         for (String claimIdSt : claims) {
	        	 
	        	 String[] claimIn = claimIdSt.split("~");
	        	 
	        	 Claim claim = session.load(Claim.class, claimIn[0]);
	        	 claim.setClaimStatus(claimIn[1]);
	        	 claim.setUpdatedDate(new Date());
	        	 
	        	 session.update(claim);
	        	 System.out.println("updated claim:" +claimIn[0]);
		          
		      }
	         
	         
	         
            session.getTransaction().commit();
        }

        return request;
    }
}

