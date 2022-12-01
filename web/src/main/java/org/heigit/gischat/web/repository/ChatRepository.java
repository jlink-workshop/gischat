package org.heigit.gischat.web.repository;

import org.heigit.gischat.domain.*;

public interface ChatRepository {

	Chat findOne();

	void save(Chat chat);
}
