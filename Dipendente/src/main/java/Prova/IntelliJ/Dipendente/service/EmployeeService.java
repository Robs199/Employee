package Prova.IntelliJ.Dipendente.service;

import Prova.IntelliJ.Dipendente.entity.Employee;
import Prova.IntelliJ.Dipendente.entity.Sex;
import Prova.IntelliJ.Dipendente.exception.CFException;
import Prova.IntelliJ.Dipendente.exception.CfNotFoundException;
import Prova.IntelliJ.Dipendente.exception.MyException;
import Prova.IntelliJ.Dipendente.exception.OurException;
import Prova.IntelliJ.Dipendente.repository.EmployeeRepository;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.List;

@Service
@Log
public class EmployeeService {
    private final EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void insertEmployee(Employee employee) throws MyException {
        //controlla se i campi sono vuoti
        if (employee == null || employee.getName() == null || employee.getSurname() == null || employee.getAge() == null || employee.getCF() == null || employee.getQualification() == null ||
                employee.getRole() == null || employee.getSex() == null) {
            //se sono vuoti mi lancia l eccezione
            throw new MyException("Inserisci tutti i campi");
        } else if (employee.getName().isEmpty() || employee.getSurname().isEmpty() || employee.getCF().isEmpty() ||
                employee.getAge().isEmpty() || employee.getRole().isEmpty()) {
            throw new MyException("Uno o piu campi vuoti");
        } else if (employeeRepository.existsBycF(employee.getCF())) {
            throw new MyException("Codice fiscale gia presente nel database");
        } else {
            employeeRepository.save(employee);
        }
    }

    public String checkInsert(Employee employee) {
        try {
            // nel caso non mi lancia l errore allora mi lancia il messaggio di dipendente aggiunto
            this.insertEmployee(employee);
            return "Inserito con successo";
        } catch (MyException e) {
            //quindi se il metodo mi da l errore questo lo eseguo qui
            String errorMessage = new StringBuilder().append("Inserimento fallito: ").append(e.getMessage()).toString();
            log.warning(errorMessage);
            //mi ritorna il messaggio dell errore
            return errorMessage;
        }
    }

    public boolean checkEmployee(Employee employee) {
        if (employee.getName() == null || employee.getName().isEmpty()) {
            return false;
        }
        if (employee.getSurname() == null || employee.getSurname().isEmpty()) {
            return false;
        }
        if (employee.getAge() == null || employee.getAge().isEmpty()) {
            return false;
        }
        if (employee.getQualification() == null || employee.getQualification().equals("")) {
            return false;
        }
        if (employee.getRole() == null || employee.getRole().isEmpty()) {
            return false;
        }
        if (employee.getSex() == null || employee.getSex().equals("MALE") || employee.getSex().equals("FEMALE")) {
            return false;
        }
            return true;

    }

    public void updateEmployee(String codiceFiscale, Employee employee) throws CFException, CfNotFoundException {
        if (checkEmployee(employee)) {
            if (employeeRepository.existsBycF(codiceFiscale)) {
                Employee found = employeeRepository.findBycF(codiceFiscale);
                found.setName(employee.getName());
                found.setSurname(employee.getSurname());
                found.setAge(employee.getAge());
                found.setQualification(employee.getQualification());
                found.setRole(employee.getRole());
                found.setSex(employee.getSex());
                employeeRepository.save(found);
            } else {
                throw new CfNotFoundException("Codice fiscale non trovato");
            }
        } else {
            throw new CFException("Uno o piu campi vuoti");
        }
    }

    public String checkUpdate(String codiceFiscale, Employee employee) {
        try {
            this.updateEmployee(codiceFiscale, employee);
            return "Dipendente aggiornato";
        } catch (CFException e) {
            String errorMessage = new StringBuilder().append("Modifica fallita: ").append(e.getMessage()).toString();
            log.warning(e.getMessage());
            return errorMessage;
        }catch (CfNotFoundException e){
            String errorMEssage =new StringBuilder().append("Mofùdifica fallita: ").append(e.getMessage()).toString();
            log.warning(e.getMessage());
            return errorMEssage;
        }
    }

    public void deleteEmployee(String cF) throws CfNotFoundException {
        if (employeeRepository.existsBycF(cF)) {
            Employee employee = employeeRepository.findBycF(cF);
            employeeRepository.delete(employee);
        } else {
            throw new CfNotFoundException("Codice fiscale non trovato nel database");
        }
    }

    public String checkDelete(String codiceFiscale) {
        try {
            this.deleteEmployee(codiceFiscale);
            return "Dipendente eliminato";
        } catch (CfNotFoundException e) {
            String errorMessage = new StringBuilder().append("Eliminazione fallita: ").append(e.getMessage()).toString();
            log.warning(e.getMessage());
            return errorMessage;
        }
    }

    public Employee findOne(String cF) throws CfNotFoundException {
        if (employeeRepository.existsBycF(cF)) {
            return employeeRepository.findBycF(cF);
        } else {
            throw new CfNotFoundException("Codice fiscale non trovato nel database");
        }
    }

    public ResponseEntity<Employee> checkFindOne(String cF) {
        try {
            return ResponseEntity.ok(findOne(cF));
        } catch (CfNotFoundException e) {
            String errorMessage = new StringBuilder().append("Stampa fallita: ").append(e.getMessage()).toString();
            log.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> getAllBySexAfterEighteen(Sex sex) throws OurException {
        LocalTime now = LocalTime.now();
        LocalTime limit = now.withHour(18).withMinute(00).withSecond(00);

        if (now.isAfter(limit)) {
            return employeeRepository.findAllBySex(sex);
        } else {
            throw new OurException("Non sono ancora le 18");
        }
    }

    public ResponseEntity<List<Employee>> checkerFindAllBySex(Sex sex) {
        // ResponseEntity rappresenta la risposta dell http, posso controllare tutto quello che entra da qui
        //Incapsula e gestisce lo stato di controllo
        try {
            //mi ritorna il metodo di sopra che mi verifica l orario e il limite dell oratio
            return ResponseEntity.ok(this.getAllBySexAfterEighteen(sex));

        } catch (OurException e) {
            log.warning(new StringBuilder().append("Mi dispiace, sono ancora le: ").append(LocalTime.now().minusSeconds(LocalTime.now().getSecond()).minusNanos(LocalTime.now().getNano()).getNano())
                    .append("Attenda fino alle 18:00").toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

