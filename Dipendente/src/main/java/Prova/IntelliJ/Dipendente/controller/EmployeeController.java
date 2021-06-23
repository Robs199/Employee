package Prova.IntelliJ.Dipendente.controller;


import Prova.IntelliJ.Dipendente.entity.Employee;
import Prova.IntelliJ.Dipendente.entity.Sex;
import Prova.IntelliJ.Dipendente.exception.CFException;
import Prova.IntelliJ.Dipendente.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping("/addEmployee")
    public String addEmployee(Employee employee){
        return employeeService.checkInsert(employee);
    }

    @PatchMapping("/updateEmployee")
    public String updateEmployee(@RequestParam String codiceFiscale, Employee employee){
        return employeeService.checkUpdate(codiceFiscale, employee);
    }
    @DeleteMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam String codiceFiscale){
        return employeeService.checkDelete(codiceFiscale);
    }
    @GetMapping("/findOne")
    public ResponseEntity<Employee> findOne(@RequestParam String codiceFiscale)  {
        return employeeService.checkFindOne(codiceFiscale);
    }
    @GetMapping("/findAll")
    public List<Employee> findAll(){
        return employeeService.findAll();
    }
    @GetMapping("/findallbysex")
        public ResponseEntity<List<Employee>> findAllBySex(@RequestParam Sex sex){
        //mi ritorna la verifica con i log
        return employeeService.checkerFindAllBySex(sex);
    }
}
