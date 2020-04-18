package fr.istic.iodeman.repositories;

import fr.istic.iodeman.models.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {
    Room findByName(String name);
}
