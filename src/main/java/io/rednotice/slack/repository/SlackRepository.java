package io.rednotice.slack.repository;

import io.rednotice.slack.entity.Slack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackRepository extends JpaRepository<Slack, Long> {
}
