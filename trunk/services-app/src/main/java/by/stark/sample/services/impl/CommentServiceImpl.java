package by.stark.sample.services.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.stark.sample.datamodel.Comment;
import by.stark.sample.services.CommentService;

@Service
public class CommentServiceImpl extends AbstractServiceImpl<Long, Comment>
		implements CommentService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CommentServiceImpl.class);

	@PostConstruct
	private void init() {
		// this method will be called by Spring after bean instantiation. Can be
		// used for any initialization process.
		LOGGER.info("Instance of CommentService is created. Class is: {}",
				getClass().getName());
	}

}
