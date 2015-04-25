package by.stark.sample.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Author extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String firstName;

	@Column
	private String lastName;

	/*
	 * @JoinTable(name = "book_2_author", joinColumns = { @JoinColumn(name =
	 * "author_id") }, inverseJoinColumns = { @JoinColumn(name = "book_id") })
	 * 
	 * @ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY) private
	 * Set<Book> book;
	 */

	@Override
	public Long getId() {
		return id;
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
}
