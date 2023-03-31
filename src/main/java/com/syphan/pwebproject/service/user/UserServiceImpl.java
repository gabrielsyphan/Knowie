package com.syphan.pwebproject.service.user;

import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.UserEntity;
import com.syphan.pwebproject.model.mapper.UserMapper;
import com.syphan.pwebproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            UserEntity userEntity = UserMapper.INSTANCE.dtoToEntity(obj);
            UserEntity userSaved = this.userRepository.save(userEntity);
            return UserMapper.INSTANCE.entityToDto(userSaved);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("UserServiceImpl - create: Error when create user: {}", e);
        }
    }

    @Override
    public void delete(UserDto obj) {

    }

    @Override
    public List<UserDto> findAll() {
        List<UserEntity> userEntities = this.userRepository.findAll();
        return userEntities.stream().map(UserMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public UserDto findById(long id) {
        return null;
    }
}
