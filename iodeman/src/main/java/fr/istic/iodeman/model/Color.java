package fr.istic.iodeman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Color {
    @Id
    @GeneratedValue
    private Integer id;
    private String code;

    @JsonIgnore
    @OneToMany(mappedBy = "color", cascade = CascadeType.PERSIST)
    private Collection<OralDefense> oralDefense;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
