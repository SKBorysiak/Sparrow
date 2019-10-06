package controllers.servlets;

import dao.UserDAO;
import model.User;
import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "registerServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    private final String PASSWORD = "password";
    private final String LOGIN = "login";
    private final String REPEATED_PASSWORD = "repeatedPassword";
    private final String USER_NAME = "userName";
    private final String EMAIL = "email";
    private final String LAST_NAME = "lastName";
    UserService service;

    @Override
    public void init() throws ServletException {
        service = new UserService(new UserDAO());
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN);
        String password = req.getParameter(PASSWORD);
        String repeatedPassword = req.getParameter(REPEATED_PASSWORD);
        String userName = req.getParameter(USER_NAME);
        String email = req.getParameter(EMAIL);
        String lastName = req.getParameter(LAST_NAME);

        if (password.equals(repeatedPassword)) {
            req.setAttribute("hasError", "true");
            req.setAttribute("error", "Passwords are not equal.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setLastName(lastName);
        user.setName(userName);
        user.setLogin(login);

        String registerStatus = service.registerUser(user);
        if (!registerStatus.equals(UserService.SUCCESS)) {
            req.setAttribute("hasError", "true");
            req.setAttribute("error", registerStatus);
            req.getRequestDispatcher("/register.jsp").forward(req,resp);
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
