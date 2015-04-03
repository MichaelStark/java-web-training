package by.stark.sample.datamodel;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Genre extends AbstractEntity {

	@Column
	private String name;

	@JoinTable(name = "book_2_genre", joinColumns = { @JoinColumn(name = "genre_id") }, inverseJoinColumns = { @JoinColumn(name = "book_id") })
	@ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY)
	private Set<Book> book;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
