package yassa_exercise.restful_api.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import yassa_exercise.restful_api.entity.Contact;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.ContactResponse;
import yassa_exercise.restful_api.models.CreateContactRequest;
import yassa_exercise.restful_api.models.SearchContactRequest;
import yassa_exercise.restful_api.models.UpdateContactRequest;
import yassa_exercise.restful_api.repository.ContactRepository;
import yassa_exercise.restful_api.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidatorService validatorService;

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request) {
        validatorService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPhone(request.getPhoneNumber());
        contact.setEmail(request.getEmail());
        contact.setUser(user);
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse get(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));

        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequest request) {
        validatorService.validate(request);
        Contact contact = contactRepository.findFirstByUserAndId(user, request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPhone(request.getPhoneNumber());
        contact.setEmail(request.getEmail());
        contact.setUser(user);
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    public void delete(User user, String contactId){
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));
        contactRepository.delete(contact);
    }

    @Transactional
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        Specification<Contact> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));

            if(Objects.nonNull(request.getName())){
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%" + request.getName()),
                        builder.like(root.get("lastName"), "%" + request.getName())
                ));
            }

            if(Objects.nonNull(request.getEmail())){
                predicates.add(builder.like(root.get("email"), "%" + request.getEmail()));
            }

            if(Objects.nonNull(request.getPhone())){
                predicates.add(builder.like(root.get("phone"), "%" + request.getPhone()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);

        List<ContactResponse> contactResponses = contacts.getContent().stream()
                .map(this::toContactResponse).toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }

    private ContactResponse toContactResponse(Contact contact) {
        return ContactResponse.builder().id(contact.getId()).firstName(contact.getFirstName()).lastName(contact.getLastName())
                .email(contact.getEmail()).phone(contact.getPhone()).build();
    }
}
