package com.avanzada.unilocal.Unilocal.serviceImplements;

import com.avanzada.unilocal.Unilocal.dto.CrearCiudadDto;
import com.avanzada.unilocal.Unilocal.entity.Ciudad;
import com.avanzada.unilocal.Unilocal.entity.Place;
import com.avanzada.unilocal.Unilocal.entity.Tipo;
import com.avanzada.unilocal.Unilocal.repository.CiudadRepository;
import com.avanzada.unilocal.Unilocal.repository.TiposRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PublicService {

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private TiposRepository tiposRepository;


    public List<Ciudad> getCiudades(){
        return ciudadRepository.findAll();
    }

    public List<Tipo> getTipos(){
        return tiposRepository.findAll();
    }

    public void  crearCiudad(CrearCiudadDto crearCiudadDto){
        Ciudad ciudad = new Ciudad();

        ciudad.setNombre(crearCiudadDto.nombre());
        ciudad.setId(autoIncrementCiudad());
        ciudadRepository.save(ciudad);

    }

    // private methods
    private int autoIncrementCiudad() {
        List<Ciudad> ciudad = ciudadRepository.findAll();
        return ciudad.isEmpty() ? 1 :
                ciudad.stream().max(Comparator.comparing(Ciudad::getId)).get().getId() + 1;
    }
}
