package bzu.edu.hotelManagmentAPI.service.RoomServiceImp;

import bzu.edu.hotelManagmentAPI.assembler.RoomResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.RoomPartialUpdateDto;
import bzu.edu.hotelManagmentAPI.dto.RoomRequestDto;
import bzu.edu.hotelManagmentAPI.dto.RoomResponseDto;
import bzu.edu.hotelManagmentAPI.dto.RoomUpdateDto;
import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.Floor;
import bzu.edu.hotelManagmentAPI.model.Room;
import bzu.edu.hotelManagmentAPI.model.RoomClass;
import bzu.edu.hotelManagmentAPI.model.RoomStatus;
import bzu.edu.hotelManagmentAPI.repository.FloorRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomClassRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomStatusRepository;
import bzu.edu.hotelManagmentAPI.security.SecurityUtils;
import bzu.edu.hotelManagmentAPI.service.RoomService;
import bzu.edu.hotelManagmentAPI.utils.LocalDateFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImp implements RoomService {
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final RoomResponseAssembler roomResponseAssembler;
    private final RoomClassRepository roomClassRepository;
    private final RoomStatusRepository roomStatusRepository;

    @Autowired
    public RoomServiceImp(RoomRepository roomRepository, RoomResponseAssembler roomResponseAssembler, FloorRepository floorRepository, RoomClassRepository roomClassRepository, RoomStatusRepository roomStatusRepository) {
        this.roomRepository = roomRepository;
        this.roomResponseAssembler = roomResponseAssembler;
        this.floorRepository = floorRepository;
        this.roomClassRepository = roomClassRepository;
        this.roomStatusRepository = roomStatusRepository;
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getAllRooms(Integer page, Integer size) {
        if (page == null){
            page = 0;
        }
        if (size == null){
            size = 20;
        }
        Pageable pageable = PageRequest.of(page, size);
        // throw new UnsupportedOperationException("Contact support");
        List<EntityModel<RoomResponseDto>> rooms = roomRepository.findAll(pageable).stream().map(roomResponseAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(rooms);
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getAllRooms(Integer floorNo, String date, Integer size) {
        LocalDate dateObj = LocalDateFormatter.parseDate(date);
        List<EntityModel<RoomResponseDto>> rooms = roomRepository.findAll(floorNo, dateObj, size).stream().map(roomResponseAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(rooms);
    }


    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getAvailableRooms() {
       return CollectionModel.of(roomRepository.findAll().stream().map(roomResponseAssembler::toModel).collect(Collectors.toList()));
    }

    @Override
    public EntityModel<RoomResponseDto> getRoomById(Long id) {
        SecurityUtils.checkIfAdminAuthority();
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found!"));
        return roomResponseAssembler.toModel(room);
    }

    @Override
    public EntityModel<RoomResponseDto> addNewRoom(RoomRequestDto roomRequestDto) {
        SecurityUtils.checkIfAdminAuthority();
        try{
            Floor floor = floorRepository.findByFloorNumber(roomRequestDto.getFloorNumber());
            if (floor == null){
                throw new RuntimeException("Floor " + roomRequestDto.getFloorNumber() + " not found, add floor first");
            }
            RoomClass roomClass = roomClassRepository.findByClassName(roomRequestDto.getClassName());
            if (roomClass == null){
                throw new RuntimeException("Room class " + roomRequestDto.getClassName() + " not found, add room class first");
            }
            RoomStatus roomStatus;
            if (roomRequestDto.getStatus().equals("AVAILABLE")){
                roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.Available).orElseThrow(() ->
                  new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.Available.name())); 
            }
            else {
                roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.Maintenance).orElseThrow(() ->
                  new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.Maintenance.name()));
            }
            Room room = new Room(floor, roomClass, roomStatus, roomRequestDto.getRoomNumber());
            return roomResponseAssembler.toModel(roomRepository.save(room));
        } catch (Exception e){
            System.out.println(e.getMessage()); //TODO: remove this line
            throw new RuntimeException("Failed to add new room!");
        }
    }

    @Override
    public void deleteRoom(Long id) {
        SecurityUtils.checkIfAdminAuthority();
        roomRepository.deleteById(id);
    }

    @Override
    public EntityModel<RoomResponseDto> updateRoom(Long id, RoomUpdateDto roomUpdateDto) {
        SecurityUtils.checkIfAdminAuthority();
        return roomRepository.findById(id).map(room -> {
            try{
                Floor floor = floorRepository.findByFloorNumber(roomUpdateDto.getFloorNumber());
                if (floor == null){
                   throw new RuntimeException("Floor " + roomUpdateDto.getFloorNumber()  + " not found, add floor first");
                }
                room.setFloor(floor);
                RoomClass roomClass = roomClassRepository.findByClassName(roomUpdateDto.getClassName());
                if (roomClass == null){
                    throw new RuntimeException("Room class " + roomUpdateDto.getClassName() + " not found, add room class first");
                }
                //** updating whole class of rooms here not individual rooms! **//
                roomClass.setNumBeds(roomUpdateDto.getNumBeds());
                roomClass.setPrice(roomUpdateDto.getPrice());
                roomClassRepository.save(roomClass);
                room.setRoomClass(roomClass);

                room.setRoomNumber(roomUpdateDto.getRoomNumber());
                if (roomUpdateDto.getStatus().equals("AVAILABLE")){
                    RoomStatus roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.Available).orElseThrow(() -> new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.Available.name()));
                    room.setStatus(roomStatus);
                }
                else {
                    RoomStatus roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.Reserved).orElseThrow(() -> new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.Reserved.name()));
                    room.setStatus(roomStatus);
            }
            return roomResponseAssembler.toModel(roomRepository.save(room));
        } catch (Exception e){
            System.out.println(e.getMessage()); //TODO: remove this line
            throw new RuntimeException("Failed to update room!");
        }
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found!"));
    
    }

    @Override
    public EntityModel<RoomResponseDto> partialUpdateRoom(Long id, RoomPartialUpdateDto roomPartialUpdateDto) {
        SecurityUtils.checkIfAdminAuthority();
        return roomRepository.findById(id).map(room -> {
            if (roomPartialUpdateDto.getFloorNumber() != null){
                Floor floor = floorRepository.findByFloorNumber(roomPartialUpdateDto.getFloorNumber());
                if (floor == null){
                   throw new RuntimeException("Floor " + roomPartialUpdateDto.getFloorNumber()  + " not found, add floor first");
                }
                room.setFloor(floor);
            }
            if (roomPartialUpdateDto.getNumBeds() != null){
                RoomClass roomClass = roomClassRepository.findByClassName(roomPartialUpdateDto.getClassName());
                if (roomClass == null){
                    throw new RuntimeException("Room class " + roomPartialUpdateDto.getClassName() + " not found, add room class first");
                }
                //** updating whole class of rooms here not individual rooms! **//
                if (roomPartialUpdateDto.getNumBeds() != null){
                    roomClass.setNumBeds(roomPartialUpdateDto.getNumBeds());
                }
                if (roomPartialUpdateDto.getPrice() != null){
                    roomClass.setPrice(roomPartialUpdateDto.getPrice());
                }
                roomClassRepository.save(roomClass);
                room.setRoomClass(roomClass);
            }
            if (roomPartialUpdateDto.getRoomNumber() != null){
                room.setRoomNumber(roomPartialUpdateDto.getRoomNumber());
            }
            if (roomPartialUpdateDto.getStatus() != null){
                if (roomPartialUpdateDto.getStatus().equals("AVAILABLE")){
                    RoomStatus roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.Available).orElseThrow(() -> new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.Available.name()));
                    room.setStatus(roomStatus);
                }
                else {
                    RoomStatus roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.Reserved).orElseThrow(() -> new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.Reserved.name()));
                    room.setStatus(roomStatus);
                }
            }
            return roomResponseAssembler.toModel(roomRepository.save(room));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getRoomsBySize(Integer size, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if (size == null){
            size = 20;
        }
        if (page == null){
            page = 0;
        }
       return CollectionModel.of(roomRepository.findAllBySize(size, pageable).stream().map(roomResponseAssembler::toModel).collect(Collectors.toList()));
    }
}
