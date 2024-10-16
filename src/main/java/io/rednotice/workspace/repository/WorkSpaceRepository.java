package io.rednotice.workspace.repository;

import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.workspace.entity.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkSpaceRepository extends JpaRepository<WorkSpace, Long> {

    @Query("SELECT w.name FROM WorkSpace w WHERE w.id IN :workSpaceIdList")
    List<String> findByIdList(@Param("workSpaceIdList") List<Long> workSpaceIdList);

    default WorkSpace getWorkspaceById(Long workspcaeId) {
        return findById(workspcaeId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_WORKSPACE)
        );
    }
}
