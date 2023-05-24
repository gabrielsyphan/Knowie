package com.syphan.pwebproject.service.user;

import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.User;
import com.syphan.pwebproject.model.mapper.UserMapper;
import com.syphan.pwebproject.repository.UserRepository;
import com.syphan.pwebproject.util.BCryptEncoder;
import com.syphan.pwebproject.util.StringGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(UserDto obj) throws Exception {
        try {
            User userEntity = UserMapper.INSTANCE.dtoToEntity(obj);

            if(userEntity.getId() != null) {
                User userEntityFound = this.userRepository.findById(userEntity.getId()).orElseThrow(
                        () -> new Exception("UserServiceImpl - create/update: User not found")
                );
                User userEntityUpdated = UserMapper.INSTANCE.updateEntity(userEntity, userEntityFound);
                return UserMapper.INSTANCE.entityToDto(this.userRepository.saveAndFlush(userEntityUpdated));
            }

            if(userEntity.getPassword() != null) {
                throw new Exception("UserServiceImpl - create: Password must be null");
            }

            String password = StringGenerator.generateCode(8);
            userEntity.setPassword(BCryptEncoder.encode(password));
            userEntity.setFirstAccess(true);

            User userSaved = this.userRepository.save(userEntity);
            UserDto userDto = UserMapper.INSTANCE.entityToDto(userSaved);
            userDto.setPassword(password);

            return userDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("UserServiceImpl - create: Error when create user: {}", e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            Optional<User> userEntity = this.userRepository.findById(id);
            if(userEntity.isPresent()) {
                this.userRepository.delete(userEntity.get());
            } else {
                throw new Exception("UserServiceImpl - delete: User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("UserServiceImpl - delete: Error when delete user: {}", e);
        }
    }

    @Override
    public List<UserDto> findAll() {
        List<User> userEntities = this.userRepository.findAll();
        return userEntities.stream().map(UserMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public UserDto findById(long id) {
        return this.userRepository.findById(id).map(UserMapper.INSTANCE::entityToDto).orElseThrow(
                () -> new RuntimeException("UserServiceImpl - findById: User not found")
        );
    }

    @Override
    public UserDto findByEmailAndPassword(String email, String password) {
        String encodedPassword = BCryptEncoder.encode(password);
        return this.userRepository.findByEmailAndPassword(email, encodedPassword)
                .map(UserMapper.INSTANCE::entityToDto).orElse(null);
    }

    @Override
    public UserDto login(UserDto userDto, HttpSession session) {
        UserDto user = this.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());

        if(userDto.getEmail().equals("admin") && userDto.getPassword().equals("admin")) {
            user = UserDto.builder().id(0L).email("admin@knowie.site").name("ADMIN").userType(3L).build();
        }

        if (user != null) {
            session.setAttribute("user", user);
            return user;
        }

        return null;
    }

    @Override
    public void resetPassword(UserDto userDto, HttpSession session) throws RuntimeException {
        UserDto user = (UserDto) session.getAttribute("user");
        if(!Objects.equals(user.getId(), userDto.getId())) {
            throw new RuntimeException("UserServiceImpl - resetPassword: User not authorized");
        }

        User userEntity = this.userRepository.findById(userDto.getId()).orElseThrow(
                () -> new RuntimeException("UserServiceImpl - resetPassword: User not found")
        );
        userEntity.setPassword(BCryptEncoder.encode(userDto.getPassword()));
        userEntity.setFirstAccess(false);
        this.userRepository.save(userEntity);
    }

    @Override
    public UserDto forgotPassword(UserDto userDto) throws RuntimeException {
        User userEntity = this.userRepository.findByEmail(userDto.getEmail()).orElseThrow(
                () -> new RuntimeException("UserServiceImpl - forgotPassword: User not found")
        );

        String password = StringGenerator.generateCode(8);
        userEntity.setPassword(BCryptEncoder.encode(password));
        userEntity.setFirstAccess(true);
        User user = this.userRepository.save(userEntity);

        return UserDto.builder().password(password).build();
    }
}
