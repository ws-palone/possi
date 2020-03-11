package fr.istic.iodeman.Audit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PriorityAudit {
    @Id
    @GeneratedValue
    private Integer id;

    private String role;

    private Integer weight;

    public PriorityAudit() {

    }

    public PriorityAudit(String role, Integer weight) {
        this.role = role;
        setWeight(weight);
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
