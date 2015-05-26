package by.stark.sample.services;

import java.util.Date;
import java.util.List;

import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;

public interface Record4RoomService extends AbstractService<Long, Record4Room> {

	List<Record4Room> getAllByUser(Userprofile user);

	List<Record4Room> getAllByLibriary(Libriary libriary);

	List<Record4Room> getAllByStatus(RecordStatus status);

	List<Record4Room> getAllByTimeTake(Date date);

	List<Record4Room> getAllByTimeReturn(Date date);

	Record4Room getById(Long id);

	List<Record4Room> getAllByDateTake(Date date, Date nextDay);

	List<Record4Room> getAllByDateReturn(Date date, Date nextDay);

}
