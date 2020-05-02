package fr.istic.iodeman.models.revision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.istic.iodeman.models.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class OralDefenseRevision extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Person student;

    @ManyToOne
    private Person followingTeacher;

    private String tutorFullName;

    private String company;

    @ManyToOne
    private RoomRevision room;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "from", column = @Column(name = "period_from")),
            @AttributeOverride(name = "to", column = @Column(name = "period_to"))
    })
    private TimeBox timeBox;

    @OneToOne
    private Person secondTeacher;

    @ManyToOne
    @JsonIgnore
    private PlanningRevision planning;

    private Integer number;

    @ManyToOne
    private Color color;

    @Transient
    private Collection<TimeBox> unavailabilities;

    public OralDefenseRevision() {}

    public OralDefenseRevision(OralDefense oralDefense) {
        this.student = oralDefense.getStudent();
        this.followingTeacher = oralDefense.getFollowingTeacher();
        this.secondTeacher = oralDefense.getSecondTeacher();
        this.tutorFullName = oralDefense.getTutorFullName();
        this.company = oralDefense.getCompany();
        this.color = oralDefense.getColor();
        this.number = oralDefense.getNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getStudent() {
        return student;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    public Person getFollowingTeacher() {
        return followingTeacher;
    }

    public void setFollowingTeacher(Person followingTeacher) {
        this.followingTeacher = followingTeacher;
    }

    public String getTutorFullName() {
        return tutorFullName;
    }

    public void setTutorFullName(String tutorFullName) {
        this.tutorFullName = tutorFullName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public RoomRevision getRoom() {
        return room;
    }

    public void setRoom(RoomRevision room) {
        this.room = room;
    }

    public TimeBox getTimeBox() {
        return timeBox;
    }

    public void setTimeBox(TimeBox timeBox) {
        this.timeBox = timeBox;
    }

    public Person getSecondTeacher() {
        return secondTeacher;
    }

    public void setSecondTeacher(Person secondTeacher) {
        this.secondTeacher = secondTeacher;
    }

    public PlanningRevision getPlanning() {
        return planning;
    }

    public void setPlanning(PlanningRevision planning) {
        this.planning = planning;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Collection<TimeBox> getUnavailabilities() {
        return unavailabilities;
    }

    public void setUnavailabilities(Collection<TimeBox> unavailabilities) {
        this.unavailabilities = unavailabilities;
    }
}
