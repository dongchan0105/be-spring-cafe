package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepositoryV2;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepositoryV2 userRepository;

    public UserService(UserRepositoryV2 userRepository) {
        this.userRepository = userRepository;
    }

    // 회원 가입
    public User registerUser(String name, String email, String loginId, String password) {
        User newUser = new User(name, email, loginId, password);
        return userRepository.save(newUser);
    }

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 특정 회원 조회
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // 회원 프로필 업데이트
    public void updateUserProfile(Long userId, String password, String name, String email) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(password);
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);  // 변경 사항 저장
        } else {
            throw new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다.");
        }
    }
}
