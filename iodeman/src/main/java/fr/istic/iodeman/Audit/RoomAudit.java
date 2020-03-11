package fr.istic.iodeman.Audit;

import fr.istic.iodeman.model.Room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class RoomAudit implements Comparable<RoomAudit> {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    /*public int compareTo(Room o) {
        return getName().compareTo(o.getName());
    }*/

    @Override
    public int compareTo(RoomAudit o) {
        return 0;
    }
}
