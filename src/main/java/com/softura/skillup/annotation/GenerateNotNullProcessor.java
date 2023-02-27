package com.softura.skillup.annotation;

import com.softura.skillup.utils.JavaassistUtils;
import com.softura.skillup.entity.Student;
import javassist.*;
import javassist.bytecode.*;

import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.EnumMemberValue;
import net.bytebuddy.agent.ByteBuddyAgent;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.tools.Diagnostic;


import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.function.BiConsumer;

import static org.apache.poi.xdgf.exceptions.XDGFException.error;


public class GenerateNotNullProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, messager.toString());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(GenerateNotNULL.class.getName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "this is inside process method");
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(GenerateNotNULL.class)) {
            TypeElement typeElement = (TypeElement) annotatedElement;

            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error("Only class can be annotated with AutoElement", annotatedElement);
                return false;
            }
            try {
                for (Field field : Class.forName(typeElement.asType().toString()).getDeclaredFields()) {
                    if (field.getName().equals("id")) {
                        JavaassistUtils.addAnnotationToField(Student.class, field.getName(), GeneratedValue.class, (annotation, constPool) -> {
                            EnumMemberValue memberValue = new EnumMemberValue(constPool);
                            memberValue.setType(GenerationType.class.getName());
                            memberValue.setValue(GenerationType.IDENTITY.name());
                            annotation.addMemberValue("strategy", memberValue);
                        });
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static void addAnnotationToField(Class<?> clazz, Field field, Class<?> annotationClass,
                                            BiConsumer<Annotation, ConstPool> initAnnotation) throws NotFoundException {
        ClassPath path = ClassPool.getDefault().insertClassPath(new ClassClassPath(Student.class));
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(path);
        CtClass ctClass;

        try {

            ctClass = pool.getCtClass(clazz.getName());
            if (ctClass.isFrozen()) {
                ctClass.defrost();
            }
            CtField ctField = ctClass.getDeclaredField(field.getName());
            ConstPool constPool = ctClass.getClassFile().getConstPool();

            Annotation annotation = new Annotation(annotationClass.getName(), constPool);
            if (initAnnotation != null) {
                initAnnotation.accept(annotation, constPool);
            }

            AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            ctField.getFieldInfo().addAttribute(attr);
            attr.addAnnotation(annotation);

            retransformClass(clazz, ctClass.toBytecode());
        } catch (NotFoundException | IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    private static void retransformClass(Class<?> clazz, byte[] byteCode) {
        ClassFileTransformer cft = new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                return byteCode;
            }
        };

        Instrumentation instrumentation = ByteBuddyAgent.install();
        try {
            instrumentation.addTransformer(cft, true);
            instrumentation.retransformClasses(clazz);
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        } finally {
            instrumentation.removeTransformer(cft);
        }
    }

//    private static AnnotationsAttribute getAnnotationsAttributeFromField(CtField ctField) {
//        List<AttributeInfo> attrs = ctField.getFieldInfo().getAttributes();
//        AnnotationsAttribute attr = null;
//        if (attrs != null) {
//            Optional<AttributeInfo> optional = attrs.stream()
//                    .filter(AnnotationsAttribute.class::isInstance)
//                    .findFirst();
//            if (optional.isPresent()) {
//                attr = (AnnotationsAttribute) optional.get();
//            }
//        }
//        return attr;
//    }

}
