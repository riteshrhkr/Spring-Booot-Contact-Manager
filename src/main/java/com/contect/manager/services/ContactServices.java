package com.contect.manager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.contect.manager.dao.ContactRepo;
import com.contect.manager.models.Contact;

@Service
public class ContactServices {
    @Autowired
    private ContactRepo contactRepo;

    public Contact getContactById(int id) {
        try {
            return contactRepo.findById(id).get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Contact();
        }
    }

    public Contact saveContact(Contact contact) {
        try {
            return contactRepo.save(contact);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Contact();
        }
    }

    public Page<Contact> getContactsByUsername(String username, int currentPage) {

        try {
            Pageable pageable = PageRequest.of(currentPage, 30);
            return contactRepo.findByUsername(username, pageable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean deleteContactById(int id) {
        try {
            contactRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Contact updateContact(Contact updatedContact) {
        try {
            if (contactRepo.existsById(updatedContact.getId())) {
                // updatedContact.setId(updatedContact.getId());
                return contactRepo.save(updatedContact);
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Contact> getSearchResultsByName(String name, String username,Pageable pageable) {
        try{
            return contactRepo.findByNameAndUsername(name, username, pageable);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public List<Contact> getSearchResultsByPhone(String phone, String username,Pageable pageable) {
        try{
            return contactRepo.findByPhoneAndUsername(phone, username, pageable );
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return List.of();
        }
    }
    public List<Contact> getSearchResultsByName(String name, String username) {
        try{
            return contactRepo.findByNameAndUsername(name, username,Pageable.unpaged());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public List<Contact> getSearchResultsByPhone(String phone, String username) {
        try{
            return contactRepo.findByPhoneAndUsername(phone, username,Pageable.unpaged() );
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return List.of();
        }
    }


}
