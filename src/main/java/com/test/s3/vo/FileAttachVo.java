package com.test.s3.vo;

import java.util.Date;

public class FileAttachVo {

	private String attach_id;
	private Integer file_seqno;
	private String real_file_nm;
	private String save_file_nm;
	private Integer file_size;
	private String file_path;
	private Integer download_cnt;
	private String reg_id;
	private Date reg_dt;
	private String reg_ip;
	
	public String getAttach_id() {
		return attach_id;
	}
	public void setAttach_id(String attach_id) {
		this.attach_id = attach_id;
	}
	public Integer getFile_seqno() {
		return file_seqno;
	}
	public void setFile_seqno(Integer file_seqno) {
		this.file_seqno = file_seqno;
	}
	public String getReal_file_nm() {
		return real_file_nm;
	}
	public void setReal_file_nm(String real_file_nm) {
		this.real_file_nm = real_file_nm;
	}
	public String getSave_file_nm() {
		return save_file_nm;
	}
	public void setSave_file_nm(String save_file_nm) {
		this.save_file_nm = save_file_nm;
	}
	public Integer getFile_size() {
		return file_size;
	}
	public void setFile_size(Integer file_size) {
		this.file_size = file_size;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public Integer getDownload_cnt() {
		return download_cnt;
	}
	public void setDownload_cnt(Integer download_cnt) {
		this.download_cnt = download_cnt;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public Date getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(Date reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getReg_ip() {
		return reg_ip;
	}
	public void setReg_ip(String reg_ip) {
		this.reg_ip = reg_ip;
	}
	
	
}
