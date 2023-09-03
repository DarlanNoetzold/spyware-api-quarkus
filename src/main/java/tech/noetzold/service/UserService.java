package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.noetzold.model.User;
import tech.noetzold.repository.UserRepository;

import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    private UserRepository userRepository;

    public List<User> findAllUsuarios(){
        return userRepository.findAll().list();
    }

    @Transactional
    public User saveUsuario(User user){
        userRepository.persist(user);
        return user;
    }

}
