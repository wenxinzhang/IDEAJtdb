package com.jt.web.pojo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class User extends BasePojo{
	private Long id;
	@Length(min=6,max=20,message="6-20位字符，支持中英文、数字及\"-\"、\"_\"组合")
	@NotEmpty(message="用户名必须填写")
	private String username;
	
	@Length(min=6,max=20,message="6-20位字符，支持中英文、数字及\"-\"、\"_\"组合")
	@NotEmpty(message="密码必须填写")
	private String password;
	
	@Length(min=11,max=11,message="手机必须是11位")
	private String phone;
	
	@Email(message="Email格式不正确，请重新填写")
	private String email;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
