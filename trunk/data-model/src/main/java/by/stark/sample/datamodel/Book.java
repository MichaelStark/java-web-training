package by.stark.sample.datamodel;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Book extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Picture.class)
	private Picture picture;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Publisher.class)
	private Publisher publisher;

	@Column
	@NotNull
	@Size(max = 100)
	private String isbn;

	@Column
	@NotNull
	@Size(max = 100)
	private String title;

	@Column
	@NotNull
	@Max(value = 2999)
	@Min(value = 1)
	private Long year;

	@Column
	@NotNull
	@Max(value = 9999)
	@Min(value = 1)
	private Long pages;

	@Column
	@NotNull
	private String description;

	@JoinTable(name = "book_2_author", joinColumns = { @JoinColumn(name = "book_id") }, inverseJoinColumns = { @JoinColumn(name = "author_id") })
	@ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY)
	private Set<Author> authors;

	@JoinTable(name = "book_2_genre", joinColumns = { @JoinColumn(name = "book_id") }, inverseJoinColumns = { @JoinColumn(name = "genre_id") })
	@ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY)
	private Set<Genre> genres;

	@Override
	public Long getId() {
		return id;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Long getPages() {
		return pages;
	}

	public void setPages(Long pages) {
		this.pages = pages;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
