package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal,1));
    }
    @Override
    public Meal save(Meal meal, int userId) {
        if(meal.isNew()){
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(),meal);
            return meal;
        }
        if(meal.getUserId()==userId){
            return repository.computeIfPresent(meal.getId(),(id,value)->meal);}
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if(repository.containsKey(id)&&repository.get(id).getUserId()==userId)return repository.remove(id)!=null;
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal=repository.get(id);
        if(meal!=null&&meal.getUserId()==userId)return meal;
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId()==userId)
                .sorted((meal1,meal2)->meal2.getDate().compareTo(meal1.getDate()))
                .collect(Collectors.toList());
    }
}

