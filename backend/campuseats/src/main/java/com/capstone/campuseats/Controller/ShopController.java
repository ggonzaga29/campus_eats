package com.capstone.campuseats.Controller;

import com.capstone.campuseats.Entity.DasherEntity;
import com.capstone.campuseats.Entity.ShopEntity;
import com.capstone.campuseats.Entity.UserEntity;
import com.capstone.campuseats.Service.ShopService;
import com.capstone.campuseats.config.CustomException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
//add in the apply shop to check if theres already existing shop/dasher application return error
@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<List<ShopEntity>> getAllShops() {
        return new ResponseEntity<>(shopService.getAllShops(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ShopEntity>> getShopById(@PathVariable String id) {
        return new ResponseEntity<>(shopService.getShopById(id), HttpStatus.OK);
    }

    @GetMapping("/pending-lists")
    public ResponseEntity<Map<String, List<ShopEntity>>> getShopsList() {
        Map<String, List<ShopEntity>> shopsMap = shopService.getShopsList();
        return new ResponseEntity<>(shopsMap, HttpStatus.OK);
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyShop(
            @RequestPart("shop") ShopEntity shop,
            @RequestPart("image") MultipartFile image,
            @RequestPart("userId") String userIdStr) throws IOException {
        try {
            String userId = new String(userIdStr);
            ShopEntity createdShop = shopService.createShop(shop, image, userId);
            return new ResponseEntity<>(createdShop, HttpStatus.CREATED);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/shop-update/{shopId}")
    public ResponseEntity<?> updateShop(
            @PathVariable String shopId,
            @RequestPart("shop") ShopEntity shop,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        try {
            ShopEntity updatedShop = shopService.updateShop(shopId, shop, image);
            return new ResponseEntity<>(updatedShop, HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<ShopEntity>> getActiveShops() {
        return new ResponseEntity<>(shopService.getActiveShops(), HttpStatus.OK);
    }

    @PutMapping("/update/{shopId}/status")
    public ResponseEntity<Boolean> updateShopStatus(@PathVariable String shopId, @RequestParam String status) {
        boolean isUpdated = shopService.updateShopStatus(shopId, status);
        if (isUpdated) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update/{shopId}/deliveryFee")
    public ResponseEntity<Boolean> updateShopDeliveryFee(@PathVariable String shopId, @RequestParam float deliveryFee) {
        boolean isUpdated = shopService.updateShopDeliveryFee(shopId, deliveryFee);
        if (isUpdated) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}



