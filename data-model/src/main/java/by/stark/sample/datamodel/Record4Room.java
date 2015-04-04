package by.stark.sample.datamodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import by.stark.sample.datamodel.enums.RecordStatus;

@Entity
public class Record4Room extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Book.class)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	private Libriary libriary;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private RecordStatus status;

	@Column
	private Date timeTake;

	@Column
	private Date timeReturn;

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

	public Date getTimeTake() {
		return timeTake;
	}

	public void setTimeTake(Date timeTake) {
		this.timeTake = timeTake;
	}

	public Date getTimeReturn() {
		return timeReturn;
	}

	public void setTimeReturn(Date timeReturn) {
		this.timeReturn = timeReturn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
