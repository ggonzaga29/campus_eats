package com.capstone.campuseats.Controller;

import com.capstone.campuseats.Entity.ItemEntity;
import com.capstone.campuseats.Entity.ShopEntity;
import com.capstone.campuseats.Service.ItemService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemEntity>> getAllItems() {
        return new ResponseEntity<>(itemService.getAllItems(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ItemEntity>> getItemById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }

    @PostMapping("/shop-add-item/{shopId}")
    public ResponseEntity<?> addItemToShop(
            @PathVariable ObjectId shopId,
            @RequestPart("item") ItemEntity item,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            ItemEntity createdItem = itemService.createItem(item, image, shopId);
            return new ResponseEntity<>(Map.of("message", "Item created successfully", "itemId", createdItem.getId().toString()), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/shop-update-item/{itemId}")
    public ResponseEntity<?> updateItem(
            @PathVariable ObjectId itemId,
            @RequestPart("item") ItemEntity item,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            ItemEntity updatedItem = itemService.updateItem(itemId, item, image);
            return new ResponseEntity<>(Map.of("message", "Item updated successfully", "itemId", updatedItem.getId().toString()), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{shopId}/shop-items")
    public ResponseEntity<?> getItemsByShopId(@PathVariable ObjectId shopId) {
        try {
            List<ItemEntity> items = itemService.getItemsByShopId(shopId);
            if (items.isEmpty()) {
                return new ResponseEntity<>(Map.of("error", "No items found for this shop"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
