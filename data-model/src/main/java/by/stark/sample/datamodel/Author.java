package by.stark.sample.datamodel;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Author extends AbstractEntity {

	@Column
	private String firstName;

	@Column
	private String lastName;

	@JoinTable(name = "book_2_author", joinColumns = { @JoinColumn(name = "author_id") }, inverseJoinColumns = { @JoinColumn(name = "book_id") })
	@ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY)
	private Set<Book> book;

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
}
