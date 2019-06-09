package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InterfaceForDao;
import ru.javawebinar.topjava.dao.Interfacies;
import ru.javawebinar.topjava.model.Meal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.slf4j.LoggerFactory.getLogger;

public class CRUDmealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static Interfacies inter=new InterfaceForDao();
    private static String LIST_USER = "/listOfMeals.jsp";
    private static String EDIT = "/editMeal.jsp";
    private static String ADD = "/addMeal.jsp";
    public static final DateTimeFormatter FORMATTER=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect GET to CrudMeals");
        req.setCharacterEncoding("UTF-8");
        String forward="";
        String action = req.getParameter("action");
        if (action==null){
            forward = LIST_USER;
            req.setAttribute("list", inter.getListOfMeals());}
        else if (action.equalsIgnoreCase("delete")){
            int userId = Integer.parseInt(req.getParameter("userId"));
            inter.delete(userId);
            forward = LIST_USER;
            req.setAttribute("list", inter.getListOfMeals());
        } else if (action.equalsIgnoreCase("edit")){
            forward = EDIT;
            int userId = Integer.parseInt(req.getParameter("userId"));
            Meal meal = inter.getMeal(userId);
            req.setAttribute("list", Collections.singletonList(meal));
        } else {
            forward = ADD;
        }
        req.getRequestDispatcher(forward).forward(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect POST to CrudMeals");
        req.setCharacterEncoding("UTF-8");
        String i=req.getParameter("ID");
        String description=req.getParameter("nameMeal");
        int calories=Integer.parseInt(req.getParameter("calories"));
        String d=req.getParameter("date");
        System.out.println(d);
        LocalDateTime datetime = LocalDateTime.parse(d.replace("T"," "), FORMATTER);
        System.out.println(i);
        if(i!=null){
            int id=Integer.parseInt(i);
            inter.updateMeal(id,datetime,description,calories);
            req.setAttribute("list", inter.getListOfMeals());
            req.getRequestDispatcher(LIST_USER).forward(req,resp);
        }
        else {
            inter.createMeal(datetime,description,calories);
            req.setAttribute("list", inter.getListOfMeals());
            req.getRequestDispatcher(LIST_USER).forward(req,resp);
        }
    }
}
