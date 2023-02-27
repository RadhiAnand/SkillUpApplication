package com.softura.skillup;

import com.softura.skillup.entity.Student;
import com.softura.skillup.utils.JavaassistUtils;
import javassist.bytecode.annotation.EnumMemberValue;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


class JavaassistUtilsTest {
      public static class Extension implements BeforeAllCallback {

        @Override
        public void beforeAll(ExtensionContext arg0) throws Exception {
            JavaassistUtils.addAnnotationToField(Student.class, "id", GeneratedValue.class, (annotation, constPool) -> {
                EnumMemberValue memberValue = new EnumMemberValue(constPool);
                memberValue.setType(GenerationType.class.getName());
                memberValue.setValue(GenerationType.IDENTITY.name());
                annotation.addMemberValue("strategy", memberValue);
            });
        }


    }
    @Test
    public void testAnnotation() throws NoSuchFieldException {
        Assert.assertNotNull(Student.class.getDeclaredField("id").getAnnotation(GeneratedValue.class));
    }
}