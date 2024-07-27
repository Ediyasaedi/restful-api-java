package yassa_exercise.restful_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.*;
import yassa_exercise.restful_api.service.ContactService;

import java.util.Collections;
import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    private ResponseData<ContactResponse> toResponseData(ContactResponse contactResponse, String errMsg){
        ResponseData<ContactResponse> resData = new ResponseData<>();
        resData.setErrors(errMsg);
        resData.setLst(Collections.singletonList(contactResponse));

        return resData;
    }

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<ContactResponse>> create(User user, @RequestBody CreateContactRequest request){
        ContactResponse contactResponse = contactService.create(user, request);

        return WebResponse.<ResponseData<ContactResponse>>builder().success(true).data(toResponseData(contactResponse, null)).build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<ContactResponse>> get(User user, @PathVariable String contactId){
        ContactResponse contactResponse = contactService.get(user,contactId);

        return WebResponse.<ResponseData<ContactResponse>>builder().success(true).data(toResponseData(contactResponse, null)).build();
    }

    @PutMapping(
            path = "/api/contacts/{contactId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<ContactResponse>> update(User user,
                                                             @RequestBody UpdateContactRequest request,
                                                             @PathVariable String contactId){
        request.setId(contactId);
        ContactResponse contactResponse = contactService.update(user, request);

        return WebResponse.<ResponseData<ContactResponse>>builder().success(true).data(toResponseData(contactResponse, null)).build();
    }

    @DeleteMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<ContactResponse>> delete(User user, @PathVariable String contactId){
        contactService.delete(user,contactId);

        return WebResponse.<ResponseData<ContactResponse>>builder().success(true).build();
    }

    @GetMapping(
            path = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ContactResponse>>
    search(User user,
           @RequestParam(value = "name", required = false) String name,
           @RequestParam(value = "phone", required = false) String phone,
           @RequestParam(value = "email", required = false) String email,
           @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
           @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){

        SearchContactRequest request = SearchContactRequest.builder()
                .page(page).name(name).phone(phone).email(email).size(size)
                .build();

        Page<ContactResponse> contactResponse = contactService.search(user, request);
        return WebResponse.<List<ContactResponse>>builder().success(true)
                .data(contactResponse.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(contactResponse.getNumber())
                        .total(contactResponse.getTotalPages())
                        .size(contactResponse.getSize())
                        .build())
                .build();
    }
}
