package by.stark.sample.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class Libriary extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Book.class)
	private Book book;

	@Column
	private Long uin;

	@Column
	private Boolean availability;

	@Column
	private Boolean readingRoom;

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Long getUin() {
		return uin;
	}

	public void setUin(Long uin) {
		this.uin = uin;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public Boolean getReadingRoom() {
		return readingRoom;
	}

	public void setReadingRoom(Boolean readingRoom) {
		this.readingRoom = readingRoom;
	}

}
