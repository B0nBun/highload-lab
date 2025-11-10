package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupAddOfficeRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupAddUserRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group.GroupDetailedDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.GroupService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/group")
public class GroupController {
    @Autowired private GroupService groups;

    @GetMapping(value = "/")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        List<GroupDTO> list =
                this.groups.getAllWithoutDetails().stream().map(GroupDTO::fromModel).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<GroupDetailedDTO> getDetailedGroup(@PathVariable @NotNull Long groupId) {
        return this.groups
                .getById(groupId)
                .map(GroupDetailedDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElseThrow(
                        () ->
                                new ObjectNotFoundException("group with id '%d'", groupId)
                                        .responseException());
    }

    @PostMapping(value = "/")
    public ResponseEntity<GroupDetailedDTO> createGroup(
            @Valid @RequestBody GroupCreateRequestDTO req) {
        Group group = this.groups.create(req.name());
        return ResponseEntity.ok(
                new GroupDetailedDTO(group.getId(), group.getName(), List.of(), List.of()));
    }

    @PostMapping(value = "/{groupId}/users")
    public ResponseEntity<GroupDetailedDTO> addUserToGroup(
            @NotNull @PathVariable Long groupId, @Valid @RequestBody GroupAddUserRequestDTO req) {
        try {
            Group group = this.groups.addUserToGroup(groupId, req.userId());
            return ResponseEntity.ok(GroupDetailedDTO.fromModel(group));
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }

    @DeleteMapping(value = "/{groupId}/users/{userId}")
    public ResponseEntity<GroupDetailedDTO> removeUserFromGroup(
            @NotNull @PathVariable Long groupId, @NotNull @PathVariable Long userId) {
        try {
            Group group = this.groups.removeUserFromGroup(groupId, userId);
            return ResponseEntity.ok(GroupDetailedDTO.fromModel(group));
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }

    @PostMapping(value = "/{groupId}/offices")
    public ResponseEntity<GroupDetailedDTO> addOfficeToGroup(
            @NotNull @PathVariable Long groupId, @Valid @RequestBody GroupAddOfficeRequestDTO req) {
        try {
            Group group = this.groups.addOfficeToGroup(groupId, req.officeId());
            return ResponseEntity.ok(GroupDetailedDTO.fromModel(group));
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }

    @DeleteMapping(value = "/{groupId}/offices/{officeId}")
    public ResponseEntity<GroupDetailedDTO> removeOfficeFromGroup(
            @NotNull @PathVariable Long groupId, @NotNull @PathVariable Long officeId) {
        try {
            Group group = this.groups.removeOfficeFromGroup(groupId, officeId);
            return ResponseEntity.ok(GroupDetailedDTO.fromModel(group));
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }
}
