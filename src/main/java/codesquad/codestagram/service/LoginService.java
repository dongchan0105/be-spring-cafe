package codesquad.codestagram.service;

import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.UserRepositoryV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private final UserRepositoryV2 userRepository;

    public LoginService(UserRepositoryV2 userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String loginId, String password) {
        User findByLoginId = userRepository.findByLoginId(loginId);
        if(findByLoginId.getPassword().equals(password))
            return findByLoginId;
        return null;
    }


}
