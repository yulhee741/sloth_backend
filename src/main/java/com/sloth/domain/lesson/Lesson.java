package com.sloth.domain.lesson;

import com.sloth.domain.BaseEntity;
import com.sloth.domain.category.Category;
import com.sloth.domain.lesson.constant.LessonStatus;
import com.sloth.domain.member.Member;
import com.sloth.domain.site.Site;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
@ToString(of = {"lessonId", "lessonName", "presentNumber", "totalNumber", "startDate", "endDate",
        "price", "alertDays", "message", "isFinished"})
@EqualsAndHashCode(of = "lessonId", callSuper = false)
@Table(name = "lesson")
public class Lesson extends BaseEntity  {

    @Id @GeneratedValue
    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(nullable = false, length = 150)
    private String lessonName;

    private int presentNumber;

    private int totalNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int price;

    @Column(nullable = true)
    private String alertDays;

    @Column(length = 200)
    private String message;

    @Column(nullable = false)
    private Boolean isFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Lesson(Long lessonId, Member member, String lessonName, LocalDate startDate, LocalDate endDate, int totalNumber,
                  int price, String alertDays, String message, Site site, Category category) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalNumber = totalNumber;
        this.price = price;
        this.alertDays = alertDays;
        this.site = site;
        this.category = category;
        this.message = message;
        this.isFinished = false;

        connectMember(member);
    }

    private void connectMember(Member member) {
        this.member = member;
        if(member != null) {
            member.getLessons().add(this);
        }
    }

    public void updatePresentNumber(int count) {
        if (this.isFinished) {
            return;
        } else {
            this.presentNumber += count;
        }

        if (this.presentNumber >= totalNumber) {
            this.presentNumber = totalNumber;
            this.isFinished = true;
        } else if (this.presentNumber <= 0) {
            this.presentNumber = 0;
            this.isFinished = false;
        }
    }

    public boolean isDoingLesson() {
        return this.getStartDate().isBefore(LocalDate.now()) && this.getEndDate().isAfter(LocalDate.now());
    }

    public int getRemainDay() {
        return Period.between(LocalDate.now(), this.endDate).getDays();
    }

    public int getCurrentProgressRate() {
        return (int) Math.floor((double) this.presentNumber / (double) this.totalNumber * 100);
    }

    public int getGoalProgressRate() {
        return (int) Math.floor( (double) Period.between(this.startDate, LocalDate.now()).getDays() / (double) Period.between(this.startDate, this.endDate).getDays() * 100);
    }

    public int getWastePrice() {
        int wastePrice = (int) (price * ((double) (getGoalProgressRate() - getCurrentProgressRate()) / (double) 100));
        return wastePrice >= 0 ? wastePrice : 0;
    }

    public LessonStatus getLessonStatus() {
        if(LocalDate.now().isBefore(endDate)){
            return LessonStatus.CURRENT;
        }

        return LessonStatus.PAST;
    }

    public void updateLesson(String lessonName, Integer totalNumber, Category category, Site site) {
        this.lessonName = lessonName;
        this.totalNumber = totalNumber;
        this.category = category;
        this.site = site;
    }
}
