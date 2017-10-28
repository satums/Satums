package cn.com.moudle.user.entity;

public class User {
	private int id;
	private String code;
	private String username;
	private String departcode;
	private String personpk;
	private String status;
	private String pwd;
	private String seqno;
	public String getSeqno() {
		return seqno;
	}
	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getDepartcode() {
		return departcode;
	}
	public void setDepartcode(String departcode) {
		this.departcode = departcode;
	}
	public String getPersonpk() {
		return personpk;
	}
	public void setPersonpk(String personpk) {
		this.personpk = personpk;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
