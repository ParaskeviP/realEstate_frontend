package gr.hua.dit.ds.grp41.rentalmanagement.repositories;

import gr.hua.dit.ds.grp41.rentalmanagement.entities.Property;
import gr.hua.dit.ds.grp41.rentalmanagement.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepo extends JpaRepository<Property, Integer> {
    List<Property> findByRoomNum(Integer roomNum);

    List<Property> findByBedNum(Integer bedNum);

    List<Property> findByBathNum(Integer bathNum);

    List<Property> findByCity(String city);

    List<Property> findBySize(Integer size);

    List<Property> findByPrice(Long price);

    List<Property> findByPetsAllowed(Boolean petsAllowed);

    List<Property> findByHasGarden(Boolean hasGarden);

    List<Property> findByHasBalcony(Boolean hasBalcony);

    List<Property> findByHasHeating(Boolean hasHeating);
    
    List<Property> findByIsFurnished(Boolean isFurnished);

    List<Property> findByRenovationYear(Integer renovationYear);

    List<Property> getPropertiesByTenant(Tenant tenant);

}