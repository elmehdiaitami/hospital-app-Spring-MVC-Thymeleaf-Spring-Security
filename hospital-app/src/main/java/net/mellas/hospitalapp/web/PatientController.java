package net.mellas.hospitalapp.web;

import jakarta.validation.Valid;
import net.mellas.hospitalapp.entities.Patient;
import net.mellas.hospitalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "keyword", defaultValue = "")String keyword
                      ){
       Page<Patient> pagePatients = patientRepository.findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(keyword,keyword,PageRequest.of(page, size));
       model.addAttribute("listPatients", pagePatients.getContent());
       model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
       model.addAttribute("currentPage", page);
       model.addAttribute("keyword",keyword);
       return "patients";
    }
    @GetMapping("/deletePatient")
    public String delete(@RequestParam(name = "id") Long id, String keyword,int page){
       patientRepository.deleteById(id);
       return  "redirect:/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/patients")
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }


   @GetMapping("/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }
    @PostMapping(path = "/save")
    public String Save(Model model, @Valid Patient patient, BindingResult bindingResult,
                      @RequestParam(defaultValue = "0") int page ,
                       @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/editPatient")
    public String editPatient(Model model, Long id,String keyword, int page){
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("Patient not found");
        model.addAttribute("patient", patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editPatient";

    }

}

