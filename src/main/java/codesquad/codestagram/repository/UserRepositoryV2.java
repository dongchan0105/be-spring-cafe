package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryV2 extends JpaRepository<User,Long> {
    User findByLoginId(String LoginId);
}
