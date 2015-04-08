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
public class Record4Hands extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Userprofile.class)
	private Userprofile userprofile;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Libriary.class)
	private Libriary libriary;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private RecordStatus status;

	@Column
	private Date dateTake;

	@Column
	private Date dateReturn;

	@Column
	private String description;

	public Userprofile getUser() {
		return userprofile;
	}

	public void setUser(Userprofile userprofile) {
		this.userprofile = userprofile;
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

	public Date getDateTake() {
		return dateTake;
	}

	public void setDateTake(Date dateTake) {
		this.dateTake = dateTake;
	}

	public Date getDateReturn() {
		return dateReturn;
	}

	public void setDateReturn(Date dateReturn) {
		this.dateReturn = dateReturn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
