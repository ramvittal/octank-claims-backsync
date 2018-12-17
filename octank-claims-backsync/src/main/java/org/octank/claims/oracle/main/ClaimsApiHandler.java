package org.octank.claims.oracle.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.octank.claims.api.model.APIClaim;
import org.octank.claims.oracle.model.Claim;
import org.octank.claims.oracle.model.InsuranceCompany;
import org.octank.claims.oracle.model.MedicalProvider;
import org.octank.claims.oracle.model.Patient;
import org.octank.claims.oracle.model.Staff;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class ClaimsApiHandler implements RequestStreamHandler {

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// TODO Auto-generated method stub

	}

	
	public void saveClaim(
  		  InputStream inputStream, 
  		  OutputStream outputStream, 
  		  Context context)
  		  throws IOException {
  		 
  		    JSONParser parser = new JSONParser();
  		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
  		    JSONObject responseJson = new JSONObject();
  		     String claimNbr = null;
  		 
  		 
  		    try {
  		        JSONObject event = (JSONObject) parser.parse(reader);
  		 
  		        if (event.get("body") != null) {
  		            APIClaim claim = new APIClaim((String) event.get("body"));
  		            
  		            //call a method to map this to db model claim and save it
  		            
  		            System.out.println("begin parseNSaveClaim");
  		            
  		          claimNbr = parseNSaveClaim(claim);
  		            
  		 
  		        }
  		        
  		         
  		 
  		        JSONObject responseBody = new JSONObject();
  		        
  		      if(claimNbr != null)  
  		        responseBody.put("message", "New Claim created with Claim # " +claimNbr);
  		      else 
  		    	  responseBody.put("message", "Claim creation failed");
  		 
  		        JSONObject headerJson = new JSONObject();
  		        headerJson.put("x-custom-header", "my custom header value");
  		 
  		        responseJson.put("statusCode", 200);
  		        responseJson.put("headers", headerJson);
  		        responseJson.put("body", responseBody.toString());
  		 
  		    } catch (Exception pex) {
  		        responseJson.put("statusCode", 400);
  		        responseJson.put("exception", pex);
  		    }
  		 
  		    OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
  		    writer.write(responseJson.toString());
  		    writer.close();
  		}
	
	private String parseNSaveClaim(APIClaim apiClaim) {
		
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		 Session sessionObj = null;
		 String status="success";
		 String claimNbr="";
		
		
		try {
					sessionObj = sessionFactory.openSession();
					sessionObj.beginTransaction();
				
					Claim claim = new Claim();
					claim.setAmountClaimed(new BigDecimal(apiClaim.getAmountClaimed()));
					
					//gen claim id
					Query query = 
							sessionObj.createSQLQuery("select CLAIM_SEQ.nextval as num from dual")
					            .addScalar("num", StandardBasicTypes.BIG_INTEGER);
					String claimId = ((BigInteger) query.uniqueResult()).longValue() + "";
					claim.setClaimId(claimId);
					
					
					InsuranceCompany ic = new InsuranceCompany();
					ic.setInsuranceCompanyId(apiClaim.getInsuranceCompanyId());
					claim.setInsuranceCompany(ic);
					
					claim.setInsurancePolicyNbr(apiClaim.getInsurancePolicyNbr());
				
					
					MedicalProvider mp = new MedicalProvider();
					mp.setMedicalProviderId(apiClaim.getMedicalProviderId());
					claim.setMedicalProvider(mp);
					
					//find existing patient by name and zip, if not found then create it
					
					String patientId = setupPatient(sessionObj, apiClaim);
					
					Patient p = new Patient();
					p.setPatientId(patientId);
					
					claim.setPatient(p);
					
					Staff s = new Staff();
					
					s.setStaffId(apiClaim.getStaffId());
					claim.setStaff(s);
					
					claim.setClaimStatus(apiClaim.getClaimStatus());
					
					claim.setUpdatedDate(new Date());
					claim.setMedicalCode(apiClaim.getMedicalCode());
					
						
					System.out.println("saving claim:" +claim.getClaimId());
						
					sessionObj.save(claim);
					
					claimNbr = claim.getClaimId();
					
					//sessionObj.flush();
			       // sessionObj.clear();
			        
			      //  System.out.println("saved claim:" +claim.getClaimId());
				
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
		
		return claimNbr;
		
	}
	
	
	String setupPatient(Session session, APIClaim apiClaim)  {
		
		String hql = "from Patient where patientName = :patientName and patientZip = :patientZip";
		
		String patientName = apiClaim.getPatientName();
		String patientZip = apiClaim.getPatientZip();
		String patientId = null;
		Query query = session.createQuery(hql);
		
		System.out.println("PatientName:" + patientName);
		System.out.println("PatientZip:" + patientZip);
		
		query.setParameter("patientName", patientName);
		query.setParameter("patientZip", patientZip);
	
		List<Patient> lp = query.list();
		
		for (Patient p : lp) {
			patientId = p.getPatientId();
			System.out.println(p.getPatientId() +"-" +p.getPatientName());
		}
		 
		if(patientId == null)  {
			
			query = 
					session.createSQLQuery("select PATIENT_SEQ.nextval as num from dual")
			            .addScalar("num", StandardBasicTypes.BIG_INTEGER);
			
			patientId = ((BigInteger) query.uniqueResult()).longValue() + "";
			
			Patient p = new Patient();
			p.setPatientId(patientId);
			
			p.setDateOfBirth(apiClaim.getPatientDateOfBirth());
			p.setGender(apiClaim.getGender());
			p.setPatientAddress(apiClaim.getPatientAddress());
			p.setPatientCity(apiClaim.getPatientCity());
			p.setPatientCountry(apiClaim.getPatientCountry());
			p.setPatientName(apiClaim.getPatientName());
			p.setPatientState(apiClaim.getPatientState());
			p.setPatientZip(apiClaim.getPatientZip());
			System.out.println("saving patient");
			session.save(p);
			
		}
		
		
		return patientId;
		
		
		
	}
  
}
