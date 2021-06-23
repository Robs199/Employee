package Prova.IntelliJ.Dipendente.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
//Builde e un patten che mi permette di costruire oggetti senza la keyword
//ex: Persona p =Persona.builder().nome("nome").build;
public class Employee {
    @Id
    private String id;
    private String name;
    private String surname;
    private String age;
    @Indexed(unique = true)
    private String cF;
    private String role;
    private Qualification qualification;
    private Sex sex;
    public String getFullName(){
      //  StringBuilder sb = new StringBuilder();
     //   sb.append(name).append(" ").append(surname);
       // return sb.toString();
        return new StringBuilder().append(name).append(" ").append(surname).toString();
    }

}
