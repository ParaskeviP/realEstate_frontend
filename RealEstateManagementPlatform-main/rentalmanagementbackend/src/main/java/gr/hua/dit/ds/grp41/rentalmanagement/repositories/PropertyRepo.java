package gr.hua.dit.ds.grp41.rentalmanagement.repositories;

import gr.hua.dit.ds.grp41.rentalmanagement.entities.Property;
import gr.hua.dit.ds.grp41.rentalmanagement.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepo extends JpaRepository<Property, Integer>, JpaSpecificationExecutor<Property> {
    List<Property> getPropertiesByTenant(Tenant tenant);
}