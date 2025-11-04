package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupAddOfficeRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupAddUserRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupDetailedDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.GroupService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.OfficeService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/api/group")
public class GroupController {
    @Autowired
    private UserService users;
    
    @Autowired
    private GroupService groups;

    @Autowired
    private OfficeService offices;

    @GetMapping(value = "/")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        List<GroupDTO> list = this.groups.getAllWithoutDetails().stream().map(GroupDTO::fromEntity).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<GroupDetailedDTO> getDetailedGroup(@PathVariable @NotNull Long groupId) {
        Group group = this.getGroupById(groupId);
        return ResponseEntity.ok(GroupDetailedDTO.fromEntity(group));
    }

    @PostMapping(value = "/")
    public ResponseEntity<GroupDetailedDTO> createGroup(@Valid @RequestBody GroupCreateRequestDTO req) {
        Group group = this.groups.create(req.name());
        return ResponseEntity.ok(new GroupDetailedDTO(group.getId(), group.getName(), List.of(), List.of()));
    }

    @PostMapping(value = "/{groupId}/users")
    public ResponseEntity<GroupDetailedDTO> addUserToGroup(
        @NotNull @PathVariable Long groupId,
        @Valid @RequestBody GroupAddUserRequestDTO req
    ) {
        Group group = this.getGroupById(groupId);
        User user = this.getUserById(req.userId());
        group = this.groups.addUserToGroup(group, user);
        return ResponseEntity.ok(GroupDetailedDTO.fromEntity(group));
    }

    @DeleteMapping(value = "/{groupId}/users/{userId}")
    public ResponseEntity<GroupDetailedDTO> removeUserFromGroup(
        @NotNull @PathVariable Long groupId,
        @NotNull @PathVariable Long userId
    ) {
        Group group = this.getGroupById(groupId);
        group = this.groups.removeUserFromGroup(group, userId);
        return ResponseEntity.ok(GroupDetailedDTO.fromEntity(group));
    }

    @PostMapping(value = "/{groupId}/offices")
    public ResponseEntity<GroupDetailedDTO> addOfficeToGroup(
        @NotNull @PathVariable Long groupId,
        @Valid @RequestBody GroupAddOfficeRequestDTO req
    ) {
        Group group = this.getGroupById(groupId);
        Office office = this.getOfficeById(req.officeId());
        group = this.groups.addOfficeToGroup(group, office);
        return ResponseEntity.ok(GroupDetailedDTO.fromEntity(group));
    }

    @DeleteMapping(value = "/{groupId}/offices/{officeId}")
    public ResponseEntity<GroupDetailedDTO> removeOfficeFromGroup(
        @NotNull @PathVariable Long groupId,
        @NotNull @PathVariable Long officeId
    ) {
        Group group = this.getGroupById(groupId);
        group = this.groups.removeOfficeFromGroup(group, officeId);
        return ResponseEntity.ok(GroupDetailedDTO.fromEntity(group));
    }

    private Group getGroupById(Long id) {
        return this.groups.getById(id)
            .orElseThrow(() -> new ProblemResponseException(HttpStatus.NOT_FOUND, "group with id '%d' not found", id));
    }

    private Office getOfficeById(Long id) {
        return this.offices.getById(id)
            .orElseThrow(() -> new ProblemResponseException(HttpStatus.NOT_FOUND, "office with id '%d' not found", id));
    }

    private User getUserById(Long id) {
        return this.users.getById(id)
            .orElseThrow(() -> new ProblemResponseException(HttpStatus.NOT_FOUND, "user with id '%d' not found", id));
    }
}
