package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.createAndPopulateRepository;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final MealRepository repository;
    private final int LoggedUser = 1;

    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";

    public MealServlet() {
        super();
        repository = createAndPopulateRepository(LoggedUser);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String forward;
        String action = request.getParameter("action");

        if ("edit".equalsIgnoreCase(action)){
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = repository.get(id, LoggedUser);
            request.setAttribute("meal", meal);
            log.debug("set meal with id={} in request", meal.getId());
            forward = INSERT_OR_EDIT;
        } else {
            if ("delete".equalsIgnoreCase(action)){
                int id = Integer.parseInt(request.getParameter("id"));
                if (repository.delete(id, LoggedUser)) log.info("deleted record id={}", id);
                else log.error("unsuccesful deleting record id={}", id);
            }
            request.setAttribute("meal", new Meal(LocalDateTime.now(), "", 0));
            forward = LIST_MEALS;
            log.debug("get list meals with exceed");
            List<MealWithExceed> mealList = MealsUtil.getAllWithExceeded(repository.getAll(LoggedUser), 2000);
            request.setAttribute("meals", mealList);
        }
        log.debug("redirect to {}", forward);
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("try saving new record or edit");

        String id = request.getParameter("id");
        String datetime = request.getParameter("datetime");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");

        Meal meal = new Meal(LocalDateTime.parse(datetime), description, Integer.parseInt(calories));
        if (!id.isEmpty()) {
            meal.setId(Integer.parseInt(id));
        }

        Meal save = repository.save(meal, LoggedUser);
        log.debug("succesful save meal with id={}, datetime={}, description={}, calories={}", save.getId(), save.getDateTime(), save.getDescription(), save.getCalories());
        response.sendRedirect("meals");
    }
}
