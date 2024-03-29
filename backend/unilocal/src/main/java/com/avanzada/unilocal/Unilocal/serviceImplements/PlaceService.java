package com.avanzada.unilocal.Unilocal.serviceImplements;

import com.avanzada.unilocal.Unilocal.dto.CreatePlaceDto;
import com.avanzada.unilocal.Unilocal.dto.RegisterRevisionDto;
import com.avanzada.unilocal.Unilocal.entity.Place;
import com.avanzada.unilocal.Unilocal.enums.StateUnilocal;
import com.avanzada.unilocal.Unilocal.interfaces.BusinessService;
import com.avanzada.unilocal.Unilocal.repository.PlaceRepository;
import com.avanzada.unilocal.global.exceptions.AttributeException;
import com.avanzada.unilocal.global.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author Juanes Cardona
 */
@Service
public class PlaceService implements BusinessService {

    @Autowired
    PlaceRepository placeRepository;

    @Override
    public Place createBusiness(CreatePlaceDto createPlaceDto) throws AttributeException {
        if (placeRepository.existsByName(createPlaceDto.name()))
            throw new AttributeException("name already in use");

        int id = autoIncrement();
        StateUnilocal stateBusiness = StateUnilocal.Revision;
        Place place = new Place(id, createPlaceDto.description(), createPlaceDto.name(), createPlaceDto.schedules(), createPlaceDto.images(), createPlaceDto.businessType(), createPlaceDto.phones(), stateBusiness);

        return placeRepository.save(place);
    }

    @Override
    public Place updateBusiness(int id, CreatePlaceDto createPlaceDto) throws AttributeException, ResourceNotFoundException {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        if (placeRepository.existsByName(createPlaceDto.name()) && !Objects.equals(placeRepository.findByName(createPlaceDto.name()).get().getName(), createPlaceDto.name()))
            throw new AttributeException("id already in use");

        place.setName(createPlaceDto.name());
        place.setImages(createPlaceDto.images());
        place.setDescription(createPlaceDto.description());
        place.setSchedules(createPlaceDto.schedules());
        place.setBusinessType(createPlaceDto.businessType());
        place.setPhones(createPlaceDto.phones());

        return placeRepository.save(place);
    }

    @Override
    public Place deleteBusiness(int id) throws ResourceNotFoundException {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El id no esta asociado a un lugar"));
        place.setStateBusiness(StateUnilocal.Inactive);
        placeRepository.save(place);
        return place;
    }

    @Override
    public void findBusiness(int id) {

    }

    @Override
    public void filterByState(StateUnilocal stateBusiness) {

    }

    @Override
    public void listOwnerBusiness() {

    }

    @Override
    public void changeState(StateUnilocal newState) {

    }

    @Override
    public void registerRevision(RegisterRevisionDto registerRevisionDto) {

    }

    //-----------------------------Private Methods----------------------------------------

    public List<Place> getAll() {
        return placeRepository.findAll();
    }


    public Place getOne(int id) throws ResourceNotFoundException {
        return placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    // private methods
    private int autoIncrement() {
        List<Place> places = placeRepository.findAll();
        return places.isEmpty() ? 1 :
                places.stream().max(Comparator.comparing(Place::getId)).get().getId() + 1;
    }
}
