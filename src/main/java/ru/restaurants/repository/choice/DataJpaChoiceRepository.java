package ru.restaurants.repository.choice;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.restaurants.model.Choice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaChoiceRepository implements ChoiceRepository {

    @PersistenceContext
    private EntityManager em;

    private final CrudChoiceRepository crudChoiceRepository;

    public DataJpaChoiceRepository(CrudChoiceRepository crudChoiceRepository) {
        this.crudChoiceRepository = crudChoiceRepository;
    }

    @Override
    public Choice save(Choice choice) {
        Assert.notNull(choice, "Choice must not be null");
        if (!choice.isNew() && get(choice.getId()) == null) {
            return null;
        }
        return crudChoiceRepository.save(choice);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(crudChoiceRepository.delete(id) != 0, id);
    }

    @Override
    public Choice get(int id) {
        return checkNotFoundWithId(crudChoiceRepository.get(id).orElse(null), id);
    }

    @Override
    public List<Choice> getAll() {
        return crudChoiceRepository.getAll();
    }

    @Override
    public List<Choice> getAllByUserId(int userId) {
        return crudChoiceRepository.getAllByUserId(userId);
    }

    @Override
    public Choice update(Choice choice) {
        Assert.notNull(choice, "Choice must not be null");
        return checkNotFoundWithId(save(choice), choice.getId());
    }

    @Override
    public Choice getLastChoiceByUser(int userId) {
        Object choice = em.createQuery("SELECT u FROM Choice u WHERE u.user.id=?1 ORDER BY u.registered DESC")
                .setMaxResults(1)
                .setParameter(1, userId)
                .getSingleResult();
        return (Choice) choice;
    }
}
