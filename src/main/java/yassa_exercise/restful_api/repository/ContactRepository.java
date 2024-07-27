package yassa_exercise.restful_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import yassa_exercise.restful_api.entity.Contact;
import yassa_exercise.restful_api.entity.User;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {
    Optional<Contact> findFirstByUserAndId(User user, String id);
}
