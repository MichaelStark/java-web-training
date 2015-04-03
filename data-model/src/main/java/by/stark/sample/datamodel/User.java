package by.stark.sample.datamodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import by.stark.sample.datamodel.enums.UserGender;
import by.stark.sample.datamodel.enums.UserRole;
import by.stark.sample.datamodel.enums.UserStatus;

@Entity
public class User extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Picture.class)
	private Picture picture;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private UserRole role;

	@Column(updatable = true)
	private String email;

	@Column
	private String password;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private UserGender gender;

	@Column
	private Date birthday;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private UserStatus status;

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserGender getGender() {
		return gender;
	}

	public void setGender(UserGender gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

}
