package by.stark.sample.services;

import java.util.Date;
import java.util.List;

import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;

public interface Record4HandsService extends
		AbstractService<Long, Record4Hands> {

	List<Record4Hands> getAll();

	List<Record4Hands> getAllRecordsByUser(Userprofile userprofile);

	List<Record4Hands> getAllRecordsByLibriary(Libriary libriary);

	List<Record4Hands> getAllRecordsByStatus(RecordStatus status);

	List<Record4Hands> getAllRecordsByDateTake(Date dateTake);

	List<Record4Hands> getAllRecordsByDateReturn(Date dateReturn);

}
