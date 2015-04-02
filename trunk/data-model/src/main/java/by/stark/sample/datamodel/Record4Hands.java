package by.stark.sample.datamodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import by.stark.sample.datamodel.enums.RecordStatus;

@Entity
public class Record4Hands {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Book.class)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	private Libriary libriary;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private RecordStatus status;

	@Column
	private Date date_take;

	@Column
	private Date date_return;

	@Column
	private String description;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Libriary getLibriary() {
		return libriary;
	}

	public void setLibriary(Libriary libriary) {
		this.libriary = libriary;
	}

	public RecordStatus getStatus() {
		return status;
	}

	public void setStatus(RecordStatus status) {
		this.status = status;
	}

	public Date getDate_take() {
		return date_take;
	}

	public void setDate_take(Date date_take) {
		this.date_take = date_take;
	}

	public Date getDate_return() {
		return date_return;
	}

	public void setDate_return(Date date_return) {
		this.date_return = date_return;
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
