package tech.noetzold.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user_adapter")
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String login;
    private String password;
}
