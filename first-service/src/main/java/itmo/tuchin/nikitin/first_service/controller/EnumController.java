package itmo.tuchin.nikitin.first_service.controller;

import itmo.tuchin.nikitin.first_service.dto.ColorDTO;
import itmo.tuchin.nikitin.first_service.dto.CountryDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://se.ifmo.ru")
@RestController
@RequestMapping(
        path = "/",
        produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8"
)
public class EnumController {

    @GetMapping("/color")
    public ResponseEntity<ColorDTO> getColors() {
        return ResponseEntity.ok(new ColorDTO());
    }

    @GetMapping("/country")
    public ResponseEntity<CountryDTO> getCountry() {
        return ResponseEntity.ok(new CountryDTO());
    }
}
