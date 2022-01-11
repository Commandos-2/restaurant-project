package ru.restaurants.repository.choice;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.restaurants.model.Choice;

import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaChoiceRepository implements ChoiceRepository {

    private final CrudChoiceRepository crudRepository;

    public DataJpaChoiceRepository(CrudChoiceRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Choice save(Choice choice) {
        Assert.notNull(choice, "Choice must not be null");
        if (!choice.isNew() && get(choice.getId()) == null) {
            return null;
        }
        return crudRepository.save(choice);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(crudRepository.delete(id) != 0, id);
    }

    @Override
    public Choice get(int id) {
        return checkNotFoundWithId(crudRepository.get(id).orElse(null), id);
    }

    @Override
    public List<Choice> getAll() {
        return crudRepository.getAll();
    }

    @Override
    public List<Choice> getAllByUserId(int userId) {
        return crudRepository.getAllByUserId(userId);
    }

    @Override
    public Choice update(Choice choice) {
        Assert.notNull(choice, "Choice must not be null");
        return checkNotFoundWithId(save(choice), choice.getId());
    }
}
