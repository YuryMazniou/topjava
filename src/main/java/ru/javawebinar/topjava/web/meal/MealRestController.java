package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;


@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);
    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public void savePart(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        String action = request.getParameter("action");
        final Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                service.get(getId(request),SecurityUtil.authUserId());
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
    }
    public void deletePart(HttpServletRequest request, HttpServletResponse response)throws IOException {
        int id = getId(request);
        log.info("Delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
        response.sendRedirect("meals");
    }
    /*public Meal getPart(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        return service.get(id,userId);
    }*/

    public void getListOfPart(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        log.info("getAll");
        request.setAttribute("meals",
                MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
    public void getListOfPartsWithFilter(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        log.info("getAll and Filter");
        String line;
        line=request.getParameter("afterDate");
        LocalDate startDate=(line==null?LocalDate.MIN:LocalDate.parse(line));
        LocalDate endDate=LocalDate.parse(request.getParameter("beforeDate"));
        LocalTime startTime=LocalTime.parse(request.getParameter("afterTime"));
        LocalTime endTime=LocalTime.parse(request.getParameter("beforeTime"));
        request.setAttribute("meals",MealsUtil.getFilteredWithExcess(service.getAll(SecurityUtil.authUserId()),
                MealsUtil.DEFAULT_CALORIES_PER_DAY,startTime,endTime,startDate,endDate));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    public void doPostController(HttpServletRequest request, HttpServletResponse response)throws IOException{
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String idUser = request.getParameter("idUser");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        meal.setUserId(idUser.isEmpty()? null : Integer.valueOf(idUser));
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        service.save(meal,SecurityUtil.authUserId());
        response.sendRedirect("meals");
    }
    public void doGetController(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                deletePart(request,response);
                break;
            case "create":
            case "update":
                savePart(request,response);
                break;
            case "filter":
                getListOfPartsWithFilter(request,response);
                break;
            case "all":
            default:
                getListOfPart(request,response);
                break;
        }
    }
}