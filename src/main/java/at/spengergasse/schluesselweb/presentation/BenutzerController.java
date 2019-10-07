package at.spengergasse.schluesselweb.presentation;

import javax.validation.Valid;

import at.spengergasse.schluesselweb.domain.Schluessel;
import at.spengergasse.schluesselweb.domain.User;
import at.spengergasse.schluesselweb.domain.Verfuegbarkeit;
import at.spengergasse.schluesselweb.service.Schluesselservice;
import at.spengergasse.schluesselweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class BenutzerController
{
    private final static String RETURN_LOGINPAGE = "user/login";
    private final static String RETURN_UPDATE_PAGE = "admin/update-user";
    public static final String REDIRECT_INDEX = "redirect:/admin/show-user";
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private Schluesselservice schluesselservice;



    @RequestMapping(value= {"/", "/login"}, method=RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        model.setViewName(RETURN_LOGINPAGE);
        return model;
    }
    @RequestMapping(value= {"/login"}, method=RequestMethod.POST)
    public ModelAndView loginpost() {
        ModelAndView model = new ModelAndView();
        model.setViewName(RETURN_LOGINPAGE);
        return model;
    }

    @RequestMapping(value= {"/signup"}, method=RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        model.setViewName("user/signup");

        return model;
    }

    @RequestMapping(value= {"/signup"}, method=RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());

        if(userExists != null) {
            bindingResult.rejectValue("email", "error.user", "Diese Email Adresse ist bereits besetzt");
        }
        if(bindingResult.hasErrors()) {
            model.setViewName("user/signup");
        } else {
            userService.saveUser(user);
            model.addObject("msg", "User erfolgreich registiert!");
            model.addObject("user", new User());
            model.setViewName("user/signup");
        }

        return model;
    }
    @RequestMapping(value= {"admin/home"}, method=RequestMethod.GET)
    public ModelAndView adminhome() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        //model.addObject("userName", user.getFirstName() + " " + user.getLastName());
        //model.addObject("user",user);
        model.setViewName("/admin/home");
        return model;
    }
        //localhost:8080/admin/add-user
    @RequestMapping(value= {"/admin/add-user"}, method=RequestMethod.GET)
    public ModelAndView addUser() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        Schluessel schluessel = Schluessel.builder().zimmerbezeichnung("Eigener Schlüsselbund").verfuegbarkeit(Verfuegbarkeit.VERFUEGBAR).build();
        user.setPrivaterSchluessel(schluessel);
        model.addObject("user", user);
        model.addObject("schluessel_nr",schluessel.getId());
        model.setViewName("/admin/add-user");
        return model;
    }
    @RequestMapping(value= {"/admin/add-user"}, method=RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, @ModelAttribute Schluessel schluessel, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if(userExists != null) {
            bindingResult.rejectValue("email", "error.user", "Diese Email Adresse ist bereits besetzt");
        }
        if(bindingResult.hasErrors()) {
            model.setViewName("/admin/add-user");
        } else {
            userService.saveUser(user);
            model.setViewName(REDIRECT_INDEX);
        }

        return model;
    }

    @RequestMapping(value= {"admin/show-user"}, method=RequestMethod.GET)
    public ModelAndView showUser() {
        ModelAndView model = new ModelAndView();
        List<User> userList = userService.userList();
        model.addObject("userList",userList);
        model.setViewName("admin/show-user");
        return model;
    }

    @GetMapping(value = "/admin/update-user/{id}")
    public String editKunde(@PathVariable Long id, Model model) {
        model.addAttribute("user",userService.findbyid(id));
        return RETURN_UPDATE_PAGE;
    }

    @PostMapping(value = "/admin/update-user")
            public String updateUser(long id,@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("msg",userService.errorMsg());
            return RETURN_UPDATE_PAGE;
        }
        else{
            User toSave = userService.findbyid(id);
            toSave.setFirstName(user.getFirstName());
            toSave.setLastName(user.getLastName());
            toSave.setBirthdate(user.getBirthdate());
            toSave.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.saveUser(toSave);
            List <User> userList= userService.userList();
            model.addAttribute("userList",userList);
            return REDIRECT_INDEX;
        }
    }



    @RequestMapping(value = "/admin/update/{id}")
    public String editLieferant(@PathVariable Long id, Model model) {
        model.addAttribute("user", this.userService.findbyid(id));
        model.addAttribute("activePage", "user");
        return RETURN_UPDATE_PAGE;
    }





    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id")Long id, Model model,@RequestParam Optional<String> namePart) {
        User user = userService.findbyid(id);
        userService.deleteUser(user);
        List <User> userList= userService.userList();
        model.addAttribute("userList",userList);
        return REDIRECT_INDEX;
    }


    @RequestMapping(value= {"/home/home"}, method=RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addObject("schluessellist",schluesselservice.findAlleAusserEigenen());
        model.addObject("userName", user.getFirstName() + " " + user.getLastName());
        model.addObject("user",user);
        if(user.getPrivaterSchluessel()!=null) {
            model.addObject("status", user.getPrivaterSchluessel().statusString());
        }
        model.setViewName("home/home");
        return model;
    }

    @RequestMapping(value= {"/access_denied"}, method= RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/access_denied");
        return model;
    }



}
