package com.zyp.entity;


public class Student {

	private String s_id;
	private String s_name;
	private boolean s_flag;
	public Student(String s_id, String s_name, boolean s_flag) {
		super();
		this.s_id = s_id;
		this.s_name = s_name;
		this.s_flag = s_flag;
	}
	public Student() {
		super();
	}
	@Override
	public String toString() {
		return "Student [s_id=" + s_id + ", s_name=" + s_name + ", s_flag=" + s_flag + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	public String getS_id() {
		return s_id;
	}
	public void setS_id(String s_id) {
		this.s_id = s_id;
	}
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	public boolean isS_flag() {
		return s_flag;
	}
	public void setS_flag(boolean s_flag) {
		this.s_flag = s_flag;
	}	
}
