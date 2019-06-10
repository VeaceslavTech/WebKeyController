package at.spengergasse.schluesselweb.presentation;

import at.spengergasse.schluesselweb.domain.*;
import at.spengergasse.schluesselweb.service.Schluesselservice;
import at.spengergasse.schluesselweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor

@Controller
@RequestMapping("/schluessel")
public class SchluesselController{
    public static final String RETURN_INDEX = "schluessel/show-schluessel";
    public static final String RETURN_ADD = "schluessel/add-schluessel";
    public static final String REDIRECT_INDEX = "redirect:/schluessel";
    public static final String NEUER_SCHLUESSEL = "/neuerSchluessel";
    public static final String RETURN_DETAILS_PAGE = "schluessel/details-schluessel";
    public static final String RETURN_RETOURNIEREN_PAGE = "schluessel/retournieren";
    public static final String RETURN_ABHOLEN_PAGE = "schluessel/abholen";
    private final Schluesselservice schluesselservice;
    private final UserService userService;
    @GetMapping
    public String getReservierung(Model model)
    {
        model.addAttribute("schluessellist",schluesselservice.schluessellist());// muss mit th:each in .html übereinstimmen
        return RETURN_INDEX;
    }
    @GetMapping(NEUER_SCHLUESSEL)
    public ModelAndView showAddFormUser() {
        Schluessel schluessel = new Schluessel();
        ModelAndView model = new ModelAndView();
        model.addObject("verfuegbarkeitliste",Verfuegbarkeit.values());
        model.addObject("schluessel",schluessel);
        model.setViewName(RETURN_ADD);
        return model;
    }
    @PostMapping(NEUER_SCHLUESSEL)
    public String addNewSchluessel(@ModelAttribute Schluessel schluessel,
                                   Model model, final BindingResult result, @RequestParam Optional<String> namePart)
    {
      schluesselservice.createSchluessel(schluessel);
        if (result.hasErrors())
        {
            return RETURN_ADD;
        }
        else
        {
            List<Schluessel> schluessels= schluesselservice.schluessellist();
            model.addAttribute("schluessellist",schluessels);
            return REDIRECT_INDEX;
        }
    }
    @RequestMapping(value = "/abholen/{id}")
    public String reservierungAbholenPage(@PathVariable Long id, Model model) {
        User user = getAuth();
        Schluessel abholung = user.getPrivaterSchluessel();
        String msg = abholung.schluesselabholenprivat();
        schluesselservice.createSchluessel(abholung);
        model.addAttribute("user",user);
        model.addAttribute("abholung",abholung);
        model.addAttribute("msg", msg);
        model.addAttribute("entnohmen",abholung.statusString());
        return RETURN_ABHOLEN_PAGE;
    }
    @RequestMapping(value = "/retournieren/{id}")
    public String reservierungRetourinerenPage(@PathVariable Long id, Model model) {
        User user = getAuth();
        Schluessel abholung = user.getPrivaterSchluessel();
        String msg = abholung.schluesselretournierenprivat();
        schluesselservice.createSchluessel(abholung);
        model.addAttribute("user",user);
        model.addAttribute("abholung",abholung);
        model.addAttribute("msg", msg);
        model.addAttribute("verfuegbar",abholung.statusString());
        return RETURN_RETOURNIEREN_PAGE;
    }

    /*@RequestMapping(value = "/retournieren/{id}")
    public String reservierungRetournierenPage(@PathVariable Long id, Model model) {
        User user = getAuth();
        Reservierung abholung = reservierungservice.findbyid(id);
        abholung.schluesselretournieren();
        reservierungservice.createReservierung(abholung);
        model.addAttribute("user",user);
        model.addAttribute("abholung",abholung);
        return RETURN_RETOURNIEREN_PAGE;
    }
    */
    public User getAuth()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        return user;
    }
}
