package by.stark.sample.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Picture extends AbstractEntity {

	@Column
	private String name;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
