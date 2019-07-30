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
import at.spengergasse.schluesselweb.service.Reservierungservice;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Controller
@RequestMapping("/reservierung")
public class Reservierungcontroller {
        public static final String RETURN_INDEX = "reservierung/show-reservierungen";
        public static final String RETURN_INDEX_USER = "reservierung/show-reservierung-user";
        public static final String RETURN_ADD = "reservierung/add-reservierung";
        public static final String REDIRECT_INDEX = "redirect:/home/home";
        public static final String NEUE_RESERVIERUNG = "/neueReservierung";
        public static final String RETURN_ADD_USER = "reservierung/add-reservierung-user";
        public static final String RETURN_UPDATE_PAGE = "reservierung/update-reservierung";
        public static final String RETURN_ABHOLEN_PAGE = "reservierung/abholen-reservierung";
        public static final String RETURN_RETOURNIEREN_PAGE = "reservierung/retournieren-reservierung";
        public static final String RETURN_DETAILS_PAGE = "reservierung/reservierung-details";
        private final Reservierungservice reservierungservice;
        private final UserService userService;
        private final Schluesselservice schluesselservice;
        @GetMapping
        public String getReservierung(Model model)
        {
            model.addAttribute("reservierungen",reservierungservice.reservierungList());
            return RETURN_INDEX;
        }
       @GetMapping("/neueReservierung/{id}")
        public ModelAndView showAddFormUser() {
        ModelAndView model = new ModelAndView();
        User user = getAuth();
        Reservierung reservierung = new Reservierung();
        model.addObject("reservierung", reservierung);
        model.addObject("status_enum_values", ReservierungStatus.values());
        model.addObject("status_geplant",ReservierungStatus.GEPLANT);
        model.addObject("verfuegbarkeitliste",Verfuegbarkeit.values());
        model.addObject("schluessellist",schluesselservice.findAlleAusserEigenen());
        model.addObject("userName", user.getFirstName() + " " + user.getLastName());
        model.addObject("user",user);
        model.setViewName(RETURN_ADD_USER);
        return model;
    }
    @GetMapping("/reservierungen/{id}")
    public ModelAndView showReservierungTable(@PathVariable Long id) {
        ModelAndView model = new ModelAndView();
        User user = getAuth();
        model.addObject("user",user);
        model.addObject("reservierunglist",userService.findListbyEmail(user.getEmail()));
        model.setViewName(RETURN_INDEX_USER);
        return model;
    }
           @PostMapping(NEUE_RESERVIERUNG)
        public String addNewReservierung(@ModelAttribute Reservierung reservierung,Model model,
                                          @ModelAttribute Schluessel schluessel, final BindingResult result)
        {
            User user = getAuth();
            user.addReservierung(reservierung);
            reservierung.setUser(user);
            reservierung.reserviereSchluessel(schluessel);
            reservierung.getSchluessel().setVerfuegbarkeit(Verfuegbarkeit.RESERVIERT);
            if(reservierungservice.ueberpruefeUeberlappung(reservierung).isEmpty())
            {
                reservierungservice.createReservierung(reservierung);
            }
            else
            {
                String msg = "Ausgewählte Zeit überlappt sich mit einer anderen Reservierung"
                        +reservierungservice.ueberpruefeUeberlappung(reservierung).get(0);
                model.addAttribute("msg",msg);
                 return RETURN_ADD_USER;
            }
            if (result.hasErrors())
            {
                return RETURN_ADD_USER;
            }
            else
            {
                List<Reservierung> reservierungList= reservierungservice.reservierungList();
                model.addAttribute("reservierungen",reservierungList);
                return "redirect:/reservierung/reservierungen/"+user.getId();
            }

        }

    public User getAuth()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         User user = userService.findUserByEmail(auth.getName());
         return user;
    }

    @RequestMapping(value = "/update/{id}")
    public String editReservierung(@PathVariable Long id, Model model) {
            model.addAttribute("reservierung", reservierungservice.findbyid(id));
            model.addAttribute("activePage", "reservierungen_page");
            return RETURN_UPDATE_PAGE;
        }
    @RequestMapping(value = "/abholen/{id}")
    public String reservierungAbholenPage(@PathVariable Long id, Model model) {
        User user = getAuth();
            Reservierung abholung = reservierungservice.findbyid(id);
            String msg = abholung.schluesselabholen();
        reservierungservice.createReservierung(abholung);
        model.addAttribute("user",user);
        model.addAttribute("abholung",abholung);
        model.addAttribute("entnommen_zeit", abholung.getSchluessel().getVerfuegbarkeit() + " AM " + abholung.getSchluessel().getAbgeholt_datum()+ " UM "+ abholung.getSchluessel().getAbgeholt_zeit().format(DateTimeFormatter.ISO_TIME));
        model.addAttribute("msg", msg);
        return RETURN_DETAILS_PAGE;
        }
    @RequestMapping(value = "/retournieren/{id}")
    public String reservierungRetournierenPage(@PathVariable Long id, Model model) {
        User user = getAuth();
        Reservierung abholung = reservierungservice.findbyid(id);
        abholung.schluesselretournieren();
        reservierungservice.createReservierung(abholung);
        model.addAttribute("user",user);
        model.addAttribute("abholung",abholung);
        return RETURN_RETOURNIEREN_PAGE;
    }



        @PostMapping("/update")
        public String updateReservierung (long id, @Valid Reservierung reservierung, BindingResult result, Model model) {
            if (result.hasErrors()) {
                return RETURN_UPDATE_PAGE;
            }
            Reservierung toSave = reservierungservice.findbyid(id);
            toSave.setStatus_enum(reservierung.getStatus_enum());
            reservierungservice.createReservierung(toSave);
            List<Reservierung> reservierungList = reservierungservice.reservierungList();
            model.addAttribute("reservierungList", reservierungList);
            return REDIRECT_INDEX;
        }

        @GetMapping("/delete/{id}")
        public String deleteReservierung(@PathVariable("id")Long id, Model model,@RequestParam Optional<String> namePart) {
            Reservierung reservierung =reservierungservice.findbyid(id);
            reservierungservice.deleteReservierung(reservierung);
            List <Reservierung> reservierungList= reservierungservice.reservierungList();
            model.addAttribute("reservierungList",reservierungList);
            return REDIRECT_INDEX;
        }

    }
