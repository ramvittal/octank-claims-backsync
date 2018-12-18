package org.octank.claims.oracle.main;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.octank.claims.api.model.APIClaim;
import org.octank.claims.oracle.model.Claim;
import org.octank.claims.oracle.model.InsuranceCompany;
import org.octank.claims.oracle.model.MedicalProvider;
import org.octank.claims.oracle.model.Patient;
import org.octank.claims.oracle.model.Staff;

import com.google.gson.Gson;


public class ClaimsGenerator {

	public static void main(String[] args) {
		
		BatchRequest request = new BatchRequest();
		request.setCount(1);
		
		request.setRequestId("CL-102-2-");
		request.setStatus("Submitted");
		
		Claim claim = new Claim();
		claim.setAmountClaimed(new BigDecimal(5000));
		
		InsuranceCompany ic = new InsuranceCompany();
		ic.setInsuranceCompanyId("IC102");
		claim.setInsuranceCompany(ic);
		claim.setInsurancePolicyNbr("IC101-102");
		
		MedicalProvider mp = new MedicalProvider();
		mp.setMedicalProviderId("MP102");
		
		claim.setMedicalProvider(mp);
		
		Patient p = new Patient();
		
		p.setPatientId("101");
		claim.setPatient(p);
		
		Staff s = new Staff();
		
		s.setStaffId("S102");
		
		claim.setStaff(s);
		
		
		
		request.setClaim(claim);
		
		//setupPatient(claim);
		
		generateClaims(request);
		

	}
	
	
	public static void setupPatient(Claim claim)   {
		
		
		
		
	}
	
	
	 public static String generateClaims(BatchRequest request) {
	       
		 SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		 Session sessionObj = null;
		 String status="success";
		 
		
		 
		 
		 try {
				sessionObj = sessionFactory.openSession();
				sessionObj.beginTransaction();
				

				System.out.println("begin");
				
				
				String hql1 = "from Claim where updatedDate >= :from and updatedDate <= :to";
				
				
				Query query1 = sessionObj.createQuery(hql1);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date from = sdf.parse("2018-12-17 00:00");
				Date to = sdf.parse("2018-12-17 23:59");
				
				
				query1.setParameter("from", from);
				query1.setParameter("to", to);
				
				
				List<Claim> lc1 = query1.list();
				
				List<APIClaim> acl = new ArrayList();
				
				for(Claim c : lc1) {
					System.out.println(c.getClaimId());
					//System.out.println(c.getPatient().getGender());
					APIClaim ac = new APIClaim();
					ac.setClaimId(c.getClaimId());
					acl.add(ac);
				}
				
				
				String jsonCl = new Gson().toJson(acl);
				
				System.out.println(jsonCl);
				

				String hql = "from Patient where patientName = :keyword";
				
				String keyword = "Aaron Davis";
				Query query = sessionObj.createQuery(hql);
				query.setParameter("keyword", keyword);
			
				List<Patient> lp = query.list();
				
				for (Patient p : lp) {
					System.out.println(p.getPatientName());
				}
				
				
				for(int i=0; i < request.getCount(); i++ )
				{
					
					
					 query = 
							sessionObj.createSQLQuery("select CLAIM_SEQ.nextval as num from dual")
					            .addScalar("num", StandardBasicTypes.BIG_INTEGER);
					
					Long lc = ((BigInteger) query.uniqueResult()).longValue();
					
										
					Claim c = request.getClaim();
					c.setClaimStatus(request.getStatus());
					c.setClaimId(lc.longValue()+"");
					c.setUpdatedDate(new Date());
					c.setMedicalCode("S02.10XA");
					
					String gcid = (String) sessionObj.save(c);
					sessionObj.flush();
			        sessionObj.clear();
			        
			        System.out.println("saving claim:"+c.getClaimId());
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
