package io.rednotice.list.repository;

import io.rednotice.list.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListsRepository extends JpaRepository<Lists, Long> {
    List<Lists> findAllByBoard_WorkspaceId(Long workSpaceId);
}
