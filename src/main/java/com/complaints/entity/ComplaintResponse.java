package com.complaints.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponse {
    CustomerComplaint customerComplaint;
    User user;
    Purchase purchase;
}
