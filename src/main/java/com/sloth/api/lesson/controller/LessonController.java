package com.sloth.api.lesson.controller;

import com.sloth.api.lesson.constant.LessonType;
import com.sloth.api.lesson.dto.*;
import com.sloth.api.lesson.service.LessonService;
import com.sloth.domain.lesson.Lesson;
import com.sloth.exception.InvalidParameterException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "들은 강의 수 수정 API", description = "들은 강의 수 추가 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    @PatchMapping(value = "/number/plus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonNumberDto.Response> plusPresentNumber(@Valid @RequestBody LessonNumberDto.Request request) {
        Lesson lesson = lessonService.plusPresentNumber(request.getLessonId(), request.getCount());
        LessonNumberDto.Response response = LessonNumberDto.Response.create(lesson);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "들은 강의 수 수정 API", description = "들은 강의 수 감소 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    @PatchMapping(value = "/number/minus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonNumberDto.Response> minusPresentNumber(@Valid @RequestBody LessonNumberDto.Request request) {
        Lesson lesson = lessonService.minusPresentNumber(request.getLessonId(), request.getCount());
        LessonNumberDto.Response response = LessonNumberDto.Response.create(lesson);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "강의 조회 API", description = "강의 상세 조회 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonDetailDto.Response> getLessonDetail(@Valid @RequestBody LessonDetailDto.Request request) {
        Lesson lesson = lessonService.findLessonWithSiteCategory(request.getLessonDetailId());
        LessonDetailDto.Response lessonDetail = LessonDetailDto.Response.create(lesson);
        return ResponseEntity.ok(lessonDetail);
    }

    @Operation(summary = "멤버가 현재 진행중인 강의 조회 API", description = "멤버가 현재 진행중인 강의 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    @GetMapping("/doing")
    public ResponseEntity<List<DoingLessonDto.Response>> getDoingLesson(@Valid @RequestBody DoingLessonDto.Request request) {
        List<Lesson> lessons = lessonService.getDoingLessons(request.getMemberId());
        List<DoingLessonDto.Response> doingLessonResponses = new ArrayList<>();
        for (Lesson lesson : lessons) {
            DoingLessonDto.Response doingLessonResponse = DoingLessonDto.Response.create(lesson);
            doingLessonResponses.add(doingLessonResponse);
        }
        return ResponseEntity.ok(doingLessonResponses);
    }


    @Operation(summary = "강의 수정 API", description = "강의 상세 내용 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    @PatchMapping("/{lessonId}")
    public ResponseEntity<LessonUpdateDto.Response> updateLesson(@PathVariable("lessonId") Long lessonId,
                                                                                @Valid @RequestBody LessonUpdateDto.Request lessonUpdateDto,
                                                                                BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError error : allErrors) {
                sb.append(error.getDefaultMessage());
            }

            throw new InvalidParameterException(sb.toString());
        }

        LessonUpdateDto.Response responseLessonUpdateDto = LessonUpdateDto.Response.builder()
                .lessonId(lessonId)
                .lessonName(lessonUpdateDto.getLessonName())
                .totalNumber(lessonUpdateDto.getTotalNumber())
                .siteId(lessonUpdateDto.getSiteId())
                .categoryId(lessonUpdateDto.getCategoryId())
                .build();

        return ResponseEntity.ok(responseLessonUpdateDto);
    }

    @Operation(summary = "강의 목록 조회 API", description = "강의 목록 리스트 조회 API")
    @GetMapping("/list")
    public ResponseEntity<List<MainLessonDto.Response>> getLessonList() {

        List<MainLessonDto.Response> mainLessonDtos = new ArrayList<>();


        // 1. 진행중인 강의
        MainLessonDto.Response currentLesson1 = MainLessonDto.Response.builder()
                .lessonId(10L)
                .type(LessonType.CURRENT.name())
                .remainDay(10)
                .categoryName("디자인")
                .siteName("인프런")
                .lessonName("디자인 입문")
                .price(5000)
                .currentProgressRate(50)
                .goalProgressRate(60)
                .startDate(LocalDate.now().minusDays(10).getMonth().getValue() + "/" + "01")
                .endDate(LocalDate.now().plusDays(10).getMonth().getValue() + "/" + "01")
                .totalNumber(15)
                .build();

        MainLessonDto.Response currentLesson2 = MainLessonDto.Response.builder()
                .lessonId(10L)
                .type(LessonType.CURRENT.name())
                .remainDay(10)
                .categoryName("디자인")
                .siteName("인프런")
                .lessonName("포토샵 강의 입문")
                .price(350000)
                .currentProgressRate(80)
                .goalProgressRate(30)
                .startDate(LocalDate.now().minusDays(5).getMonth().getValue() + "/" + "01")
                .endDate(LocalDate.now().plusDays(10).getMonth().getValue() + "/" + "01")
                .totalNumber(13)
                .build();

        MainLessonDto.Response currentLesson3 = MainLessonDto.Response.builder()
                .lessonId(10L)
                .type(LessonType.CURRENT.name())
                .remainDay(10)
                .categoryName("디자인")
                .siteName("인프런")
                .lessonName("디자인이란 무엇인가?")
                .price(3500)
                .currentProgressRate(50)
                .goalProgressRate(50)
                .startDate(LocalDate.now().minusDays(5).getMonth().getValue() + "/" + "01")
                .endDate(LocalDate.now().plusDays(10).getMonth().getValue() + "/" + "01")
                .totalNumber(13)
                .build();

        mainLessonDtos.add(currentLesson1);
        mainLessonDtos.add(currentLesson2);
        mainLessonDtos.add(currentLesson3);

        // 2. 완료한 강의
        MainLessonDto.Response finishedLesson1 = MainLessonDto.Response.builder()
                .lessonId(10L)
                .type(LessonType.FINISH.name())
                .remainDay(0)
                .categoryName("개발")
                .siteName("인프런")
                .lessonName("파이썬 입문")
                .price(50000)
                .currentProgressRate(100)
                .goalProgressRate(100)
                .startDate(LocalDate.now().minusDays(10).getMonth().getValue() + "/" + "15")
                .endDate(LocalDate.now().plusDays(10).getMonth().getValue() + "/" + "15")
                .totalNumber(50)
                .isSuccess(true)
                .build();

        MainLessonDto.Response finishedLesson2 =MainLessonDto.Response.builder()
                .lessonId(11L)
                .type(LessonType.FINISH.name())
                .remainDay(0)
                .categoryName("개발")
                .siteName("인프런")
                .lessonName("데이터베이스 입문")
                .price(40000)
                .currentProgressRate(100)
                .goalProgressRate(100)
                .startDate(LocalDate.now().minusDays(10).getMonth().getValue() + "/" + "20")
                .endDate(LocalDate.now().plusDays(10).getMonth().getValue() + "/" + "20")
                .totalNumber(30)
                .isSuccess(true)
                .build();

        MainLessonDto.Response finishedLesson3 =MainLessonDto.Response.builder()
                .lessonId(11L)
                .type(LessonType.FINISH.name())
                .remainDay(0)
                .categoryName("개발")
                .siteName("인프런")
                .lessonName("데이터베이스 입문")
                .price(100000)
                .currentProgressRate(70)
                .goalProgressRate(100)
                .startDate(LocalDate.now().minusDays(5).getMonth().getValue() + "/" + "10")
                .endDate(LocalDate.now().plusDays(15).getMonth().getValue() + "/" + "20")
                .totalNumber(40)
                .isSuccess(false)
                .build();

        mainLessonDtos.add(finishedLesson1);
        mainLessonDtos.add(finishedLesson2);
        mainLessonDtos.add(finishedLesson3);

        return ResponseEntity.ok(mainLessonDtos);
    }
  
    @Operation(summary = "강의 생성 API", description = "강의 생성 API")
    @PostMapping
    public ResponseEntity<Long> saveLesson(@RequestBody MainLessonDto.Request requestDto) {
        Long lessonId = lessonService.saveLesson(requestDto);
        return new ResponseEntity<Long>(lessonId, HttpStatus.CREATED);
    }

}