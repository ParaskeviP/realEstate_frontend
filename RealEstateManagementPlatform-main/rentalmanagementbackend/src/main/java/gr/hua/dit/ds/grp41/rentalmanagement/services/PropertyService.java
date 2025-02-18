package gr.hua.dit.ds.grp41.rentalmanagement.services;

import gr.hua.dit.ds.grp41.rentalmanagement.entities.Property;
import gr.hua.dit.ds.grp41.rentalmanagement.entities.Tenant;
import gr.hua.dit.ds.grp41.rentalmanagement.repositories.PropertyRepo;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepo propertyRepository;

    @Transactional
    public void saveProperty(Property property) {
        propertyRepository.save(property);
    }

    @Transactional
    public void deleteProperty(Integer id){
        propertyRepository.deleteById(id);
    }

    @Transactional
    public List<Property> getAllProperties(){
        return propertyRepository.findAll();
    }

    public List<Property> searchProperties(Integer roomNum, Integer bedNum, Integer bathNum,
                                           String city, Integer size, Long price, Boolean petsAllowed,
                                           Boolean hasGarden, Boolean hasBalcony, Boolean hasHeating,
                                           Boolean isFurnished, Integer renovationYear) {
        return propertyRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (roomNum != null) predicates.add(cb.equal(root.get("roomNum"), roomNum));
            if (bedNum != null) predicates.add(cb.equal(root.get("bedNum"), bedNum));
            if (bathNum != null) predicates.add(cb.equal(root.get("bathNum"), bathNum));
            if (city != null && !city.isEmpty()) predicates.add(cb.like(root.get("city"), "%" + city + "%"));
            if (size != null) predicates.add(cb.equal(root.get("size"), size));
            if (price != null) predicates.add(cb.lessThanOrEqualTo(root.get("price"), price));
            if (petsAllowed != null) predicates.add(cb.equal(root.get("petsAllowed"), petsAllowed));
            if (hasGarden != null) predicates.add(cb.equal(root.get("hasGarden"), hasGarden));
            if (hasBalcony != null) predicates.add(cb.equal(root.get("hasBalcony"), hasBalcony));
            if (hasHeating != null) predicates.add(cb.equal(root.get("hasHeating"), hasHeating));
            if (isFurnished != null) predicates.add(cb.equal(root.get("isFurnished"), isFurnished));
            if (renovationYear != null) predicates.add(cb.equal(root.get("renovationYear"), renovationYear));

            System.out.println(city);
            System.out.println("HJFDSGHJKDFGHJKSDFGHJKDFS");
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Transactional
    public List<Property> getPropertiesByTenant(Tenant tenant){
        return propertyRepository.getPropertiesByTenant(tenant);
    }

    @Transactional
    public Property getById(Integer id){
        return propertyRepository.getById(id);
    }
}
