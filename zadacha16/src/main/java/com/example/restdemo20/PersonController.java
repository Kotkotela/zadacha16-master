package com.example.restdemo20;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return (List<Person>) personRepository.findAll();
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable(value = "id") int personId) {
        Optional<Person> optionalPerson = personRepository.findById(personId);
        return optionalPerson.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id") int personId, @RequestBody Person updatedPerson) {
        Optional<Person> optionalPerson = personRepository.findById(personId);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Person existingPerson = optionalPerson.get();
        existingPerson.setFirstname(updatedPerson.getFirstname());
        existingPerson.setSurname(updatedPerson.getSurname());
        existingPerson.setLastname(updatedPerson.getLastname());
        existingPerson.setBirthday(updatedPerson.getBirthday());
        existingPerson.setMessages(updatedPerson.getMessages());
        return ResponseEntity.ok(personRepository.save(existingPerson));
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable(value = "id") int personId) {
        Optional<Person> optionalPerson = personRepository.findById(personId);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        personRepository.delete(optionalPerson.get());
        return ResponseEntity.noContent().build();
    }
}