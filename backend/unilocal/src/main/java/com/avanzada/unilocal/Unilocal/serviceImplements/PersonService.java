package com.avanzada.unilocal.Unilocal.serviceImplements;

import com.avanzada.unilocal.Unilocal.dto.ChangePasswordDTO;
import com.avanzada.unilocal.Unilocal.dto.RegisterUserDto;
import com.avanzada.unilocal.Unilocal.dto.SesionUserDto;
import com.avanzada.unilocal.Unilocal.dto.UpdateUserDto;
import com.avanzada.unilocal.Unilocal.entity.Person;
import com.avanzada.unilocal.Unilocal.enums.StateUnilocal;
import com.avanzada.unilocal.Unilocal.interfaces.UserService;
import com.avanzada.unilocal.Unilocal.repository.PersonRepository;
import com.avanzada.unilocal.Unilocal.enums.Role;
import com.avanzada.unilocal.global.exceptions.AttributeException;
import com.avanzada.unilocal.global.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author Juanes Cardona
 */
@Service
public class PersonService implements UserService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    EmailService emailService;

    /**
     * Method to register a new user
     *
     * @param registerUserDto User to register
     * @return The registered user
     * @throws AttributeException Exception that will be executed if the nickname already exists
     */
    @Override
    public Person signUp(RegisterUserDto registerUserDto) throws AttributeException {
        if (personRepository.existsByNickname(registerUserDto.nickname()))
            throw new AttributeException("nickname already in use");
        if (personRepository.existsByEmail(registerUserDto.email()))
            throw new AttributeException("Email already in use");

        int id = autoIncrement();
        StateUnilocal register = StateUnilocal.Active;
        Person person = new Person(id, registerUserDto.name(), registerUserDto.photo(), registerUserDto.nickname(), registerUserDto.email(), registerUserDto.password(), registerUserDto.residenceCity(), Role.USER, register);

        return personRepository.save(person);
    }


    @Override
    public void login(SesionUserDto sesionUserDto) throws Exception {

    }

    /**
     * Method to update user information
     *
     * @param updateUserDto User to be updated
     * @param id            User id
     * @return The updated user
     * @throws ResourceNotFoundException Exception that is executed if a user with that id is not found
     */
    @Override
    public Person profileEdit(UpdateUserDto updateUserDto, int id) throws ResourceNotFoundException {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        person.setName(updateUserDto.name());
        person.setEmail(updateUserDto.email());
        person.setNickname(updateUserDto.nickname());
        person.setPhoto(updateUserDto.photo());
        person.setResidenceCity(updateUserDto.residenceCity());

        return personRepository.save(person);
    }

    /**
     * Method that deletes the user account
     *
     * @param id User id
     * @return User whose registration status was changed to Inactive
     * @throws ResourceNotFoundException Exception that is executed if a user with that id is not found
     */
    @Override
    public Person delete(int id) throws ResourceNotFoundException {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El id no esta asociado a un usuario"));
        person.setStateUnilocal(StateUnilocal.Inactive);
        personRepository.save(person);
        return person;
    }

    /**
     * This method generates a link and sends an email to a user to change the password
     *
     * @param email User email
     * @throws ResourceNotFoundException Exception that is executed if a user with that email is not found
     */
    @Override
    public void sendLinkPassword(String email) throws ResourceNotFoundException {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("El email no esta asociado a un usuario"));

        String subject = "RECUPERACIÓN DE CONTRASEÑA";
        String body = "TODO: Hacer metodo que genere el cuerpo del correo y que genere" +
                "el link de recuperacion";
        emailService.sendmail(email, subject, body);
    }

    /**
     * This method changes the user's password
     *
     * @param changePasswordDTO DTO to change the password
     *
     * @throws ResourceNotFoundException Exception that is executed if a user with that id is not found
     */
    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) throws ResourceNotFoundException {
        Person person = personRepository.findById(changePasswordDTO.id())
                .orElseThrow(() -> new ResourceNotFoundException("El id no esta asociado a un usuario"));

        person.setPassword(changePasswordDTO.password());
        personRepository.save(person);
    }


    //----------------------Private Methods--------------------------------------

    /**
     * Method that obtains all users from the database
     *
     * @return The database user list
     */
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    /**
     * Method that obtains a user by his/her ID from the database
     *
     * @param id User id
     * @return User
     * @throws ResourceNotFoundException Exception that is executed if a user with that id is not found
     */
    public Person getOne(int id) throws ResourceNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    private int autoIncrement() {
        List<Person> people = personRepository.findAll();
        return people.isEmpty() ? 1 :
                people.stream().max(Comparator.comparing(Person::getId)).get().getId() + 1;
    }
}
