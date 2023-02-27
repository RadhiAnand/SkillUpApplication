package com.softura.skillup;

import com.softura.skillup.entity.Base;
import com.softura.skillup.utils.JavaassistUtils;
import javassist.bytecode.annotation.EnumMemberValue;
import org.example.configuration.annotation.EnableSampleLib;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@SpringBootApplication
@EnableSampleLib
@EnableAspectJAutoProxy
public class SkillupApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillupApplication.class, args);
        //afterAll();
    }

    public static void afterAll() {
        JavaassistUtils.addAnnotationToField(Base.class, "id", GeneratedValue.class, (annotation, constPool) -> {
            EnumMemberValue memberValue = new EnumMemberValue(constPool);
            memberValue.setType(GenerationType.class.getName());
            memberValue.setValue(GenerationType.IDENTITY.name());
            annotation.addMemberValue("strategy", memberValue);
        });
    }

}
