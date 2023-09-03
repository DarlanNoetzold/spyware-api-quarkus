package tech.noetzold.model;

import java.io.Serializable;
import java.util.Calendar;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wildfly.common.annotation.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alert implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "sequence")
    private Long id;

    @NotNull
    private String pcId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    private String processos;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_cadastro", nullable = false)
    private Calendar dataCadastro;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Alert other = (Alert) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
