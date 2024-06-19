package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.RoomResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bzu.edu.hotelManagmentAPI.service.RoomService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/rooms", params = "version=2") // query versioning
public class RoomControllerV2 {

    // public RoomControllerV2(RoomService roomService) {
    //     super(roomService);
    // }

    protected final RoomService roomService;

    @Autowired
    public RoomControllerV2(RoomService roomService) {
        this.roomService = roomService;
    }
    @GetMapping
    public CollectionModel<EntityModel<RoomResponseDto>> getAllRoomsPageable(
            @RequestParam(required = false) Integer floorNo,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return roomService.getAllRoomsPageable(floorNo, pageable);
    }

    @GetMapping("/size/{count}")
    public ResponseEntity<?> getRoomsBySize(@PathVariable Integer count, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return ResponseEntity.ok(roomService.getRoomsBySize(count, page, pageSize));
    }

}
