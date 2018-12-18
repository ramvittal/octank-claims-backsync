package org.octank.claims.api.model;

import java.text.SimpleDateFormat;
import java.util.Date;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class APIClaim {
	
	private String claimId;
	private String claimStatus;
	private Date updatedDate;
	private double amountClaimed;
	private double amountPaid;
	private String insurancePolicyNbr;
	private String patientId;
	private String gender;
	private Date patientDateOfBirth;
	private String patientName;
	private String patientAddress;
	private String patientCity;
	private String patientState;
	private String patientZip;
	private String patientCountry;
	private String medicalCode;
	private String medicalProviderId;
	private String insuranceCompanyId;
	private String staffId;
	
	public APIClaim() {
		
	}
	
	 public APIClaim(String json) {
		 
	        Gson gson = new Gson();
	        
	        APIClaim request = gson.fromJson(json, APIClaim.class);
	        this.claimId = request.getClaimId();
	        this.claimStatus = request.getClaimStatus();
	        this.updatedDate = request.getUpdatedDate();
	        this.amountClaimed = request.getAmountClaimed();
	        this.insurancePolicyNbr = request.getInsurancePolicyNbr();
	        this.gender = request.getGender();
	        this.patientId = request.getPatientId();
	        this.patientDateOfBirth = request.getPatientDateOfBirth();
	        this.patientName = request.getPatientName();
	        this.patientAddress = request.getPatientAddress();
	        this.patientCity = request.getPatientCity();
	        this.patientZip = request.getPatientZip();
	        this.patientState = request.getPatientState();
	        this.patientCountry = request.getPatientCountry();
	        this.medicalCode = request.getMedicalCode();
	        this.medicalProviderId = request.getMedicalProviderId();
	        this.insuranceCompanyId = request.getInsuranceCompanyId();
	        this.staffId = request.getStaffId();
	        
	        
	    }
	
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public String getMedicalProviderId() {
		return medicalProviderId;
	}
	public void setMedicalProviderId(String medicalProviderId) {
		this.medicalProviderId = medicalProviderId;
	}
	public String getInsuranceCompanyId() {
		return insuranceCompanyId;
	}
	public void setInsuranceCompanyId(String insuranceCompanyId) {
		this.insuranceCompanyId = insuranceCompanyId;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public double getAmountClaimed() {
		return amountClaimed;
	}
	public void setAmountClaimed(double amountClaimed) {
		this.amountClaimed = amountClaimed;
	}
	public double getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getInsurancePolicyNbr() {
		return insurancePolicyNbr;
	}
	public void setInsurancePolicyNbr(String insurancePolicyNbr) {
		this.insurancePolicyNbr = insurancePolicyNbr;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getPatientDateOfBirth() {
		return patientDateOfBirth;
	}
	public void setPatientDateOfBirth(Date dateOfBirth) {
		this.patientDateOfBirth = dateOfBirth;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientAddress() {
		return patientAddress;
	}
	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}
	public String getPatientCity() {
		return patientCity;
	}
	public void setPatientCity(String patientCity) {
		this.patientCity = patientCity;
	}
	public String getPatientState() {
		return patientState;
	}
	public void setPatientState(String patientState) {
		this.patientState = patientState;
	}
	public String getPatientZip() {
		return patientZip;
	}
	public void setPatientZip(String patientZip) {
		this.patientZip = patientZip;
	}
	public String getPatientCountry() {
		return patientCountry;
	}
	public void setPatientCountry(String patientCountry) {
		this.patientCountry = patientCountry;
	}
	public String getMedicalCode() {
		return medicalCode;
	}
	public void setMedicalCode(String medicalCode) {
		this.medicalCode = medicalCode;
	}
	
	 public String toString() {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        return gson.toJson(this);
	    }
	
	public static void main(String[] args) { 
		
		APIClaim aclaim = new APIClaim();
		
		aclaim.setAmountClaimed(100.50);
		aclaim.setClaimId("123");
		aclaim.setClaimStatus("Submitted");
		aclaim.setGender("Male");
		aclaim.setInsuranceCompanyId("IC-101");
		aclaim.setInsurancePolicyNbr("IC101-101");
		aclaim.setMedicalCode("S02.10XA");
		aclaim.setMedicalProviderId("MP101");
		aclaim.setPatientName("Aaron Davis");
		aclaim.setPatientId("101");
		aclaim.setPatientAddress("1000 NW Millazo Lane");
		aclaim.setPatientCity("Portland");
		aclaim.setPatientCountry("US");
		aclaim.setPatientState("OR");
		aclaim.setPatientZip("97229");
		aclaim.setStaffId("S101");
		aclaim.setPatientDateOfBirth(new Date());
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		
		
		 Gson gson = new Gson();
		 
		 String json = gson.toJson(aclaim);
		 
		 System.out.println(json);
		 
		 json = json.replace("\"", "\\\"");
		 
		 System.out.println(json);
		 
		//  aclaim = gson.fromJson(json, APIClaim.class);
		//  System.out.println(sdf.format(aclaim.getPatientDateOfBirth()));
		
	}

}
