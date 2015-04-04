package by.stark.sample.dataaccess;

import java.util.Date;
import java.util.List;

import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.User;
import by.stark.sample.datamodel.enums.RecordStatus;

public interface Record4HandsDao extends AbstractDao<Long, Record4Hands> {

	List<Record4Hands> getAllRecordsByUser(User user);

	List<Record4Hands> getAllRecordsByLibriary(Libriary libriary);

	List<Record4Hands> getAllRecordsByStatus(RecordStatus status);

	List<Record4Hands> getAllRecordsByDateTake(Date dateTake);

	List<Record4Hands> getAllRecordsByDateReturn(Date dateReturn);

}
