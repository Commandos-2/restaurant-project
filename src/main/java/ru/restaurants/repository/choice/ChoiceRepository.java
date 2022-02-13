package ru.restaurants.repository.choice;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.restaurants.model.Choice;
import ru.restaurants.util.exсeption.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class ChoiceRepository {

    private final CrudChoiceRepository crudChoiceRepository;
    @PersistenceContext
    private EntityManager em;

    public ChoiceRepository(CrudChoiceRepository crudChoiceRepository) {
        this.crudChoiceRepository = crudChoiceRepository;
    }

    public Choice save(Choice choice) {
        Assert.notNull(choice, "Choice must not be null");
        if (!choice.isNew() && get(choice.getId()) == null) {
            return null;
        }
        return crudChoiceRepository.save(choice);
    }

    public void delete(int id) {
        checkNotFoundWithId(crudChoiceRepository.delete(id) != 0, id);
    }

    public Choice get(int id) {
        return crudChoiceRepository.get(id).orElseThrow(new NotFoundException("Not found entity with ".concat(String.valueOf(id))));
    }

    public List<Choice> getAll() {
        return crudChoiceRepository.getAll();
    }

    public List<Choice> getAllByUserId(int userId) {
        return crudChoiceRepository.getAllByUserId(userId);
    }

    public Choice update(Choice choice) {
        Assert.notNull(choice, "Choice must not be null");
        return checkNotFoundWithId(save(choice), choice.getId());
    }

    public Choice getLastChoiceByUser(int userId) {
        Object choice = em.createQuery("SELECT u FROM Choice u WHERE u.user.id=?1 ORDER BY u.date DESC")
                .setMaxResults(1)
                .setParameter(1, userId)
                .getSingleResult();
        return (Choice) choice;
    }

    public Choice getToDayChoiceByUser(int userId) {
        return crudChoiceRepository.getToDayChoiceByUser(userId).orElseThrow(new NotFoundException("Not found entity with ".concat(String.valueOf(userId))));
    }
}
