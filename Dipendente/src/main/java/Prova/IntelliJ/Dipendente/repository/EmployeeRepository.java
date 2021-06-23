package Prova.IntelliJ.Dipendente.repository;

import Prova.IntelliJ.Dipendente.entity.Employee;
import Prova.IntelliJ.Dipendente.entity.Sex;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee,String> {
    public List<Employee> findAllBySex(Sex sex);
    public boolean existsBycF(String cF);
    public Employee findBycF(String cF);
    public Employee deleteBycF(String cF);
}
