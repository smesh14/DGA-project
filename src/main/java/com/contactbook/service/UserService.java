package com.contactbook.service;

import com.contactbook.entity.*;
import com.contactbook.error.CmsException;
import com.contactbook.error.CmsExceptionCode;
import com.contactbook.error.ExceptionKeys;
import com.contactbook.mapper.ContactMapper;
import com.contactbook.mapper.UserMapper;
import com.contactbook.repository.ContactRepository;
import com.contactbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ContactRepository contactRepository;

    private final UserMapper userMapper;

    private final ContactMapper contactMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(UserRequest userRequest) {
            checkDuplicatedUser(userRequest);
            var user = userMapper.userRequestToUser(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            var savedUser = userRepository.save(user);
            log.info("User with username: {} saved", savedUser.getName());
            return userMapper.userToUserResponse(savedUser);
    }



    @Transactional
    public ContactResponse addContact(ContactRequest contactRequest, String name) {
        var user = userRepository.findByName(name).orElseThrow(this::
                createUserNotFoundWithNameException);
        checkDuplicatedContact(contactRequest,user.getId());
        var contact = contactMapper.toContact(contactRequest);

        contact.setUser(user);
        var savedContact = contactRepository.save(contact);
        log.info("Contact with username: {} saved", savedContact.getName());
        return contactMapper.toContactResponse(savedContact);
    }



    @Transactional
    public ContactResponse updateContact(ContactRequest contactRequest, String userName, String contactName) {
        var user = userRepository.findByName(userName).orElseThrow(this::
                createUserNotFoundWithNameException);
        var contact = contactRepository.findByNameAndUserId(contactName,user.getId());

        checkDuplicatedContact(contactRequest,user.getId(),contact.getId());

        contact.setName(contactRequest.getName());
        contact.setPhoneNumber(contactRequest.getPhoneNumber());

        var savedContact = contactRepository.save(contact);
        log.info("Contact with username: {} updated", savedContact.getName());
        return contactMapper.toContactResponse(savedContact);
    }



    @Transactional()
    public ContactResponse loadContact(String userName, String contactName)  {
        var user = userRepository.findByName(userName).orElseThrow(this::
                createUserNotFoundWithNameException);
        var contact = contactRepository.findByNameAndUserId(contactName,user.getId());

        log.info("Contact with username: {} fetched", contact.getName());
        return contactMapper.toContactResponse(contact);
    }



    @Transactional
    public Page<ContactResponse> loadContacts(String userName, Pageable pageRequest) {
        var user = userRepository.findByName(userName)
                .orElseThrow(this::createUserNotFoundWithNameException);
        Page<Contact> contactPage = contactRepository.findByUserId(user.getId(),pageRequest);
        log.info("Contacts with users username: {} fetched", user.getName());

        List<ContactResponse> contactResponses = contactPage.stream()
                .map(contactMapper::toContactResponse)
                .toList();

        return new PageImpl<>(contactResponses,pageRequest,contactPage.getTotalElements());
    }


    private void checkDuplicatedContact(ContactRequest contactRequest, Long userId) {
        checkDuplicateContactUsername(contactRequest.getName(),userId);
        checkDuplicateContactPhoneNumber(contactRequest.getPhoneNumber(),userId);
    }

    private void checkDuplicatedContact(ContactRequest contactRequest, Long userId,Long contactId) {
        checkDuplicateContactUsername(contactRequest.getName(),userId,contactId);
        checkDuplicateContactPhoneNumber(contactRequest.getPhoneNumber(),userId,contactId);
    }



    private void checkDuplicatedUser(UserRequest userRequest) {
        checkDuplicateEmail(userRequest.getEmail());
        checkDuplicateUsername(userRequest.getName());
        checkDuplicatePhoneNumber(userRequest.getPhoneNumber());
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CmsException(CmsExceptionCode.DUPLICATE, ExceptionKeys.UserEmailNotUnique) ;
        }
    }

    private void checkDuplicateUsername(String username) {
        if (userRepository.existsByName(username)) {
            throw new CmsException(CmsExceptionCode.DUPLICATE, ExceptionKeys.UserNameNotUnique);
        }
    }

    private void checkDuplicatePhoneNumber(String personalNumber) {
        if (userRepository.existsByPhoneNumber(personalNumber)) {
            throw new CmsException(CmsExceptionCode.DUPLICATE, ExceptionKeys.UserPhoneNumberNotUnique);
        }
    }

    private void checkDuplicateContactPhoneNumber(String phoneNumber, Long userId) {
        if (contactRepository.existsByPhoneNumberAndUserId(phoneNumber,userId)) {
            throw new CmsException(CmsExceptionCode.DUPLICATE, ExceptionKeys.ContactPhoneNumberNotUnique);
        }
    }

    private void checkDuplicateContactUsername(String name, Long userId) {
        if (contactRepository.existsByNameAndUserId(name,userId)) {
            throw new CmsException(CmsExceptionCode.DUPLICATE, ExceptionKeys.ContactNameNotUnique);
        }
    }

    private void checkDuplicateContactPhoneNumber(String phoneNumber, Long userId,Long id) {
        if (contactRepository.existsByPhoneNumberAndUserIdAndIdIsNot(phoneNumber,userId,id)) {
            throw new CmsException(CmsExceptionCode.DUPLICATE, ExceptionKeys.ContactPhoneNumberNotUnique);
        }
    }

    private void checkDuplicateContactUsername(String name, Long userId, Long id) {
        if (contactRepository.existsByNameAndUserIdAndIdIsNot(name,userId,id)) {
            throw new CmsException(CmsExceptionCode.DUPLICATE, ExceptionKeys.ContactNameNotUnique);
        }
    }

    private CmsException createUserNotFoundWithNameException() {
        return new CmsException(CmsExceptionCode.NOT_FOUND, ExceptionKeys.UserDoesNotExistsWithName);
    }



}
