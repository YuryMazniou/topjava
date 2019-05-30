package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
        System.out.println("================================================");
        System.out.println(getFilteredWithExceededOptimal(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
        System.out.println("================================================");
        System.out.println(getFilteredOptimalPlusCycle(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
        System.out.println("================================================");
        System.out.println(getFilteredOptimalPlusStream(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
        System.out.println("================================================");
        System.out.println(getFiltered(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map=new HashMap<>();
        for (UserMeal u:mealList) {
            map.put(u.getDateTime().toLocalDate(),map.getOrDefault(u.getDateTime().toLocalDate(),0)+u.getCalories());
        }
        List<UserMealWithExceed>list=new ArrayList<>();
        for (UserMeal u:mealList) {
            if(TimeUtil.isBetween(u.getDateTime().toLocalTime(),startTime,endTime)) {
                if(map.get(u.getDateTime().toLocalDate())>caloriesPerDay)
                    list.add(new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(), false));
                else
                    list.add(new UserMealWithExceed(u.getDateTime(), u.getDescription(), u.getCalories(), true));
            }
        }
        return list;
    }
    public static List<UserMealWithExceed>  getFilteredWithExceededOptimal(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        Map<LocalDate, Integer> map= mealList.stream()
                .collect(Collectors.toMap(
                        p->p.getDateTime().toLocalDate(),
                        UserMeal::getCalories,
                        Integer::sum
                ));
        return mealList.stream()
                .filter(p->TimeUtil.isBetween(p.getDateTime().toLocalTime(),startTime,endTime))
                .map(p->new UserMealWithExceed(p.getDateTime(),p.getDescription(),p.getCalories(), map.get(p.getDateTime().toLocalDate()) <= caloriesPerDay))
                .collect(Collectors.toList());
    }
    public static List<UserMealWithExceed> getFilteredOptimalPlusCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        List<UserMealWithExceed>list=new ArrayList<>();
        Map<LocalDate,UserMeal>mapMealOfTime=new HashMap<>();
        Map<LocalDate,Integer>mapCountMeal=new HashMap<>();
        Map<LocalDate,Integer>mapCountCalories=new HashMap<>();
        for (UserMeal u:mealList) {
            mapCountCalories.put(u.getDateTime().toLocalDate(),mapCountCalories.getOrDefault(u.getDateTime().toLocalDate(),0)+u.getCalories());
            mapCountMeal.put(u.getDateTime().toLocalDate(),mapCountMeal.getOrDefault(u.getDateTime().toLocalDate(),0)+1);
            if(TimeUtil.isBetween(u.getDateTime().toLocalTime(),startTime,endTime)){
                mapMealOfTime.put(u.getDateTime().toLocalDate(),u);
            }
            if(mapCountMeal.get(u.getDateTime().toLocalDate())==3){
                UserMeal meal=mapMealOfTime.get(u.getDateTime().toLocalDate());
                if(mapCountCalories.get(u.getDateTime().toLocalDate())>caloriesPerDay)
                    list.add(new UserMealWithExceed(meal.getDateTime(),meal.getDescription(),meal.getCalories(),false));
                else
                    list.add(new UserMealWithExceed(meal.getDateTime(),meal.getDescription(),meal.getCalories(),true));
            }
        }
        return list;
    }
    public static List<UserMealWithExceed> getFilteredOptimalPlusStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        return mealList.stream().
                filter(f->f.getDescription().equals("Завтрак"))
                .map(m->new UserMealWithExceed(m.getDateTime(),m.getDescription(),m.getCalories(),
                        mealList.stream()
                                .filter(ff->ff.getDateTime().toLocalDate().equals(m.getDateTime().toLocalDate()))
                                .mapToInt(UserMeal::getCalories)
                                .reduce(0, Integer::sum)<=caloriesPerDay))
                .collect(Collectors.toList());
    }
    public static List<UserMealWithExceed> getFiltered(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        return mealList.stream().
                collect(Collectors.groupingBy((p)->p.getDateTime().toLocalDate()))
                .values().stream()
                .map(m->new UserMealWithExceed(m.get(m.indexOf(m.stream()
                        .filter(f->TimeUtil.isBetween(f.getDateTime().toLocalTime(),startTime,endTime))
                        .toArray()[0])).getDateTime(),m.get(m.indexOf(m.stream()
                        .filter(f->TimeUtil.isBetween(f.getDateTime().toLocalTime(),startTime,endTime))
                        .toArray()[0])).getDescription(),m.get(m.indexOf(m.stream()
                        .filter(f->TimeUtil.isBetween(f.getDateTime().toLocalTime(),startTime,endTime))
                        .toArray()[0])).getCalories(),m.get(0).getCalories()+m.get(1).getCalories()+m.get(2).getCalories()<=caloriesPerDay))
                .collect(Collectors.toList());
    }
}
