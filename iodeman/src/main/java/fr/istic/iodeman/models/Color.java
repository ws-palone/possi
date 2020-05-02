package fr.istic.iodeman.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Color extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String code;

    @JsonIgnore
    @OneToMany(mappedBy = "color", cascade = CascadeType.PERSIST)
    private Collection<OralDefense> oralDefense;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Collection<OralDefense> getOralDefense() {
        return oralDefense;
    }

    public void setOralDefense(Collection<OralDefense> oralDefense) {
        this.oralDefense = oralDefense;
    }
}
