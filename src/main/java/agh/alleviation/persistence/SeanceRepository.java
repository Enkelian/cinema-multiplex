package agh.alleviation.persistence;

import agh.alleviation.model.Movie;
import agh.alleviation.model.Seance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * Repository of seances.
 *
 * @author Ksenia Fiodarava
 * @see Seance
 */
public interface SeanceRepository extends CrudRepository<Seance, Integer> {

    /**
     * Find by id with tickets seance.
     *
     * @param id the id
     * @return the seance
     */
    @Query("SELECT s FROM Seance s LEFT JOIN FETCH s.tickets WHERE s.id = ?1")
    Seance findByIdWithTickets(int id);

}
