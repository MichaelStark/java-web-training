package by.stark.sample.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Genre extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	/*
	 * @JoinTable(name = "book_2_genre", joinColumns = { @JoinColumn(name =
	 * "genre_id") }, inverseJoinColumns = { @JoinColumn(name = "book_id") })
	 * 
	 * @ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY) private
	 * Set<Book> book;
	 */

	public String getName() {
		return name;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
