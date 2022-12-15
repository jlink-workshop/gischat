package org.heigit.gischat.web.repository;

import java.util.*;

import org.heigit.gischat.domain.*;

public interface ChatRepository {

	Optional<Chat> findById(int id);

	void save(Chat chat);
}
