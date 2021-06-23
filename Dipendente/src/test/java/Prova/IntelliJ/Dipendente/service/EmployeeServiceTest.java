package Prova.IntelliJ.Dipendente.service;

import Prova.IntelliJ.Dipendente.entity.Employee;
import Prova.IntelliJ.Dipendente.entity.Qualification;
import Prova.IntelliJ.Dipendente.entity.Sex;
import Prova.IntelliJ.Dipendente.exception.CFException;
import Prova.IntelliJ.Dipendente.exception.CfNotFoundException;
import Prova.IntelliJ.Dipendente.exception.MyException;
import Prova.IntelliJ.Dipendente.exception.OurException;
import Prova.IntelliJ.Dipendente.repository.EmployeeRepository;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureDataMongo
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Log
class EmployeeServiceTest {
    Employee employee;
    Employee fetchedEmployee;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setName("Gino");
        employee.setSurname("Visciano");
        employee.setAge("24");
        employee.setCF("nte");
        employee.setQualification(Qualification.DIPLOMA);
        employee.setRole("dipendete");
        employee.setSex(Sex.MALE);

    }

    @AfterEach
    public void tearDown() {
        employeeRepository.deleteAll();
        employee = null;
    }

    @Test
    void insertEmployee() {
        employee = employeeRepository.save(employee);
        Employee fetchedEmployee = employeeRepository.findById(employee.getId()).get();
        assertEquals(employee.getId(), fetchedEmployee.getId());

    }

    @Test
    void checkInsert() {
        assertNotNull(employeeService.checkInsert(employee));
        // assertEquals("Inserito con successo", employeeService.checkInsert(employee));
    }
    @Test
    void checkInsertIsNull(){
        Employee employee = null;
        assertThrows(MyException.class,()->{employeeService.insertEmployee(employee);});
    }
    @Test
    void checkInsertIsVoid(){
        Employee employee = new Employee();
        employee.setName("solo un campo selezionato");
        assertThrows(MyException.class,()->{employeeService.insertEmployee(employee);});
    }

    @Test
    void checkEmployee() {
        employee = employeeRepository.save(employee);
        assertTrue(employeeService.checkEmployee(employee));
    }
    @Test
    void checkEmployeeNull(){
        employee = employeeRepository.save(employee);
        employee.setAge(null);
        assertFalse(employeeService.checkEmployee(employee));
    }
    @Test
    void checkEmployeeEmpty(){
        employee = new Employee();
        employee.setRole("Tester");
        assertFalse(employeeService.checkEmployee(employee));
    }

    @Test
    void updateEmployee() throws CFException {
        employee = employeeRepository.save(employee);
        fetchedEmployee = employeeRepository.findBycF(employee.getCF());
        fetchedEmployee.setName("Pippo");
        fetchedEmployee.setSurname("Baudo");
        fetchedEmployee.setAge("89");
        fetchedEmployee.setRole("Impiegato");
        fetchedEmployee.setQualification(Qualification.LAUREA);
        fetchedEmployee.setSex(Sex.MALE);
        assertDoesNotThrow(() -> {
            employeeService.updateEmployee(fetchedEmployee.getCF(), fetchedEmployee);
        });
        fetchedEmployee = employeeRepository.findBycF(employee.getCF());
        assertEquals(employee.getId(), fetchedEmployee.getId());
    }
    @Test
    void updateEmployeeCFNonTrovato(){
        employee = employeeRepository.save(employee);
        assertThrows(CfNotFoundException.class,()->{employeeService.updateEmployee(employee.getAge(), employee);});
    }
    @Test
    void updateEmployeeEmptyField(){
        employee = employeeRepository.save(employee);
        employee.setName(null);
        assertThrows(CFException.class,()->{employeeService.updateEmployee(employee.getCF(),employee);});
    }

    @Test
    void checkUpdate() throws CFException {
        this.updateEmployee();
       assertEquals("Dipendente aggiornato", employeeService.checkUpdate(fetchedEmployee.getCF(),fetchedEmployee));
    }

    @Test
    void deleteEmployee() throws CfNotFoundException {
        employee = employeeRepository.save(employee);
        employeeService.deleteEmployee(employee.getCF());
      //  assertFalse(employeeRepository.findBycF(employee.getCF()) != null, "Il dipendente non e stato eliminato");
        assertTrue(employeeRepository.findBycF(employee.getCF()) == null, "Il dipendente e stato eliminato");
    }

    @Test
    void checkDelete() throws CFException {
        employee = employeeRepository.save(employee);
        assertEquals("Dipendente eliminato", employeeService.checkDelete(employee.getCF()));
//        if (employeeRepository.existsBycF(employee.getCF())) {
//            assertDoesNotThrow(() -> {
//                employeeService.findOne(employee.getCF());
//            }, "Il dipendente è stato eliminato");
//        } else {
//            assertThrows(CFException.class, () -> {
//                employeeService.findOne(employee.getCF());
//            }, "lancia l'errore");
//        }
    }
    @Test
    void checkDeleteNotCf(){
        employee = employeeRepository.save(employee);
        assertThrows(CfNotFoundException.class,()->{employeeService.updateEmployee(employee.getAge(), employee);});
    }
    @Test
    void findOne() {
        employee = employeeRepository.save(employee);
        Employee fetchedEmployee = employeeRepository.findBycF(employee.getCF());
        assertEquals(fetchedEmployee.toString(), employee.toString());
    }
    @Test
    void findOneNotCF(){
        employee = employeeRepository.save(employee);
        assertThrows(CfNotFoundException.class,()->{employeeService.updateEmployee(employee.getAge(), employee);});
    }

    @Test
    void checkFindOne() {
        employee = employeeRepository.save(employee);
       assertNotNull(employeeService.checkFindOne(employee.getCF()));
//        if (employeeRepository.existsBycF(employee.getCF())) {
//            assertDoesNotThrow(() -> {
//                employeeService.findOne(employee.getCF());
//            });
//        }
//      //  Employee fetchedEmployee = employeeService.findOne(employee.getCF());
//        else {
//            assertThrows(CFException.class, () -> {
//                employeeService.findOne(employee.getCF());
//            });
//        }
    }

    @Test
    void findAll() {
        employee = employeeRepository.save(employee);
        List fetchedEmployee = employeeRepository.findAll();
        assertTrue(fetchedEmployee.size() > 0, "mi ha salvato la lista");
       // assertFalse(fetchedEmployee.isEmpty(), "Non ha trovato nulla");
    }

    @Test
    void getAllBySexAfterEighteen() {
        LocalTime now = LocalTime.now();
        LocalTime limit = LocalTime.now().withHour(18).withMinute(15).withSecond(0);
        if (now.isAfter(limit)) {
            assertDoesNotThrow(() -> {
                employeeService.getAllBySexAfterEighteen(Sex.MALE);
            });
        } else {
            assertThrows(OurException.class, () -> {
                employeeService.getAllBySexAfterEighteen(Sex.MALE);
            });
        }
    }

  
}