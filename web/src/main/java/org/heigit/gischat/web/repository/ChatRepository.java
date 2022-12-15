package org.heigit.gischat.web.repository;

import java.util.*;

import org.heigit.gischat.domain.*;

public interface ChatRepository {

	Chat findOne();
	Optional<Chat> findById(int id);

	void save(Chat chat);
}
