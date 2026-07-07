package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twilio_spa_fetch_backend.dto.PageDTO;
import twilio_spa_fetch_backend.dto.PhoneDTO;
import twilio_spa_fetch_backend.service.PhoneNumberService;

@RestController
@RequestMapping("/api/v1/phone-numbers")
public class PhoneNumberController {

    @Autowired
    PhoneNumberService phoneNumberService;

    @GetMapping
    public ResponseEntity<PageDTO<PhoneDTO>> getPhoneNumbers(
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String pageToken) {
        return ResponseEntity.ok(phoneNumberService.getPhoneNumbers(pageSize, pageToken));
    }
}
