package by.stark.sample.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import by.stark.sample.datamodel.enums.CommentRating;

@Entity
public class Comment extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Book.class)
	private Book book;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	private User user;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private CommentRating rating;

	@Column
	private String description;

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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