package dvc.guardtec.door.entity;

import java.io.Serializable;

public class AccuDetail implements Serializable {
	private static final long serialVersionUID = -2360999322675960936L;
	private String oid;
	private String accuNo;
	private String kdNo;
	private String kdName;
	private String doorContactNo;
	private String doorStrikeNo;
	
	public String getAccuNo() {
		return accuNo;
	}
	public void setAccuNo(String accuNo) {
		this.accuNo = accuNo;
	}
	public String getKdNo() {
		return kdNo;
	}
	public void setKdNo(String kdNo) {
		this.kdNo = kdNo;
	}
	public String getKdName() {
		return kdName;
	}
	public void setKdName(String kdName) {
		this.kdName = kdName;
	}
	public String getDoorContactNo() {
		return doorContactNo;
	}
	public void setDoorContactNo(String doorContactNo) {
		this.doorContactNo = doorContactNo;
	}
	public String getDoorStrikeNo() {
		return doorStrikeNo;
	}
	public void setDoorStrikeNo(String doorStrikeNo) {
		this.doorStrikeNo = doorStrikeNo;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	
}
