package com.contactbook.mapper;


import com.contactbook.entity.Contact;
import com.contactbook.entity.ContactRequest;
import com.contactbook.entity.ContactResponse;
import org.springframework.stereotype.Service;

@Service
public class ContactMapper {

    public Contact toContact(ContactRequest contactRequest){
        var contact = new Contact();
        contact.setName(contactRequest.getName());
        contact.setPhoneNumber(contactRequest.getPhoneNumber());
        return contact;
    }

    public ContactResponse toContactResponse(Contact contact){
        var contactResponse = new ContactResponse();
        contactResponse.setName(contact.getName());
        contactResponse.setPhoneNumber(contact.getPhoneNumber());
        return contactResponse;
    }

}
