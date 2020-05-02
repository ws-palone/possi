package fr.istic.iodeman.repositories;

import fr.istic.iodeman.models.Planning;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlanningRepository extends CrudRepository<Planning, Long> {
    Planning findByName(String name);

    @Query("select distinct p from Planning p " +
            "join p.oralDefenses o " +
            "where p.admin.uid = ?1 or " +
            "o.student.uid = ?1 or " +
            "o.followingTeacher.uid = ?1"
    )
    List<Planning> findByPerson(String uid);
}
