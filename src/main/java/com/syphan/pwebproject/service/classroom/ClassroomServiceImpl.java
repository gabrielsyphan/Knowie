package com.syphan.pwebproject.service.classroom;

import com.syphan.pwebproject.model.dto.ClassroomDto;
import com.syphan.pwebproject.model.dto.EnrollDto;
import com.syphan.pwebproject.model.dto.LinkExamClassroomDto;
import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.Classroom;
import com.syphan.pwebproject.model.entity.Exam;
import com.syphan.pwebproject.model.entity.User;
import com.syphan.pwebproject.model.mapper.ClassroomMapper;
import com.syphan.pwebproject.model.mapper.UserMapper;
import com.syphan.pwebproject.repository.ClassroomRepository;
import com.syphan.pwebproject.repository.ExamRepository;
import com.syphan.pwebproject.repository.UserRepository;
import com.syphan.pwebproject.service.exam.ExamService;
import com.syphan.pwebproject.util.StringGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    private final ExamRepository examRepository;

    private final UserRepository userRepository;

    private final ExamService examService;

    @Autowired
    public ClassroomServiceImpl(ClassroomRepository classroomRepository, ExamRepository examRepository, UserRepository userRepository, ExamService examService) {
        this.classroomRepository = classroomRepository;
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.examService = examService;
    }

    @Override
    public ClassroomDto create(ClassroomDto obj) throws Exception {
        try {
            Classroom classroomEntity = ClassroomMapper.INSTANCE.dtoToEntity(obj);
            classroomEntity.setCode(StringGenerator.generateCode(6));

            User owner = this.userRepository.findById(obj.getOwner().getId()).orElseThrow(
                    () -> new Exception("ClassroomServiceImpl - create: Owner not found")
                );
            classroomEntity.setOwner(owner);

            if(classroomEntity.getId() != null) {
                Classroom classroomEntityFound = this.classroomRepository.findById(classroomEntity.getId()).orElseThrow(
                        () -> new Exception("ClassroomServiceImpl - create/update: Classroom not found")
                );
                Classroom classroomEntityUpdated = ClassroomMapper.INSTANCE.updateEntity(classroomEntity, classroomEntityFound);
                return ClassroomMapper.INSTANCE.entityToDto(this.classroomRepository.saveAndFlush(classroomEntityUpdated));
            }

            return ClassroomMapper.INSTANCE.entityToDto(this.classroomRepository.saveAndFlush(classroomEntity));
        } catch (Exception e) {
            throw new Exception("ClassroomServiceImpl - create: " + e.getMessage());
        }
    }

    @Override
    public void delete(long id) {
        try {
            Optional<Classroom> classroomEntity = this.classroomRepository.findById(id);
            if(classroomEntity.isPresent()) {
                this.classroomRepository.delete(classroomEntity.get());
            } else {
                throw new Exception("ClassroomServiceImpl - delete: Classroom not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("ClassroomServiceImpl - delete: " + e.getMessage());
        }
    }

    @Override
    public List<ClassroomDto> findAll() {
        return this.classroomRepository.findAll().stream().map(ClassroomMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public ClassroomDto findById(long id) {
        Classroom classroomEntity = this.classroomRepository.findById(id).orElseThrow(
                () -> new RuntimeException("ClassroomServiceImpl - findById: Classroom not found")
        );

        ClassroomDto classroomDto = ClassroomMapper.INSTANCE.entityToDto(classroomEntity);
        classroomDto.getExams().forEach(this.examService::updateExamStatus);

        return classroomDto;
    }

    @Override
    public ClassroomDto enroll(EnrollDto enrollDto) throws Exception {
        try {
            Classroom classroomEntity = this.classroomRepository.findByCode(enrollDto.getClassroomCode()).orElseThrow(
                    () -> new Exception("ClassroomServiceImpl - enroll: Classroom not found")
            );

            User userEntity = this.userRepository.findById(enrollDto.getUserId()).orElseThrow(
                    () -> new Exception("ClassroomServiceImpl - enroll: User not found")
            );

            classroomEntity.getUsers().add(userEntity);
            return ClassroomMapper.INSTANCE.entityToDto(this.classroomRepository.saveAndFlush(classroomEntity));
        } catch (Exception e) {
            throw new Exception("ClassroomServiceImpl - enroll: " + e.getMessage());
        }
    }

    @Override
    public ClassroomDto create(ClassroomDto classroomDto, HttpSession session) throws Exception {
        UserDto user = (UserDto) session.getAttribute("user");
        classroomDto.setOwner(user);
        return this.create(classroomDto);
    }

    @Override
    public List<ClassroomDto> findAllByOwner(UserDto user) throws Exception {
        User userEntity;
        if(this.isAdmin(user)) {
            userEntity = UserMapper.INSTANCE.dtoToEntity(user);
        } else {
            userEntity = this.userRepository.findById(user.getId()).orElseThrow(
                    () -> new Exception("ClassroomServiceImpl - findAllByOwner: User not found")
            );
        }

        if(user.getUserType() == 1) {
            return this.classroomRepository.findAllByUsers(userEntity).stream().map(ClassroomMapper.INSTANCE::entityToDto).toList();
        }

        return this.classroomRepository.findAllByOwner(userEntity).stream().map(ClassroomMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public void join(ClassroomDto classroomDto, UserDto user ) throws Exception {
        Classroom classroomEntity = this.classroomRepository.findByCode(classroomDto.getCode()).orElseThrow(
                () -> new RuntimeException("ClassroomServiceImpl - join: Classroom not found")
        );

        User userEntity = this.userRepository.findById(user.getId()).orElseThrow(
                () -> new Exception("ClassroomServiceImpl - findAllByOwner: User not found")
        );

        classroomEntity.getUsers().add(userEntity);
        this.classroomRepository.saveAndFlush(classroomEntity);
    }

    @Override
    public void linkExamToClassroom(LinkExamClassroomDto linkExamClassroomDto) {
        Classroom classroomEntity = this.classroomRepository.findById(linkExamClassroomDto.getClassroomId()).orElseThrow(
                () -> new RuntimeException("ClassroomServiceImpl - linkExamToClassroom: Classroom not found")
        );

        Exam examEntity = this.examRepository.findByCode(linkExamClassroomDto.getExamCode()).orElseThrow(
                () -> new RuntimeException("ClassroomServiceImpl - linkExamToClassroom: Exam not found")
        );

        classroomEntity.getExams().add(examEntity);

        this.classroomRepository.saveAndFlush(classroomEntity);
    }

    private boolean isAdmin(UserDto user) {
        return user.getId() == 0;
    }
}
