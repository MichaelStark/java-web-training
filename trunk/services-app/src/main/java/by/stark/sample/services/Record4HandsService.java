package by.stark.sample.services;

import java.util.Date;
import java.util.List;

import by.stark.sample.datamodel.Libriary;
import by.stark.sample.datamodel.Record4Hands;
import by.stark.sample.datamodel.Userprofile;
import by.stark.sample.datamodel.enums.RecordStatus;

public interface Record4HandsService extends
		AbstractService<Long, Record4Hands> {

	List<Record4Hands> getAllByUser(Userprofile user);

	List<Record4Hands> getAllByLibriary(Libriary libriary);

	List<Record4Hands> getAllByStatus(RecordStatus status);

	List<Record4Hands> getAllByDateTake(Date date);

	List<Record4Hands> getAllByDateReturn(Date date);

	Record4Hands getById(Long id);

}
