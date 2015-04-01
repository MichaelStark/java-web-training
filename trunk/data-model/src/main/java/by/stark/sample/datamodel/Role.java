package by.stark.sample.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import by.stark.sample.datamodel.enums.UserRole;

@Entity
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private UserRole name;

	public UserRole getName() {
		return name;
	}

	public void setName(UserRole name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}
}
