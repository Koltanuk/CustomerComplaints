package com.complaints.repo;

import com.complaints.entity.CustomerComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ComplaintRepository extends JpaRepository<CustomerComplaint, UUID> {

}
