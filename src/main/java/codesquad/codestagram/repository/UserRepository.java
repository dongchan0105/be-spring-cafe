package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>(); // DB 대신 ArrayList 사용

    // 회원 저장
    public void saveUser(String name, String email,String loginId, String password) {
        User user = new User(name, email ,loginId, password);
        users.add(user);
    }

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return users;
    }

    // 특정 회원 조회 (id 기준)
    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByLoginId(String loginId){
        return getAllUsers().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public void updateUserProfile(Long id,String password,String name,String email){
        Optional<User> byLoginId = findById(id);
        if(byLoginId.isPresent()){
            User user = byLoginId.get();
            user.setPassword(password);
            user.setName(name);
            user.setEmail(email);
        }
    }

    public void clearStore(){
        users.clear();
    }
}