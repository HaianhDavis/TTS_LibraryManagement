package com.example.TTS_LibraryManagement.service.impl;

import com.example.TTS_LibraryManagement.dto.request.User.UserCreationRequest;
import com.example.TTS_LibraryManagement.dto.request.User.UserSearchRequest;
import com.example.TTS_LibraryManagement.dto.request.User.UserUpdateRequest;
import com.example.TTS_LibraryManagement.dto.response.User.UserResponse;
import com.example.TTS_LibraryManagement.entity.Role;
import com.example.TTS_LibraryManagement.entity.User;
import com.example.TTS_LibraryManagement.exception.AppException;
import com.example.TTS_LibraryManagement.exception.ErrorCode;
import com.example.TTS_LibraryManagement.mapper.UserMapper;
import com.example.TTS_LibraryManagement.repository.RoleRepo;
import com.example.TTS_LibraryManagement.repository.UserRepo;
import com.example.TTS_LibraryManagement.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepo userRepo;
    UserMapper userMapper;
    RoleRepo roleRepo;
    PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepo.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        if (userRepo.existsByIdentityNumber(request.getIdentityNumber())){
            throw new AppException(ErrorCode.IDENTITY_NUMBER_EXISTED);
        }
        User user = userMapper.toUserCreate(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Role role = roleRepo.findByRoleGroupCodeAndIsDeletedFalse("USER").orElseThrow();
        role.setUsers(Set.of(user));
        return userMapper.toUserResponse(userRepo.save(user));
    }

    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepo
                .findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserUpdateRequest currentUserDto = userMapper.toUserUpdateRequest(user);
        if(request.equals(currentUserDto)){
            throw new AppException(ErrorCode.USER_NOT_CHANGED);
        }
        userMapper.toUserUpdate(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return userMapper.toUserResponse(userRepo.save(user));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepo
                .findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setIsDeleted(1);
        user.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        userRepo.save(user);
    }

    @Transactional
    public void restoreUser(Long userId) {
        User user = userRepo
                .findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getIsDeleted() == 0) {
            throw new AppException(ErrorCode.USER_NOT_DELETED);
        }
        user.setIsDeleted(0);
        user.setDeletedAt(null);
        user.setDeletedBy(null);
        userRepo.save(user);
    }

    @Transactional
    public List<UserResponse> getUsers() {
        log.info("in method getUsers");
        List<User> users = userRepo.findUsersByIsDeletedFalse();
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return users.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    @Transactional
    public UserResponse getUserById(Long id) {
        return userMapper.toUserResponse(userRepo
                .findUserByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public Page<UserResponse> getUsersByPage(int page, int size, UserSearchRequest request) {
        if(page < 1) {
            throw new AppException(ErrorCode.PAGE_NO_ERROR);
        }
        if(size < 1) {
            throw new AppException(ErrorCode.PAGE_SIZE_ERROR);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), 0)); // Chỉ lấy User chưa xóa mềm

            if (request != null) {
                if (request.getFullname() != null && !request.getFullname().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("fullname")),
                            "%" + request.getFullname().toLowerCase() + "%"));
                }
                if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                    predicates.add(cb.equal(root.get("phoneNumber"), request.getPhoneNumber()));
                }
                if (request.getIdentityNumber() != null && !request.getIdentityNumber().isEmpty()) {
                    predicates.add(cb.equal(root.get("identityNumber"), request.getIdentityNumber()));
                }
                if (request.getAge() != null) {
                    predicates.add(cb.equal(root.get("age"), request.getAge()));
                }
                if (request.getBirthday() != null) {
                    predicates.add(cb.equal(root.get("birthday"), request.getBirthday()));
                }
                if (request.getAddress() != null && !request.getAddress().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("address")),
                            "%" + request.getAddress().toLowerCase() + "%"));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<User> users = userRepo.findAll(spec, pageable);
        return users.map(userMapper::toUserResponse);
    }
}
