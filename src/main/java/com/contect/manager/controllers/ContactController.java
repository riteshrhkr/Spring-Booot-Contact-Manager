package com.contect.manager.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.contect.manager.models.Contact;
import com.contect.manager.services.ContactServices;
import com.contect.manager.services.FileServices;
import com.contect.manager.services.UserServices;
import com.contect.manager.utils.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contact")
public class ContactController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private ContactServices contactServices;
    @Autowired
    private FileServices fileServices;

    @GetMapping("/add")
    public String addContact(Model model) {
        model.addAttribute("contact", new Contact());
        // Contact contact = new Contact();
        // contact.setName("Ritesh Kumar");
        // contact.setPhone("9876543210");
        // contact.setEmail("abcd@gmail.com");
        // contact.setAddress("Delhi India");
        // contact.setWork("Software Developer");
        // contact.setDescription("This is temp description");
        // model.addAttribute("contact", contact);
        return "user/addContact_form";
    }

    @PostMapping("/add")
    public String addContact(@Valid @ModelAttribute Contact contact, BindingResult result,
            @RequestParam("profileImage") MultipartFile file, Model model, Principal principal) {
        String userName = principal.getName();

        if (result.hasErrors()) {
            return "user/addContact_form";

        }

        // save image in file at /img/users/{userName}/img.jpg and set contact.image
        if (file.isEmpty()) {
            contact.setImage("default.png");
        } else if (FileServices.isImage(file.getContentType())) {
            try {
                String fileName = fileServices.saveFile(file, "/img/users/" + userName + "/");
                if (fileName != null) {
                    contact.setImage(fileName);
                } else {
                    contact.setImage("default.png");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        contact.setUser(userServices.getUserByUserName(userName));
        contactServices.saveContact(contact);

        model.addAttribute("message", new Message("Your contact has been added successfully", "success"));
        model.addAttribute("contact", new Contact());
        return "user/addContact_form";
    }

    @GetMapping("/list")
    public String listContact(Model model, Principal principal) {
        // model.addAttribute("contacts", contactServices.getAllContacts());
        Page<Contact> contacts = contactServices.getContactsByUsername(principal.getName(), 0);

        // model.addAttribute("currPage", 1);
        // model.addAttribute("totalPages", contacts.getTotalPages());

        model.addAttribute("contacts", contacts);
        return "user/showContacts";
    }

    @GetMapping("/list/{currPage}")
    public String listContactPage(@PathVariable("currPage") int currPage, Model model, Principal principal) {
        currPage--; // in view page start from 1 but in controller start from 0
        Page<Contact> contacts = contactServices.getContactsByUsername(principal.getName(), currPage);

        // model.addAttribute("currPage", currPage+1); // currPage  is same as contacts.getNumber();
        // model.addAttribute("totalPages", contacts.getTotalPages());
        model.addAttribute("contacts", contacts);

        System.out.println("contact size = " + contacts.getNumberOfElements());
        return "user/showContacts";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public boolean deleteContact(@PathVariable("id") int id, Model model, Principal principal, HttpSession session) {
        // User userByUserName = userServices.getUserByUserName(principal.getName());
        try {
            boolean canDelete = contactServices.getContactById(id).getUser().getEmail().equals(principal.getName());
            if (canDelete) {
                String fileName = contactServices.getContactById(id).getImage();
                if (contactServices.deleteContactById(id)) {
                    FileServices.deleteFile("/img/users/" + principal.getName() + "/" + fileName);
                    session.setAttribute("message", new Message("Contact deleted successfully", "warning"));
                    return true;
                } else {
                    session.setAttribute("message", new Message("Failed to delete contact", "danger"));
                    return false;
                }
            } else {
                session.setAttribute("message", new Message("Sorry! You can not delete this contact", "warning"));
                System.out.println("Yoou are not authorized person to delete this ");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.setAttribute("message", new Message("Failed to delete contact. Try again", "danger"));
            return false;
        }

    }

    @PutMapping("/update")
    @ResponseBody
    public boolean updateContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
            @RequestParam("profileImage") MultipartFile file, Model model, Principal principal,HttpSession session) {
        // User userByUserName = userServices.getUserByUserName(principal.getName());
        try {
            Contact oldContact = contactServices.getContactById(contact.getId());
            boolean canUpdate = oldContact.getUser().getEmail().equals(principal.getName());
            if (canUpdate) {
                if (result.hasErrors()) {
                    session.setAttribute("message", new Message("Failed to update contact. Try again", "danger"));
                    return false;
                }
                if (FileServices.isImage(file.getContentType())) {
                    try {
                        String fileName = fileServices.updateFile(file, "/img/users/" + principal.getName() + "/",
                                oldContact.getImage());
                        if (fileName != null) {
                            contact.setImage(fileName);
                        } else {
                            contact.setImage("default.png");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        contact.setImage("default.png");
                    }
                } else {

                    // contact.setImage("default.png");
                    contact.setImage(oldContact.getImage());
                }
                contact.setUser(userServices.getUserByUserName(principal.getName()));
                System.out.println("contact = " + contact);
                contactServices.updateContact(contact);
                session.setAttribute("message", new Message("Contact updated successfully", "success"));
                return true;
            } else {
                session.setAttribute("message", new Message("Sorry! You can not update this contact", "warning"));
                System.out.println("You are not authorized person to update this contact");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.setAttribute("message", new Message("Failed to update contact. Try again", "danger"));
            return false;
        }
    }

    @GetMapping("/search/{identifier}/{keyword}")
    @ResponseBody
    public ResponseEntity<List<Contact>> getSearchResults(@PathVariable("identifier") String identifier,
            @PathVariable("keyword") String keyword) {
        try {
            
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("username = " + username);

            Pageable pageable = PageRequest.of(0, 10);

            if (identifier.equals("name")) {
                return ResponseEntity.ok().body(contactServices.getSearchResultsByName(keyword, username,pageable));
            } else if (identifier.equals("phone")) {
                return ResponseEntity.ok().body(contactServices.getSearchResultsByPhone(keyword, username,pageable));
            } else {
                return ResponseEntity.ok().body(List.of());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/search")
    public String searchContact(@RequestParam("search-input") String keyword,Model model, Principal principal, HttpSession session) {
        List<Contact> contacts ;if(keyword.isEmpty()){
            return "redirect:/user/contact/list";
        }else if (keyword.length() < 3) {
            session.setAttribute("message", new Message("Please enter at least 3 characters", "warning"));
            return "redirect:/user/contact/list";
            
        }else if(keyword.matches("-?\\d+")){
            contacts = contactServices.getSearchResultsByPhone(keyword, principal.getName(),PageRequest.of(0, 100));
        }
        else{
            contacts = contactServices.getSearchResultsByName(keyword, principal.getName(),PageRequest.of(0, 100));
        }
        Page<Contact> contactPage = new PageImpl<>(contacts);
        model.addAttribute("contacts",contactPage);
        return "user/showContacts";
    }
    @GetMapping("/details/{id}")
    public String getContactDetails(@PathVariable("id") int id, Model model,Principal principal) {
        try{
            boolean canView = contactServices.getContactById(id).getUser().getEmail().equals(principal.getName());
            if(canView){
                Contact contact = contactServices.getContactById(id);
                model.addAttribute("contact", contact);
                return "user/contactDetails";
            }
            return "redirect:/user/contact/list";
        }catch(Exception e){
            System.out.println(e.getMessage());
            model.addAttribute("message", new Message("Failed to get contact. Try again", "danger"));
            return "redirect:/user/contact/list";
        }
    }



}
