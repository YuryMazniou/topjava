package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InterfaceForDao;
import ru.javawebinar.topjava.dao.Interfacies;
import ru.javawebinar.topjava.dao.StorageMeals;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class CRUDmealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static Interfacies inter=new InterfaceForDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to CrudMeals");
        req.setCharacterEncoding("UTF-8");
        List<Meal> meals = inter.getListOfMeals();
        req.setAttribute("list",meals);
        req.getRequestDispatcher("/listOfMeals.jsp").forward(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
