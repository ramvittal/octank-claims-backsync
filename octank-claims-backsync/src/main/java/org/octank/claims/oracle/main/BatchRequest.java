package org.octank.claims.oracle.main;

import org.octank.claims.oracle.model.Claim;

public class BatchRequest {
	
	public BatchRequest() {
		
	}
	
	private String requestId;
	private String status;
	private int count;
	private Claim claim;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Claim getClaim() {
		return claim;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
	}

}
