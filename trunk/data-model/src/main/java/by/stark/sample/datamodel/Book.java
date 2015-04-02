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

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Picture.class)
	private Picture picture;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Publisher.class)
	private Publisher publisher;

	@Column
	private String isbn;

	@Column
	private String title;

	@Column
	private Long year;

	@Column
	private Long pages;

	@Column
	private String description;

	@JoinTable(name = "book_2_author", joinColumns = { @JoinColumn(name = "book_id") }, inverseJoinColumns = { @JoinColumn(name = "author_id") })
	@ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY)
	private Set<Author> author;

	@JoinTable(name = "book_2_genre", joinColumns = { @JoinColumn(name = "book_id") }, inverseJoinColumns = { @JoinColumn(name = "genre_id") })
	@ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY)
	private Set<Genre> genre;

	public Set<Genre> getGenre() {
		if (genre == null) {
		}

		return genre;
	}

	public void setGenre(Set<Genre> genre) {
		this.genre = genre;
	}

	public Set<Author> getAuthor() {
		if (author == null) {
		}

		return author;
	}

	public void setAuthor(Set<Author> author) {
		this.author = author;
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

	public Long getId() {
		return id;
	}
}
