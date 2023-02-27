package com.softura.skillup.service;

import com.softura.skillup.constants.DayIdEnum;
import com.softura.skillup.constants.ErrorCode;
import com.softura.skillup.entity.ScheduleDayRequest;
import com.softura.skillup.entity.ScheduleDayResponse;
import com.softura.skillup.entity.Student;
import com.softura.skillup.exception.RecordNotFoundException;
import com.softura.skillup.exception.ScheduleDayException;
import com.softura.skillup.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.example.configuration.service.DateUtil;
import org.json.JSONArray;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class StudentService {

    Logger log2 = LoggerFactory.getLogger(StudentService.class);

    List<String> test = new ArrayList<>();


    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private KieContainer kieContainer;

    @Autowired
    private DateUtil dateUtil;
    public void createStudent(Student student) {
        studentRepository.save(student);
    }

//    public List<Student> getAllStudents() {
//        return studentRepository.findAll();
//    }

    public String getAllStudents() {
        return dateUtil.getDate().toString();
    }

    public DayOfWeek getStudentById(Long id) throws ParseException {
        return getScheduledDay();
    }

    protected Student getIfExistById(Long id) throws RecordNotFoundException {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public Student updatedStudent(Student student) throws ScheduleDayException {
        //getIfExistById(student.getId());

        return null;
    }

    public void deleteStudent(Long id) {
        Student student = getIfExistById(id);
        studentRepository.delete(student);
    }

    public DayOfWeek getScheduledDay() throws ParseException, ScheduleDayException {
        Date startDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = "145:54";

        List<DayIdEnum> dayIds = new ArrayList<>();
        dayIds.add(DayIdEnum.SUN);
        dayIds.add(DayIdEnum.MON);
        dayIds.add(DayIdEnum.TUE);

        long hoursToAdd = TimeUnit.HOURS.toMillis(Long.parseLong(startTime.split(":")[0]));
        long minutesToAdd = TimeUnit.MINUTES.toMillis(Long.parseLong(startTime.split(":")[1]));
        long startEpochDate = dateToEpoch(formatter.format(startDate));
        long scheduleTimeEpoch = startEpochDate + hoursToAdd + minutesToAdd;
        long startScheduleTimeEpoch = scheduleTimeEpoch;

        DayOfWeek scheduleDay = getDayOfWeek(startScheduleTimeEpoch);
        log2.info("Before rules time {}", startScheduleTimeEpoch);
        log2.info("scheduleday: {} ", scheduleDay);
        startScheduleTimeEpoch = calculateScheduleDay1(scheduleTimeEpoch, dayIds);
        log2.info("AFter rules time {}", startScheduleTimeEpoch);

        return getDayOfWeek(startScheduleTimeEpoch);
    }

    public long dateToEpoch(String dateText) throws java.text.ParseException {
        log2.info("dateToEpoch Entry: {} ", dateText);
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateText).getTime();
    }

    //rule engine
    private long calculateScheduleDay(long scheduleTimeEpoch, DayOfWeek scheduleDay, List<DayOfWeek> dayIds) {
        ScheduleDayResponse startScheduleTimeEpoch = new ScheduleDayResponse();
        startScheduleTimeEpoch.setStartScheduleTimeEpoch(scheduleTimeEpoch);
        ScheduleDayRequest scheduleDayRequest;

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("startScheduleTimeEpoch", startScheduleTimeEpoch);
        for (Object day : dayIds) {
            scheduleDayRequest = new ScheduleDayRequest(scheduleDay, day.toString());
            kieSession.insert(scheduleDayRequest);
            int fireCount = kieSession.fireAllRules();
            log2.info("fireCount {} ", fireCount);
        }

        kieSession.dispose();

        return startScheduleTimeEpoch.getStartScheduleTimeEpoch();
    }

    private long calculateScheduleDay1(long scheduleTimeEpoch, List<DayIdEnum> dayIds) throws
            ScheduleDayException {
        long startScheduleTimeEpoch = scheduleTimeEpoch;
        DayOfWeek scheduleDay = getDayOfWeek(startScheduleTimeEpoch);
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            String scheduleDaySubString = scheduleDay.toString().substring(0,3);
            if (dayIds.toString().contains(scheduleDaySubString))
                break;
            startScheduleTimeEpoch = startScheduleTimeEpoch + TimeUnit.DAYS.toMillis(1);
            scheduleDay = getDayOfWeek(startScheduleTimeEpoch);
        }
        return startScheduleTimeEpoch;
    }

    //        while (true) {
//            if (isScheduledDayExistsInDayIds(dayIds, scheduleDay.toString()))
//                break;
//            startScheduleTimeEpoch = startScheduleTimeEpoch + TimeUnit.DAYS.toMillis(1);
//            scheduleDay = getDayOfWeek(startScheduleTimeEpoch);
//        }

    private boolean isScheduledDayExistsInDayIds(List<DayIdEnum> dayIds, String scheduleDay) {
        for (int i = 0; i < dayIds.size(); i++) {
            if (scheduleDay.contains(dayIds.get(i).name())) return true;
        }
        return false;
    }

    private static DayOfWeek getDayOfWeek(long epochSecond) {
        return Instant.ofEpochSecond(epochSecond).atOffset(ZoneOffset.UTC).getDayOfWeek();
    }
}

