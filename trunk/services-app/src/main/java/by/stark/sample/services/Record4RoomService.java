package by.stark.sample.services;

import java.util.Date;
import java.util.List;

import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Room;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;

public interface Record4RoomService extends AbstractService<Long, Record4Room> {

	List<Record4Room> getAll();

	List<Record4Room> getAllRecordsByUser(Userprofile userprofile);

	List<Record4Room> getAllRecordsByLibriary(Libriary libriary);

	List<Record4Room> getAllRecordsByStatus(RecordStatus status);

	List<Record4Room> getAllRecordsByTimeTake(Date timeTake);

	List<Record4Room> getAllRecordsByTimeReturn(Date timeReturn);

}
