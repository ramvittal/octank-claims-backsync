package org.octank.claims.oracle.main;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.octank.claims.oracle.model.Claim;
import org.octank.claims.oracle.model.InsuranceCompany;
import org.octank.claims.oracle.model.MedicalProvider;
import org.octank.claims.oracle.model.Patient;
import org.octank.claims.oracle.model.Staff;


public class ClaimsGenerator {

	public static void main(String[] args) {
		
		BatchRequest request = new BatchRequest();
		request.setCount(10);
		
		request.setRequestId("CL-102-");
		request.setStatus("Submitted");
		
		Claim claim = new Claim();
		claim.setAmountClaimed(new BigDecimal(5000));
		
		InsuranceCompany ic = new InsuranceCompany();
		ic.setInsuranceCompanyId("IC102");
		claim.setInsuranceCompany(ic);
		claim.setInsurancePolicyNbr("IC102-102");
		
		MedicalProvider mp = new MedicalProvider();
		mp.setMedicalProviderId("MP102");
		
		claim.setMedicalProvider(mp);
		
		Patient p = new Patient();
		
		p.setPatientId("102");
		claim.setPatient(p);
		
		Staff s = new Staff();
		
		s.setStaffId("S102");
		
		claim.setStaff(s);
		
		
		
		request.setClaim(claim);
		
		generateClaims(request);
		

	}
	
	
	 public static String generateClaims(BatchRequest request) {
	       
		 SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		 Session sessionObj = null;
		 String status="success";
		 
		
		 
		 
		 try {
				sessionObj = sessionFactory.openSession();
				sessionObj.beginTransaction();
				

				System.out.println("begin");
				
				
				for(int i=0; i < request.getCount(); i++ )
				{
					
					Claim c = request.getClaim();
					c.setClaimStatus(request.getStatus());
					c.setClaimId(request.getRequestId() + i);
					c.setUpdatedDate(new Date());
					
					sessionObj.save(c);
					sessionObj.flush();
			        sessionObj.clear();
			        
			        System.out.println("saving claim:" +c.getClaimId());
				}

				// Committing The Transactions To The Database
				
				sessionObj.getTransaction().commit();
				
				


				
				
				
			} catch(Exception sqlException) {
				if(null != sessionObj.getTransaction()) {
					System.out.println("\n.......Transaction Is Being Rolled Back.......");
					sessionObj.getTransaction().rollback();
					status="failure";
				}
				sqlException.printStackTrace();
			} finally {
				if(sessionObj != null) {
					sessionObj.close();
				}
				
				
			}
		 
		 return status;

	        
	    }

}
