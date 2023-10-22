package com.smallworld.data;

import java.util.Objects;

public class Transaction {
    // transaction data.
	private String mtn;
    private double amount;
    private String senderFullName;
    private int senderAge;
    private String beneficiaryFullName;
    private int beneficiaryAge;
    private int issueId;
    private boolean issueSolved;
    private String issueMessage;
    
    
    // Constructor
    public Transaction(String mtn, double amount, String senderFullName, int senderAge,String beneficiaryFullName, int beneficiaryAge, int issueId,boolean issueSolved, String issueMessage) {
        this.mtn = mtn;
        this.amount = amount;
        this.senderFullName = senderFullName;
        this.senderAge = senderAge;
        this.beneficiaryFullName = beneficiaryFullName;
        this.beneficiaryAge = beneficiaryAge;
        this.issueId = issueId;
        this.issueSolved = issueSolved;
        this.issueMessage = issueMessage;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(mtn, that.mtn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mtn);
    }

	@Override
	public String toString() {
		return "Transaction{" +
				"mtn='" + mtn + '\'' +
				", amount=" + amount +
				", senderFullName='" + senderFullName + '\'' +
				", senderAge=" + senderAge +
				", beneficiaryFullName='" + beneficiaryFullName + '\'' +
				", beneficiaryAge=" + beneficiaryAge +
				", issueId=" + issueId +
				", issueSolved=" + issueSolved +
				", issueMessage='" + issueMessage + '\'' +
				'}';
	}

	// getters and setters
	
    public String getMtn() {
		return mtn;
	}
	public void setMtn(String mtn) {
		this.mtn = mtn;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSenderFullName() {
		return senderFullName;
	}
	public void setSenderFullName(String senderFullName) {
		this.senderFullName = senderFullName;
	}
	public int getSenderAge() {
		return senderAge;
	}
	public void setSenderAge(int senderAge) {
		this.senderAge = senderAge;
	}
	public String getBeneficiaryFullName() {
		return beneficiaryFullName;
	}
	public void setBeneficiaryFullName(String beneficiaryFullName) {
		this.beneficiaryFullName = beneficiaryFullName;
	}
	public int getBeneficiaryAge() {
		return beneficiaryAge;
	}
	public void setBeneficiaryAge(int beneficiaryAge) {
		this.beneficiaryAge = beneficiaryAge;
	}
	public int getIssueId() {
		return issueId;
	}
	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}
	public boolean isIssueSolved() {
		return issueSolved;
	}
	public void setIssueSolved(boolean issueSolved) {
		this.issueSolved = issueSolved;
	}
	public String getIssueMessage() {
		return issueMessage;
	}
	public void setIssueMessage(String issueMessage) {
		this.issueMessage = issueMessage;
	}

}
