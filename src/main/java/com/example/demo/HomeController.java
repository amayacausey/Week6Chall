package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user",new User());
        return "register";
    }
    @PostMapping("/processregister")
    public String processRegisterationPage(
            @Valid @ModelAttribute("user") User user, BindingResult result, Model model)
    {
        if(result.hasErrors()){
            user.clearPassword();
            model.addAttribute("user",user);
            return "register";
        }
        else {
            model.addAttribute("user",user);
            model.addAttribute("message","New user account created");

            user.setEnabled(true);
            userRepository.save(user);
            Role role = new Role(user.getUsername(),"ROLE_USER");
            roleRepository.save(role);
        }
        return "index";
    }


    @RequestMapping("/secure")
    public String secure(Principal principal, Model model)
    {
        String username = principal.getName();
        model.addAttribute("user",userRepository.findByUsername(username));
        return "secure";
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/logout")
    public String logout(){
      return "redirect:/login?logout=true";
    }
    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }

    @RequestMapping ("/addEmployee")
    public String addEmployee(Model model){
        model.addAttribute("employee",new Employee());
        model.addAttribute("department",new Department());
        return "addEmployee";
    }
    @PostMapping ("/processEmployee")
    public String processDepartment (@Valid Employee employee,BindingResult result) {
  if (result.hasErrors()){
        return "addEmployee";
    }
        employeeRepository.save(employee);
        return "redirect:/";
}

    @RequestMapping("/addDepartment")
    public String addDepartment(Model model){
        model.addAttribute("department",new Department());
        model.addAttribute("employee",new Employee());
        return "addDepartment";
    }

    @PostMapping("/processDepartment")
    public String processDepartment (@Valid Department department,BindingResult result) {
        if (result.hasErrors()){
            return "addDepartment";
        }
        departmentRepository.save(department);
        return "redirect:/";
    }
    @RequestMapping("/listEmployee")
    public String listEmployee (Model model){
        model.addAttribute("employee",employeeRepository.findAll());
        model.addAttribute("department",departmentRepository.findAll());

        return "listEmployee";
    }

    @RequestMapping("/listDepartment")
    public String listDepartment (Model model){
        model.addAttribute("department", departmentRepository.findAll());
        return "listDepartment";
    }
    @RequestMapping("/detailsEmployee/{id}")
    public String detailsEmployee(@PathVariable("id") long id, Model model){
        model.addAttribute("employee",employeeRepository.findById(id).get());
        return "employeeDetails";
    }
    @RequestMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable("id") long id, Model model) {
        model.addAttribute("employee", employeeRepository.findById(id).get());
        return "addEmployee";
    }
}
