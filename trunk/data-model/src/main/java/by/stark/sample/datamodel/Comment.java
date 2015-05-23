package by.stark.sample.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import by.stark.sample.datamodel.enums.CommentRating;

@Entity
public class Comment extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Book.class)
	private Book book;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Userprofile.class)
	private Userprofile userprofile;

	@Column
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private CommentRating rating;

	@Column
	private String description;

	@Override
	public Long getId() {
		return id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Userprofile getUser() {
		return userprofile;
	}

	public void setUser(Userprofile userprofile) {
		this.userprofile = userprofile;
	}

	public CommentRating getRating() {
		return rating;
	}

	public void setRating(CommentRating rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
