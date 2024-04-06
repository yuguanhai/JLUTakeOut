package com.jlu.takeout.service;

import com.jlu.takeout.entity.AddressBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressBookService {

    List<AddressBook> list(AddressBook addressBook);

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void setDefault(AddressBook addressBook);

    void deleteById(Long id);

}
