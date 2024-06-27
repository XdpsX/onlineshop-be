package com.xdpsx.ecommerce.repositories.criteria;

import com.xdpsx.ecommerce.entities.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorCriteriaRepository {
    Page<Vendor> findWithFilter(Pageable pageable, String name, String sortField);
}
