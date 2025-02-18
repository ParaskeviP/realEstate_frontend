package gr.hua.dit.ds.grp41.rentalmanagement.rest;

import gr.hua.dit.ds.grp41.rentalmanagement.entities.Property;
import gr.hua.dit.ds.grp41.rentalmanagement.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyControllerRest {
    @Autowired
    private PropertyService propertyService;

    @GetMapping("/allProperties")
    public List<Property> getAllProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/search")
    public List<Property> searchProperties(
            @RequestParam(required = false) Integer roomNum,
            @RequestParam(required = false) Integer bedNum,
            @RequestParam(required = false) Integer bathNum,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long price,
            @RequestParam(required = false) Boolean petsAllowed,
            @RequestParam(required = false) Boolean hasGarden,
            @RequestParam(required = false) Boolean hasBalcony,
            @RequestParam(required = false) Boolean hasHeating,
            @RequestParam(required = false) Boolean isFurnished,
            @RequestParam(required = false) Integer renovationYear
    ) {
        return propertyService.searchProperties(roomNum, bedNum, bathNum, city, size, price, petsAllowed,
                hasGarden, hasBalcony, hasHeating, isFurnished, renovationYear);
    }
}