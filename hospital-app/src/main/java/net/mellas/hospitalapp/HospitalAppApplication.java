package net.mellas.hospitalapp;

import lombok.AllArgsConstructor;
import net.mellas.hospitalapp.entities.Patient;
import net.mellas.hospitalapp.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class HospitalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalAppApplication.class, args);
    }
    @Bean
    public CommandLineRunner start(PatientRepository patientRepository){
     return  args -> {
         Patient p1= new Patient();
         p1.setNom("mellas");
         p1.setPrenom("Mohammed Hassan");
         p1.setScore(240);
         p1.setMalade(false);
         p1.setDateNaissance(new Date());

         // AllArgsConstructor
         Patient p2 =new Patient(null,"Ibrahim","Essokomi",new Date(), 345, false);

         Patient p3 = Patient.builder()
                 .nom("Oumaima")
                 .dateNaissance(new Date())
                 .score(123)
                 .build();

         patientRepository.save(p1);
         patientRepository.save(p2);
         patientRepository.save(p3);

         List<Patient> patientList = patientRepository.findAll();
         patientList.forEach(p->{
             System.out.println(p.toString());
                 }
         );
     };
    }

}
