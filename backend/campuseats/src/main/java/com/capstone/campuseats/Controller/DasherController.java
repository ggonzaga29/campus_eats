package com.capstone.campuseats.Controller;

import com.capstone.campuseats.Entity.DasherEntity;
import com.capstone.campuseats.Service.DasherService;
import com.capstone.campuseats.config.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DasherController {

    private final DasherService dasherService;

    @GetMapping
    public ResponseEntity<List<DasherEntity>> getAllDashers() {
        return new ResponseEntity<>(dasherService.getAllDashers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<DasherEntity>> getDasherById(@PathVariable String id) {
        return new ResponseEntity<>(dasherService.getDasherById(id), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DasherEntity>> getActiveDashers(){
        return new ResponseEntity<>(dasherService.getActiveDashers(), HttpStatus.OK);
    }

    @GetMapping("/pending-lists")
    public ResponseEntity<Map<String, List<DasherEntity>>> getDashers() {
        Map<String, List<DasherEntity>> dashersMap = dasherService.getDashers();
        return new ResponseEntity<>(dashersMap, HttpStatus.OK);
    }



    @PostMapping("/apply")
    public ResponseEntity<?> applyDasher(
            @RequestPart("dasher") String dasherStr,
            @RequestPart("image") MultipartFile image,
            @RequestPart("userId") String userId) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            DasherEntity dasher = mapper.readValue(dasherStr, DasherEntity.class);
            DasherEntity createdDasher = dasherService.createDasher(dasher, image, userId);
            return new ResponseEntity<>(createdDasher, HttpStatus.CREATED);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




    @PutMapping("/update/{dasherId}")
    public ResponseEntity<?> updateDasher(
            @PathVariable String dasherId,
            @RequestPart("dasher") String dasherStr,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        try {


            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            DasherEntity dasher = mapper.readValue(dasherStr, DasherEntity.class);
            DasherEntity updatedDasher = dasherService.updateDasher(dasherId, dasher, image);
            return new ResponseEntity<>(updatedDasher, HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update/{dasherId}/status")
    public ResponseEntity<Boolean> updateDasherStatus(@PathVariable String dasherId, @RequestParam String status) {
        boolean isUpdated = dasherService.updateDasherStatus(dasherId, status);
        if (isUpdated) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }



}

