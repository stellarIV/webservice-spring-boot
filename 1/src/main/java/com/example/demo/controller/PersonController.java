package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Person;

@RestController
@RequestMapping("/persons")
public class PersonController {

    // simple in-memory store for demo
    private final Map<Long, Person> storage = new ConcurrentHashMap<>();

    public PersonController() {
        storage.put(1L, new Person(1L, "Arsema", "Smith", "Arsu@example.com", "IT", 60000.0));
        storage.put(2L, new Person(2L, "Bemnet", "Johnson", "Bemnet@example.com", "HR", 55000.0));
        storage.put(3L, new Person(2L, "Biruk", "Johnson", "Burak@example.com", "Finance", 70000.0));
        storage.put(4L, new Person(2L, "Gebeyaw", "Johnson", "Gman@example.com", "IT", 65000.0));
        storage.put(5L, new Person(2L, "Rodas", "Johnson", "Rod@example.com", "Marketing", 52000.0));
        storage.put(6L, new Person(2L, "Tsion", "Johnson", "Tsi@example.com", "Sales", 58000.0));
        storage.put(7L, new Person(2L, "Yonas", "Johnson", "Yonani@example.com" , "IT", 72000.0));
    }

    // GET /persons -> collection with links
    @GetMapping
    public CollectionModel<EntityModel<Person>> all() {
        List<EntityModel<Person>> list = new ArrayList<>();
        for (Person p : storage.values()) {
            EntityModel<Person> model = EntityModel.of(p,
                    linkTo(methodOn(PersonController.class).one(p.getId())).withSelfRel(),
                    linkTo(methodOn(PersonController.class).all()).withRel("persons"));
            list.add(model);
        }
        return CollectionModel.of(list,
                linkTo(methodOn(PersonController.class).all()).withSelfRel());
    }

    // GET /persons/{id} -> single resource with self + collection link
    @GetMapping("/{id}")
    public EntityModel<Person> one(@PathVariable Long id) {
        Person p = Optional.ofNullable(storage.get(id))
                .orElseThrow(() -> new NoSuchElementException("Person not found: " + id));
        return EntityModel.of(p,
                linkTo(methodOn(PersonController.class).one(id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).all()).withRel("persons"));
    }

    // POST /persons -> create and return 201 with Location header (HATEOAS-friendly)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Person person) {
        long id = storage.keySet().stream().mapToLong(Long::longValue).max().orElse(0L) + 1;
        person.setId(id);
        storage.put(id, person);

        EntityModel<Person> resource = EntityModel.of(person,
                linkTo(methodOn(PersonController.class).one(id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).all()).withRel("persons"));

        return ResponseEntity
                .created(resource.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(resource);
    }

    // PUT /persons/{id} -> update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Person incoming) {
        Person existing = storage.get(id);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setFirstName(incoming.getFirstName());
        existing.setLastName(incoming.getLastName());
        existing.setEmail(incoming.getEmail());
        return ResponseEntity.ok(EntityModel.of(existing,
                linkTo(methodOn(PersonController.class).one(id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).all()).withRel("persons")));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Person removed = storage.remove(id);
        if (removed == null) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
