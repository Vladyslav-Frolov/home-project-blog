package com.softserveinc.ita.homeprojectblog.service.impl;

import com.softserveinc.ita.homeprojectblog.dto.PasswordDto;
import com.softserveinc.ita.homeprojectblog.dto.RoleDto;
import com.softserveinc.ita.homeprojectblog.dto.UserDto;
import com.softserveinc.ita.homeprojectblog.entity.RoleEntity;
import com.softserveinc.ita.homeprojectblog.entity.UserEntity;
import com.softserveinc.ita.homeprojectblog.mapper.UserMapperService;
import com.softserveinc.ita.homeprojectblog.repository.RoleRepository;
import com.softserveinc.ita.homeprojectblog.repository.UserRepository;
import com.softserveinc.ita.homeprojectblog.service.UserService;
import com.softserveinc.ita.homeprojectblog.util.Checkout;
import com.softserveinc.ita.homeprojectblog.util.page.Sorter;
import com.softserveinc.ita.homeprojectblog.util.query.EntitySpecificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.softserveinc.ita.homeprojectblog.util.Constants.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    UserMapperService userMapper;

    PasswordEncoder passwordEncoder;

    Sorter sorter;

    Checkout checkout;

    @Qualifier("entitySpecificationService")
    EntitySpecificationService<UserEntity> entitySpecificationService;

    @Override
    public Page<UserDto> getUsers(BigDecimal id, String name, String sort, Integer pageNum, Integer pageSize) {
        Map<String, String> predicateMap = new HashMap<>();
        predicateMap.put("id", id != null ? id.toString() : null);
        predicateMap.put("name", name);

        var check = checkout.checkoutAndSetDefaults(sort, pageNum, pageSize);
        var specification = entitySpecificationService.getSpecification(predicateMap);
        var pageRequest = PageRequest.of(check.getPageNum(), check.getPageSize(),
                sorter.getSorter(check.getSort()));

        var pageEntities = userRepository.findAll(specification, pageRequest);

        return userMapper.toUserDtoPage(pageEntities);
    }

    @Override
    public UserDto updateCurrentUser(UserDto userDto) {
        var currentUserDto = getCurrentUser();
        var userEntity = userMapper.toUserEntityFromUsersDto(currentUserDto, userDto);
        userEntity.setUpdatedOn(OffsetDateTime.now());

        var updatedUserEntity = userRepository.save(userEntity);
        return userMapper.toUserDto(updatedUserEntity);
    }

    @Override
    public RoleDto getUserRole(BigDecimal id) {
        var userDto = getUser(id);
        return Optional.of(userDto.getRole()).orElseThrow(
                () -> new EntityNotFoundException(String.format(USER_ROLE_NOT_EXIST_FORMAT, id)));
    }

    @Override
    public void updateCurrentUserPassword(PasswordDto passwordDto) {
        var userDto = getCurrentUser();
        if (passwordEncoder.matches(passwordDto.getOldPassword(), userDto.getPassword())) {
            userDto.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        } else {
            throw new ValidationException(INCORRECT_OLD_PASSWORD);
        }
        userRepository.save(userMapper.toUserEntity(userDto));
    }

    @Override
    public RoleDto updateUserRole(BigDecimal id, RoleDto roleDto) {
        var userDto = getUser(id);
        var userEntity = userMapper.toUserEntity(userDto);
        var roleEntity = roleRepository.findByName(
                RoleEntity.NameEnum.valueOf(roleDto.getName().name())).orElseThrow(
                () -> new EntityNotFoundException(String.format(ROLE_NOT_EXIST_FORMAT, roleDto.getName().name())));
        userEntity.setRole(roleEntity);
        userEntity.setUpdatedOn(OffsetDateTime.now());
        var updatedUserEntity = userRepository.save(userEntity);
        return userMapper.toRoleDto(updatedUserEntity.getRole());
    }

    @Override
    public UserDto getUser(BigDecimal id) {
        var userEntity = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(USER_NOT_FOUND_FORMAT, id)));
        return userMapper.toUserDto(userEntity);
    }

    @Override
    public UserDto getUserByName(String username) {
        var currentUserEntity = userRepository.findByName(username).orElseThrow(() ->
                new UsernameNotFoundException(USER_NOT_EXIST));
        return userMapper.toUserDto(currentUserEntity);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        var userEntity = userMapper.toUserEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        var roleEntity = roleRepository.findByName(RoleEntity.NameEnum.BLOGGER)
                .orElse(new RoleEntity(null, RoleEntity.NameEnum.BLOGGER));

        userEntity.setRole(roleEntity);
        userEntity.setCreateOn(OffsetDateTime.now());

        userRepository.save(userEntity);
        return userMapper.toUserDto(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto bodyDto, BigDecimal id) {
        var oldUserEntity = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(USER_NOT_FOUND_FORMAT, id)));
        var newUserEntity = userMapper.toUserEntity(bodyDto);

        var userEntity = userMapper.toUserEntityFromUsersEntity(newUserEntity, oldUserEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setUpdatedOn(OffsetDateTime.now());

        var savedUserEntity = userRepository.save(userEntity);
        return userMapper.toUserDto(savedUserEntity);
    }

    @Override
    public void removeUser(BigDecimal id) {
        var userEntity = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(USER_NOT_FOUND_FORMAT, id)));
        userRepository.deleteById(userEntity.getId());
    }

    @Override
    public UserDto getCurrentUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return getUserByName(username);
    }

}