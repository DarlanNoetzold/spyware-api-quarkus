package tech.noetzold.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.noetzold.model.User;
import tech.noetzold.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    @Inject
    private UserRepository userRepository;

    public List<User> findAllUsuarios(){
        return userRepository.findAll().list();
    }

    public void saveUsuario(User user){
        userRepository.persist(user);
    }

}
