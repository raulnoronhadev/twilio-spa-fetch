package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twilio_spa_fetch_backend.dto.PhoneDTO;
import twilio_spa_fetch_backend.service.PhoneNumberService;

import java.util.List;

@RestController
@RequestMapping("/PhoneNumber")
public class PhoneNumberController {

    @Autowired
    PhoneNumberService phoneNumberService;

    @GetMapping("/List")
    public ResponseEntity<List<PhoneDTO>> getAllPhoneNumbers() {
        return ResponseEntity.ok(phoneNumberService.getAllPhoneNumbers());
    }
}
