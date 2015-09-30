package study.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import study.models.LinkInfo;
import study.models.User;

public interface LinkInfoRepository extends CrudRepository<LinkInfo, Long> {
Page<LinkInfo> findById(Long id, Pageable pageable);
Page<LinkInfo> findByAuthorId(Long authorId, Pageable pageable);
 // List<User> findFirst10ByIdNotIn(List<Integer> users);
}