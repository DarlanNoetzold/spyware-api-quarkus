package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.User;


@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
